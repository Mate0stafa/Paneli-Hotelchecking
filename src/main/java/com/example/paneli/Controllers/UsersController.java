package com.example.paneli.Controllers;

import com.example.paneli.DataObjects.Admin.UserProjection;
import com.example.paneli.DataObjects.NewUser;
import com.example.paneli.DataObjects.NewUserDto;
import com.example.paneli.DataObjects.PasswordDto;
import com.example.paneli.Models.*;
import com.example.paneli.Models.PanelUsers.Role;
import com.example.paneli.Models.PanelUsers.User;
import com.example.paneli.Repositories.*;
import com.example.paneli.Repositories.UserPanel.RoleRepository;
import com.example.paneli.Repositories.UserPanel.UserRepository;
import com.example.paneli.Services.Mail.JavaMailService;
import com.example.paneli.Services.Number.NumberService;
import com.example.paneli.Services.PasswordService;
import com.example.paneli.Services.RoleUniqueService;
import com.example.paneli.Services.UserServices.UserPanelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class UsersController {


    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordService passwordService;
    @Autowired
    OldPasswordRepository oldPasswordRepository;
    @Autowired
    private UserPanelService userPanelService;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private JavaMailService javaMailService;
    @Autowired
    private NumberService numberService;

@PreAuthorize("hasRole('ROLE_ADMIN')")
@GetMapping("/users")
public ModelAndView users(HttpServletRequest request,
                          ModelAndView modelAndView,
                          @RequestParam(required = false) String search,
                          @RequestParam(required = false) Long id) {
    if (request.isUserInRole("ROLE_ADMIN")) {

        modelAndView.addObject("search", search);
        modelAndView.addObject("id", id);
        modelAndView.setViewName("ROLE_ADMIN/users/usersList");
    }
    return modelAndView;
}


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/users/data")
    @ResponseBody
    public List<UserProjection> getUsersData(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String search) {

        List<UserProjection> users;

        if (id != null && id > 0) {
            users = userRepository.findProjectionById(id);
        } else if (search != null && !search.trim().isEmpty()) {
            users = userRepository.findProjectionByUsernameContaining(search);
        } else {
            users = userRepository.findAllUserProjections();
        }

        return users;
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/showUser")
    public ModelAndView showUser(@RequestParam(value ="userId") Integer userId, HttpServletRequest request, ModelAndView modelAndView){
        if (request.isUserInRole("ROLE_ADMIN")){
            User user = userRepository.findById(Long.valueOf(userId)).get();
            // Gjej rolin special të përdoruesit
            Role specialRole = user.getRole().stream()
                    .filter(role -> role.getId() != 1L && role.getId() != 2L && role.getId() != 3L)
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("No special role found for the user."));
            modelAndView.addObject("user", user);
            modelAndView.addObject("specialRole", specialRole);
            modelAndView.setViewName("ROLE_ADMIN/users/showUser");
        }
        return modelAndView;
    }

    @Autowired
    PropertyRepository propertyRepository;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/editUser")
    public ModelAndView editUserGet(@RequestParam(value = "id") Long id, HttpServletRequest request, ModelAndView modelAndView){
        if (request.isUserInRole("ROLE_ADMIN")){
            User user = userRepository.findById(id).get();
            // Gjej rolin special të përdoruesit
            Role specialRole = user.getRole().stream()
                    .filter(role -> role.getId() != 1L && role.getId() != 2L && role.getId() != 3L)
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("No special role found for the user."));

            // Kontrollo nëse prona ka rolin special të përdoruesit
            Property property = propertyRepository.findById(specialRole.getProperties().get(0).getId())
                    .orElseThrow(() -> new IllegalArgumentException("Property not found with ID: " + specialRole.getProperties().get(0).getId()));
            List<String> usernames = userRepository.findAllUsernames();
            List<String> emails = userRepository.findAllEmails();

            List<User> propertyUsers = property.getRoles().get(0).getUsers();

            //kontrollon nese ka user me role admin
            boolean adminExists = propertyUsers.stream()
                    .anyMatch(u -> u.isIs_admin() && !u.getId().equals(user.getId()));

            boolean hasRoleWithId3 = user.getRole().stream()
                    .anyMatch(role -> role.getId() == 3L);
            System.out.println("User has role with ID 3: " + hasRoleWithId3);
            modelAndView.addObject("hasRoleWithId3",hasRoleWithId3);

            user.setTwoFA(user.getTwoFA() == null ? true : user.getTwoFA());
            System.out.println("Property find : " + property);
            modelAndView.addObject("property", property);
            modelAndView.addObject("emails", emails);
            modelAndView.addObject("usernames", usernames);
            modelAndView.addObject("user", user);
            modelAndView.addObject("adminExists",adminExists);
            modelAndView.setViewName("ROLE_ADMIN/users/editUser");
        }
        return modelAndView;
    }



    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_GROUP_ACCOUNT')")
    @GetMapping("/edituserr")
    public ModelAndView editUser(HttpServletRequest request ,@RequestParam(value = "id") Long id, ModelAndView modelAndView) {
        // Merr përdoruesin aktual
        User currentUser = userRepository.findByUsername(request.getUserPrincipal().getName());

        // Gjej rolin special të përdoruesit
        Role specialRole = currentUser.getRole().stream()
                .filter(role -> role.getId() != 1L && role.getId() != 2L && role.getId() != 3L)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No special role found for the user."));

        // Merr pronën e lidhur me rolin special
        Property property = specialRole.getProperties().stream().findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No property associated with the special role."));

        // Kontrollo nëse përdoruesi që po editohet i përket pronës së përdoruesit aktual
        User userToEdit = userRepository.findById(id)
                .orElse(null);
        if (userToEdit == null || userToEdit.getRole().stream().noneMatch(role -> role.getProperties().contains(property))) {
            modelAndView.setViewName("/error");
            return modelAndView;
        }
        boolean hasAccess = property.getRoles().contains(specialRole);

        boolean hasGroupAccountUser = currentUser.getRole().stream()
                .anyMatch(role -> role.getId() == 3L);
        modelAndView.addObject("hasGroupAccountUser", hasGroupAccountUser);
        modelAndView.addObject("specialRole", specialRole);
        modelAndView.addObject("currentLoggedInUser", currentUser);
        if (!hasAccess) {
            modelAndView.setViewName("/error");
            return modelAndView;
        }
        if (request.isUserInRole(specialRole.getAuthority()) || hasAccess) {
            // Merr të gjithë përdoruesit e pronës
            List<User> users = property.getRoles().stream()
                    .flatMap(role -> role.getUsers().stream())
                    .distinct()
                    .collect(Collectors.toList());

            //kontrollon nese ka user me role admin
            boolean adminExists = users.stream()
                    .anyMatch(u -> u.isIs_admin() && !u.getId().equals(userToEdit.getId()));

            userToEdit.setTwoFA(userToEdit.getTwoFA() == null ? true : userToEdit.getTwoFA());

            List<String> emails = userRepository.findAllEmails();
            List<String> usernames = userRepository.findAllUsernames();

            boolean hasRoleWithId3 = userToEdit.getRole().stream()
                    .anyMatch(role -> role.getId() == 3L);
            System.out.println("User has role with ID 3: " + hasRoleWithId3);
            modelAndView.addObject("hasRoleWithId3",hasRoleWithId3);

            modelAndView.addObject("emails", emails);
            modelAndView.addObject("usernames", usernames);
            modelAndView.addObject("user", userToEdit);
            modelAndView.addObject("property", property);
            modelAndView.addObject("adminExists", adminExists);
            modelAndView.addObject("isCurrentUserAdmin", currentUser.isIs_admin());
            modelAndView.addObject("users", users);
            modelAndView.setViewName("ROLE_USER/User/editUser");
        }

        return modelAndView;
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/editUserPassword")
    public ResponseEntity<String> editUserPassword(@RequestParam(value = "id") Long id, @RequestBody PasswordDto passwordDto, ModelAndView modelAndView, HttpServletRequest request){
        if (request.isUserInRole("ROLE_ADMIN")){

            //If pass is not valid
            if (!passwordService.isNewPasswordValid(passwordDto.getNewPassword())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password is not valid.");
            }

            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            User user = userRepository.findById(id).get();

            //If pass is used before
            if (user.isPasswordUsedBefore(passwordDto.getNewPassword())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This password is used before, try using a new one.");
            }
            String passIri = bCryptPasswordEncoder.encode(passwordDto.getNewPassword());
            user.setPassword(passIri);
            userRepository.save(user);

            if (user.getOldPasswords().size()>=4){
                oldPasswordRepository.delete(user.getOldPasswords().get(0));
            }
            OldPassword oldPassword = new OldPassword(passIri, new Date(), user);
            oldPasswordRepository.save(oldPassword);

            return ResponseEntity.ok().body("Success");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This password is used before, try using a new one.");
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_GROUP_ACCOUNT')")
    @PostMapping(value = "/edituserPassword")
    public ResponseEntity<String> edituserPassword(@RequestParam(value = "id") Long id, @RequestBody PasswordDto passwordDto,ModelAndView modelAndView, HttpServletRequest request){
        if (request.isUserInRole("ROLE_USER")){

            //If pass is not valid
            if (!passwordService.isNewPasswordValid(passwordDto.getNewPassword())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password is not valid.");
            }

            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            User user = userRepository.findById(id).get();

            //If pass is used before
            if (user.isPasswordUsedBefore(passwordDto.getNewPassword())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This password is used before, try using a new one.");
            }

            String passIri = bCryptPasswordEncoder.encode(passwordDto.getNewPassword());
            user.setPassword(passIri);
            userRepository.save(user);

            if (user.getOldPasswords().size()>=4){
                oldPasswordRepository.delete(user.getOldPasswords().get(0));
            }
            OldPassword oldPassword = new OldPassword(passIri, new Date(), user);
            oldPasswordRepository.save(oldPassword);

            return ResponseEntity.ok().body("Success");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This password is used before, try using a new one.");
    }

    @Autowired
    AddressRepostitory addressRepostitory;
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/editUser")
    public ModelAndView editUser(@RequestParam(value = "id") Long id, HttpServletRequest request, ModelAndView modelAndView, User user){
        if (request.isUserInRole("ROLE_ADMIN")){
            long startTime = System.currentTimeMillis();
            User dbUser = userRepository.findById(user.getId()).get();
            dbUser.setUsername(user.getUsername());
            dbUser.setFull_name(user.getFull_name());
            dbUser.setEmail(user.getEmail());
            dbUser.setIs_admin(user.isIs_admin());
            dbUser.setTwoFA(user.getTwoFA());
            userRepository.saveAndFlush(dbUser);

            // Gjej rolin special të përdoruesit
            Role specialRole = dbUser.getRole().stream()
                    .filter(role -> role.getId() != 1L && role.getId() != 2L && role.getId() != 3L)
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("No special role found for the user."));

            // Kontrollo nëse prona ka rolin special të përdoruesit
            Property property = propertyRepository.findById(specialRole.getProperties().get(0).getId())
                    .orElseThrow(() -> new IllegalArgumentException("Property not found with ID: " + specialRole.getProperties().get(0).getId()));

            modelAndView.addObject("property", property);
            //  Lista e userave te nje prone
            List<User> users = property.getRoles().stream()
                    .flatMap(role -> role.getUsers().stream())
                    .distinct()
                    .collect(Collectors.toList());
            modelAndView.addObject("users", users);
            modelAndView.addObject("newUser", new NewUser());
            modelAndView.setViewName("redirect:/addnewuser?propertyId=" + property.getId());

            long endTime = System.currentTimeMillis();
            double durationInSeconds = (endTime - startTime) / 1000.0; // konvertohet në sekonda
            System.out.println("Koha e ekzekutimit: " + durationInSeconds + " sekonda");
        }
        return modelAndView;
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_GROUP_ACCOUNT')")
    @PostMapping("/edituser")
    public ModelAndView edituser(@RequestParam(value = "id") Long id, HttpServletRequest request, ModelAndView modelAndView, User user){

        // Merr përdoruesin aktual
        User currentLoggedInUser = userRepository.findByUsername(request.getUserPrincipal().getName());

        // Gjej rolin special të përdoruesit
        Role specialRole = currentLoggedInUser.getRole().stream()
                .filter(role -> role.getId() != 1L && role.getId() != 2L && role.getId() != 3L)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No special role found for the user."));
        Long propertyId = specialRole.getProperties().get(0).getId();
        // Kontrollo nëse prona ka rolin special të përdoruesit
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new IllegalArgumentException("Property not found with ID: " + propertyId));

        boolean hasAccess = property.getRoles().contains(specialRole);

        boolean hasGroupAccountUser = currentLoggedInUser.getRole().stream()
                .anyMatch(role -> role.getId() == 3L);
        modelAndView.addObject("hasGroupAccountUser", hasGroupAccountUser);
        modelAndView.addObject("specialRole", specialRole);
        modelAndView.addObject("currentLoggedInUser", currentLoggedInUser);
        if (!hasAccess) {
            modelAndView.setViewName("/error");
            return modelAndView;
        }
        if (request.isUserInRole(specialRole.getAuthority()) || hasAccess) {
            User dbUser = userRepository.findById(user.getId()).get();
            dbUser.setUsername(user.getUsername());
            dbUser.setFull_name(user.getFull_name());
            dbUser.setEmail(user.getEmail());
            dbUser.setIs_admin(user.isIs_admin());
            dbUser.setTwoFA(user.getTwoFA());
            userRepository.saveAndFlush(dbUser);

            // Redirect to the addnewuserr GET mapping
            modelAndView.setViewName("redirect:/addnewuserr?propertyId=" + propertyId);
        }
        return modelAndView;
    }

    @DeleteMapping("/delete_user/{userId}")
    @ResponseBody
    public ResponseEntity<?> handleDeleteUser(@PathVariable Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        Map<String,String> map = new HashMap<>();
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.getRole().clear();
            userRepository.delete(user);
            map.put("message", "User deleted successfully");
            return ResponseEntity.ok(map);
        }

        map.put("message", "User not found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
    }


    @Autowired
    JavaMailSender sender;

    @Autowired
    UserApiTokenRepository userApiTokenRepository;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/changePasswordStatus")
    @ResponseBody
    public ResponseEntity<?>  passwordExpired( @Param(value = "id") Long id) throws MessagingException {
       User user = userRepository.findById(id).get();
        if (!user.getPassword_expired()){
            user.setPassword_expired(true);
            userRepository.saveAndFlush(user);
            javaMailService.forgotPassEmail(user.getId());
        }else{
            user.setPassword_expired(false);
            userRepository.saveAndFlush(user);
        }

        return ResponseEntity.accepted().build();

    }


    private String constructResetTokenEmail(
            String token, User user) {
        String url = "http://panel.allbookers.com/user/changePassword?token=" + token;
        return url;
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/accountLockedStatus")
    @ResponseBody
    public ResponseEntity<?> accountLockedStatus(@RequestParam("id") Long id) {
        try {
            Optional<User> optionalUser = userRepository.findById(id);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                user.setAccount_locked(!user.getAccount_locked());
                userRepository.saveAndFlush(user);
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }


    @GetMapping("/register")
    public ModelAndView registerUserPage(ModelAndView modelAndView) {
        modelAndView.setViewName("/loginandregister/register");
        return modelAndView;
    }

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<String> registerUser(@RequestBody()NewUserDto newUserDto) {
        try {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

            List<Role> roleList = new ArrayList<>();
            Role role = roleRepository.findById(1L).get();

            String roleName = "ROLE_ADMIN_HOTEL_" + newUserDto.getUsername();
            int roles = roleRepository.findAllByAuthority(roleName).size();
            roles = roles + 1;
            roleName = (roles > 0 ? roleName + "_" + roles + numberService.generateThreedigitNumber() : roleName + numberService.generateThreedigitNumber());
            Role special = new Role(roleName);
            roleList.add(special);
            roleRepository.saveAndFlush(role);

            User user = new User(
                    newUserDto.getFullname(),
                    newUserDto.getEmail(),
                    newUserDto.getUsername(),
                    false,
                    false,
                    encoder.encode(newUserDto.getPassword()),
                    false,
                    roleList,
                    false,
                    true,
                    false
            );
            userRepository.save(user);

            return ResponseEntity.ok().body("Success!");
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error!");
        }

    }

    @PostMapping("checkAvailability")
    public ResponseEntity<Boolean> checkAvailability(@RequestParam(name = "username", required = false) String username, @RequestParam(name = "email", required = false) String email) {
        try{
            if(username != null){
                System.out.println("Username checked: " + username);
                return ResponseEntity.ok().body(!userRepository.existsByUsername(username));
            }else {
                System.out.println("Email checked: " + email);
                return ResponseEntity.ok().body(!userRepository.existsByEmail(email));
            }


        } catch (Exception e) {
            return ResponseEntity.badRequest().body(true);
        }

    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/confirm-your-email")
    public String emailConfirmationRequest() {
        return "loginandregister/emailConfirmationRequest";
    }

    @GetMapping("/emailConfirmation")
    public ResponseEntity<?> emailConfirmation(@RequestParam(name = "userId")Long userId) {
        try {
            javaMailService.emailConfirmation(userId);
            return ResponseEntity.ok().body("Confirmation email sent successfully.");
        }catch (Exception e){
            return ResponseEntity.internalServerError().body("Something went wrong please contact support.");
        }

    }


}

