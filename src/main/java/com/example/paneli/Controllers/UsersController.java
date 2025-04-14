package com.example.paneli.Controllers;

import com.example.paneli.DataObjects.Admin.UserProjection;
import com.example.paneli.DataObjects.NewUser;
import com.example.paneli.DataObjects.PasswordDto;
import com.example.paneli.Models.*;
import com.example.paneli.Models.PanelUsers.Role;
import com.example.paneli.Models.PanelUsers.User;
import com.example.paneli.Repositories.*;
import com.example.paneli.Repositories.UserPanel.UserRepository;
import com.example.paneli.Services.PasswordService;
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
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.getRole().clear();
            userRepository.delete(user);
            return ResponseEntity.ok(Map.of("message", "User deleted successfully"));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "User not found"));
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
        if (user.getPassword_expired()==0){
            user.setPassword_expired(1);
            userRepository.saveAndFlush(user);

            String token = UUID.randomUUID().toString();
            UserApiToken userApiToken = new UserApiToken(token, user.getUsername());
            userApiTokenRepository.save(userApiToken);

            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(user.getEmail());
            helper.setSubject("Request to reset your password");
            String htmlContent = "<div style=\"background:  #DBDBDB; display: flex;box-shadow: 0px 0px 20px rgba(0, 0, 0, 0.1);padding: 20px; margin: 0 auto; max-width: 90%; flex-direction: column; align-items: center;\">"
                    + "<div style=\"margin-left: 27%; margin-top: 10px;\">"
                    + "<img src=\"https://join.allbookers.com/images/Allbookers.png\" style=\"width: 248px; margin-bottom: 5px;\">"
                    + "</div>"
                    + "<div style=\"margin-left: 30%; margin-top: 10px; float: right;\">"
                    + "</div>"
                    + "</div><div style=\"margin-left: 32%;\">"
                    + "<h3>Your password have been expired . \n</h3>"
                    + "<p>Simply click on the button below to choose a new one.</p>"
                    + "<a style=\"text-align: center;font-size: 20px;\" href='" + constructResetTokenEmail(token, user) + "'><button style=\"padding: 10px 50px; border-radius: 5px; border: 1px solid cornflowerblue; color: #417eeb; font-weight: 600; background-color: #e7effd;\" class=\"backlogin\" type=\"button\">Reset password</button></a>"
                    + "</div><br><hr style=\"width: 35%; margin-left: auto; margin-right: auto;\"><br>"
                    + "<p style=\"text-align: center;\">© Copyright 2024 Allbookers.com | All rights reserved."
                    + "<br>This e-mail was sent by allbookers.com.</p>";

            helper.setText(htmlContent, true);
            sender.send(message);

        }else if (user.getPassword_expired()==1){
            user.setPassword_expired(0);
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
                user.setAccount_locked(user.getAccount_locked() == 1 ? 0 : 1);
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


}

