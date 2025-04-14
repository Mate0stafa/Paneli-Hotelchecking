package com.example.paneli.Controllers;

import com.example.paneli.DataObjects.Admin.UniqueKey;
import com.example.paneli.DataObjects.NewUser;
import com.example.paneli.DataObjects.NewUserDto;
import com.example.paneli.Models.PanelUsers.Role;
import com.example.paneli.Models.PanelUsers.User;
import com.example.paneli.Models.Property;
import com.example.paneli.Repositories.AddressRepostitory;
import com.example.paneli.Repositories.PropertyRepository;
import com.example.paneli.Repositories.UserPanel.RoleRepository;
import com.example.paneli.Repositories.UserPanel.UserRepository;
import com.example.paneli.Services.Mail.JavaMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Controller
public class NewUserController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PropertyRepository propertyRepository;
    @Autowired
    AddressRepostitory addressRepostitory;


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("addnewuser")
    public ModelAndView registerNewUserr(HttpServletRequest request, @RequestParam(value = "propertyId") Long propertyId) {
        ModelAndView modelAndView = new ModelAndView();
        if (request.isUserInRole("ROLE_ADMIN")) {
            Property property = propertyRepository.findById(propertyId).get();
            //  Lista e userave te nje prone
            List<User> users = property.getRoles().stream()
                    .flatMap(role -> role.getUsers().stream())
                    .distinct()
                    .collect(Collectors.toList());
            System.out.println(property + "PPR");
            modelAndView.addObject("property", property);
            modelAndView.addObject("users", users);
            modelAndView.setViewName("ROLE_ADMIN/users/propertyUser");
        }
        return modelAndView;
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_GROUP_ACCOUNT')")
    @GetMapping("addnewuserr")
    public ModelAndView     registerNewUserrUser(HttpServletRequest request ,@RequestParam(value = "propertyId") Long propertyId){
        ModelAndView modelAndView = new ModelAndView();
        // Merr përdoruesin aktual
        User currentLoggedInUser = userRepository.findByUsername(request.getUserPrincipal().getName());

        // Gjej rolin special të përdoruesit
        Role specialRole = currentLoggedInUser.getRole().stream()
                .filter(role -> role.getId() != 1L && role.getId() != 2L && role.getId() != 3L)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No special role found for the user."));

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
            User useractual = userRepository.findByUsername(request.getUserPrincipal().getName());
            //  Lista e userave te nje prone
            List<User> users = property.getRoles().stream()
                    .flatMap(role -> role.getUsers().stream())
                    .distinct()
                    .collect(Collectors.toList());

            modelAndView.addObject("useractual", useractual );
            modelAndView.addObject("property", property);
            modelAndView.addObject("users", users);
            modelAndView.setViewName("ROLE_USER/Property/propertyUser");
        }
        return modelAndView;
    }


    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("admin/addnewuser")
    public ModelAndView registerNewUser(@RequestParam(value = "propertyId") Long propertyId,HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();
        if (request.isUserInRole("ROLE_ADMIN")){
            Property property = propertyRepository.findById(propertyId).get();
            //  Lista e userave te nje prone
            List<User> users = property.getRoles().stream()
                    .flatMap(role -> role.getUsers().stream())
                    .distinct()
                    .collect(Collectors.toList());

            //Krijojme nje list e cila permban emails
            List<String> emails = userRepository.findAllEmails();
            modelAndView.addObject("emails", emails);
            //Krijojme nje list e cila permban usernames
            List<String> usernames = userRepository.findAllUsernames();
            modelAndView.addObject("usernames", usernames);

            modelAndView.addObject("users", users);
            modelAndView.addObject("property", property);
            modelAndView.addObject("newUser", new NewUser());
            modelAndView.setViewName("ROLE_ADMIN/users/createUser");
        }
        return modelAndView;
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_GROUP_ACCOUNT')")
    @GetMapping("user/addnewuser")
    public ModelAndView registerNewUserfromUser(@RequestParam(value = "propertyId") Long propertyId,HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();
        // Merr përdoruesin aktual
        User currentLoggedInUser = userRepository.findByUsername(request.getUserPrincipal().getName());

        // Gjej rolin special të përdoruesit
        Role specialRole = currentLoggedInUser.getRole().stream()
                .filter(role -> role.getId() != 1L && role.getId() != 2L && role.getId() != 3L)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No special role found for the user."));

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
            //  Lista e userave te nje prone
            List<User> users = property.getRoles().stream()
                    .flatMap(role -> role.getUsers().stream())
                    .distinct()
                    .collect(Collectors.toList());

            //Krijojme nje list e cila permban emails
            List<String> emails = userRepository.findAllEmails();
            modelAndView.addObject("emails", emails);
            //Krijojme nje list e cila permban usernames
            List<String> usernames = userRepository.findAllUsernames();
            modelAndView.addObject("usernames", usernames);

            modelAndView.addObject("users", users);
            modelAndView.addObject("property", property);
            modelAndView.addObject("newUser", new NewUser());
            modelAndView.setViewName("ROLE_USER/User/createUser");
        }
        return modelAndView;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/verifyCode")
    public ModelAndView verifyCode(@RequestParam(value = "propertyId") Long propertyId, HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();
        if (request.isUserInRole("ROLE_ADMIN")){
            Property property = propertyRepository.findById(propertyId).get();
            //  Lista e userave te nje prone
            List<User> users = property.getRoles().stream()
                    .flatMap(role -> role.getUsers().stream())
                    .distinct()
                    .collect(Collectors.toList());
            modelAndView.addObject("users", users);
            modelAndView.addObject("property", property);
            modelAndView.addObject("newUser", new NewUser());
            modelAndView.addObject("unique", new UniqueKey());
            modelAndView.setViewName("ROLE_ADMIN/users/verifyCodeUser");
        }
        return modelAndView;
    }


    @Autowired
    JavaMailService javaMailService;
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/verifyCode")
    public String verifyCodePost(@RequestParam(value = "propertyId") Long propertyId, UniqueKey uniqueKey, HttpServletRequest request) throws MessagingException {
        if (request.isUserInRole("ROLE_ADMIN")) {
            Property property = propertyRepository.findById(propertyId).get();
            User newUser = (User) request.getSession().getAttribute("user");

            if (request.getSession().getAttribute("securityCode").equals(uniqueKey.getKey())) {
                Role role = property.getRoles().get(0);
                List<Role> roleList = new ArrayList<>();
                Role role_user = roleRepository.findById(1L).get();
                roleList.add(role);
                roleList.add(role_user);

                User newUser2 = new User(0,
                        property.getFirstName() + " " + property.getLastName(),
                        newUser.getEmail(),
                        newUser.getUsername(),
                        0, 0, newUser.getPassword(), 0, 0,
                        roleList, false, true, true);

                userRepository.save(newUser2);
                javaMailService.sendNewUserCreatedData(newUser2, (String) request.getSession().getAttribute("firstPassword"));
                request.getSession().removeAttribute("firstPassword");
                request.getSession().setAttribute("verificationResult", true);
                return "redirect:/verificationResult?propertyId=" + propertyId;
            } else {
                request.getSession().setAttribute("verificationResult", false);
                return "redirect:/verificationResult?propertyId=" + propertyId;
            }
        }

        return "redirect:/error";
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/verificationResult")
    public ModelAndView verificationResult(@RequestParam(value = "propertyId") Long propertyId, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        if (request.isUserInRole("ROLE_ADMIN")) {
            Property property = propertyRepository.findById(propertyId).get();
            modelAndView.addObject("property", property);

            User newUser = (User) request.getSession().getAttribute("user");
            modelAndView.addObject("email", newUser.getEmail());
            modelAndView.addObject("newUser", new NewUser());

            Boolean verificationResult = (Boolean) request.getSession().getAttribute("verificationResult");
            modelAndView.addObject("vlera", verificationResult);

            if (verificationResult != null && verificationResult) {
                modelAndView.setViewName("ROLE_ADMIN/users/createUser1");
            } else {
                modelAndView.setViewName("ROLE_ADMIN/users/verifyCodeUser");
            }

        } else {
            modelAndView.setViewName("error");
        }

        return modelAndView;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "admin/addnewuser")
    public String addNewUsers(HttpServletRequest request, @RequestParam(value = "propertyId") Long propertyId, NewUserDto newUserDto, BindingResult bindingResult) throws MessagingException {
        if (request.isUserInRole("ROLE_ADMIN")) {
            Property property = propertyRepository.findById(propertyId).get();
            Role role = property.getRoles().get(0);
            List<Role> roleList = new ArrayList<>();
            Role role_user = roleRepository.findById(1L).get();
            roleList.add(role);
            roleList.add(role_user);

            String encodedPassword = bCryptPasswordEncoder.encode(newUserDto.getPassword());

            User newUser = new User(0,
                    property.getFirstName() + " " + property.getLastName(),
                    newUserDto.getEmail(),
                    newUserDto.getUsername(),
                    0, 0, encodedPassword, 0, 0,
                    roleList, false, true, true);

            Random rnd = new Random();
            int number = rnd.nextInt(900000) + 100000;
            String uniqueKey = String.valueOf(number);

            request.getSession().setAttribute("securityCode", uniqueKey);
            request.getSession().setAttribute("user", newUser);
            request.getSession().setAttribute("firstPassword", newUserDto.getPassword());

            System.out.println(request.getSession().getAttribute("securityCode"));
            //Kodi i shkon userit qe eshte krijuar me emailin qe sapo vendosi

            javaMailService.sendMailPinCodeUser(newUser, uniqueKey);

            return "redirect:/admin/verifycodeAdmin?propertyId=" + propertyId;
        }

        return "redirect:/error";
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "admin/verifycodeAdmin")
    public ModelAndView verifyCodeAdmin(HttpServletRequest request, @RequestParam(value = "propertyId") Long propertyId) {
        ModelAndView modelAndView = new ModelAndView();
        if (request.isUserInRole("ROLE_ADMIN")) {
            modelAndView.addObject("newUser", new NewUser());
            Property property = propertyRepository.findById(propertyId).get();
            modelAndView.addObject("property", property);

            User newUser = (User) request.getSession().getAttribute("user");
            modelAndView.addObject("email", newUser.getEmail());

            String uniqueKey = (String) request.getSession().getAttribute("securityCode");
            modelAndView.addObject("key", uniqueKey);

            modelAndView.setViewName("ROLE_ADMIN/users/verifyCodeUser");
        } else {
            modelAndView.setViewName("error");
        }

        return modelAndView;
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_GROUP_ACCOUNT')")
    @PostMapping(value = "user/addnewuser")
    public ModelAndView addNewUsersfromuser(HttpServletRequest request,
                                            @RequestParam(value = "propertyId") Long propertyId,
                                            NewUserDto newUserDto) throws MessagingException {
        ModelAndView modelAndView = new ModelAndView();
        // Merr përdoruesin aktual
        User currentLoggedInUser = userRepository.findByUsername(request.getUserPrincipal().getName());

        // Gjej rolin special të përdoruesit
        Role specialRole = currentLoggedInUser.getRole().stream()
                .filter(role -> role.getId() != 1L && role.getId() != 2L && role.getId() != 3L)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No special role found for the user."));

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
            Role role = property.getRoles().get(0);
            List<Role> roleList = new ArrayList<>();
            Role role_user = roleRepository.findById(1L).get();
            roleList.add(role);
            roleList.add(role_user);

            String encodedPassword = bCryptPasswordEncoder.encode(newUserDto.getPassword());

            User newUser = new User(0,
                    property.getFirstName() + " " + property.getLastName(),
                    newUserDto.getEmail(),
                    newUserDto.getUsername(),
                    0,
                    0,
                    encodedPassword,
                    0,
                    0,
                    roleList,
                    false,
                    true,
                    true);

            Random rnd = new Random();
            int number = rnd.nextInt(900000) + 100000;
            String uniqueKey = String.valueOf(number);

            request.getSession().setAttribute("securityCode", uniqueKey);
            request.getSession().setAttribute("user", newUser);
            request.getSession().setAttribute("firstPassword", newUserDto.getPassword());
            request.getSession().setAttribute("propertyId", propertyId);

            System.out.println(request.getSession().getAttribute("securityCode"));
            javaMailService.sendMailPinCodeUser(newUser, uniqueKey);

            return new ModelAndView("redirect:/user/verifycode");
        }
        return modelAndView;
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_GROUP_ACCOUNT')")
    @GetMapping(value = "user/verifycode")
    public ModelAndView verifyCode(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        User newUser = (User) request.getSession().getAttribute("user");
        String uniqueKey = (String) request.getSession().getAttribute("securityCode");
        Long propertyId = (Long) request.getSession().getAttribute("propertyId");

        if (newUser != null && uniqueKey != null && propertyId != null) {
            modelAndView.addObject("email", newUser.getEmail());
            modelAndView.addObject("newUser", new NewUser());
            Property property = propertyRepository.findById(propertyId).get();
            modelAndView.addObject("property", property);
            modelAndView.addObject("key", uniqueKey);
            modelAndView.setViewName("ROLE_USER/User/verifyCodeUser");
        } else {
            modelAndView.setViewName("redirect:/error");
        }

        return modelAndView;
    }

    @GetMapping(value = "makeAllAdmin")
    @ResponseBody
    public void makeAllAdmin(){

        List<Property> properties = propertyRepository.findAll();
        for (int i=0;i<properties.stream().count();i++){
            System.out.println(properties.get(i).getId());
            if (properties.get(i).getRoles().get(0).getUsers().size()>0){
                userRepository.save(properties.get(i).getRoles().get(0).getUsers().get(0));
            }
        }
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_GROUP_ACCOUNT')")
    @GetMapping(value = "/verifyCodeuser")
    public ModelAndView verifyCodeuser(@RequestParam(value = "propertyId") Long propertyId, HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();
        // Merr përdoruesin aktual
        User currentLoggedInUser = userRepository.findByUsername(request.getUserPrincipal().getName());

        // Gjej rolin special të përdoruesit
        Role specialRole = currentLoggedInUser.getRole().stream()
                .filter(role -> role.getId() != 1L && role.getId() != 2L && role.getId() != 3L)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No special role found for the user."));

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
            //  Lista e userave te nje prone
            List<User> users = property.getRoles().stream()
                    .flatMap(role -> role.getUsers().stream())
                    .distinct()
                    .collect(Collectors.toList());
            modelAndView.addObject("users", users);
            modelAndView.addObject("property", property);
            modelAndView.addObject("newUser", new NewUser());
            modelAndView.addObject("unique", new UniqueKey());
            modelAndView.setViewName("ROLE_USER/User/verifyCodeUser");
        }
        return modelAndView;
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_GROUP_ACCOUNT')")
    @PostMapping(value = "/verifyCodeuser")
    public ModelAndView verifyCodePostuser(@RequestParam(value = "propertyId") Long propertyId,
                                           UniqueKey uniqueKey,
                                           HttpServletRequest request) throws MessagingException {
        ModelAndView modelAndView = new ModelAndView();

        boolean isVerificationSuccessful = false;
        User newUser = (User) request.getSession().getAttribute("user");

        // Merr përdoruesin aktual
        User currentLoggedInUser = userRepository.findByUsername(request.getUserPrincipal().getName());

        // Gjej rolin special të përdoruesit
        Role specialRole = currentLoggedInUser.getRole().stream()
                .filter(role -> role.getId() != 1L && role.getId() != 2L && role.getId() != 3L)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No special role found for the user."));

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

        if (request.getSession().getAttribute("securityCode").equals(uniqueKey.getKey()) || hasAccess) {
            Role role = property.getRoles().get(0);
            List<Role> roleList = new ArrayList<>();
            Role role_user = roleRepository.findById(1L).get();
            roleList.add(role);
            roleList.add(role_user);

            User newUser2 = new User(0,
                    property.getFirstName() + " " + property.getLastName(),
                    newUser.getEmail(),
                    newUser.getUsername(),
                    0,
                    0,
                    newUser.getPassword(),
                    0,
                    0,
                    roleList,
                    false,
                    true,
                    true);

            userRepository.save(newUser2);
            javaMailService.sendNewUserCreatedData(newUser2, (String) request.getSession().getAttribute("firstPassword"));
            request.getSession().removeAttribute("firstPassword");
            isVerificationSuccessful = true;
        }

        request.getSession().setAttribute("verificationResult", isVerificationSuccessful);
        request.getSession().setAttribute("propertyId", propertyId);

        return new ModelAndView("redirect:/verifyCodeuserResult");
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_GROUP_ACCOUNT')")
    @GetMapping(value = "/verifyCodeuserResult")
    public ModelAndView verifyCodeResult(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();

        Boolean verificationResult = (Boolean) request.getSession().getAttribute("verificationResult");
        Long propertyId = (Long) request.getSession().getAttribute("propertyId");


        Property property = propertyRepository.findById(propertyId).get();
        User newUser = (User) request.getSession().getAttribute("user");

        modelAndView.addObject("newUser", new NewUser());
        modelAndView.addObject("property", property);
        modelAndView.addObject("email", newUser.getEmail());
        modelAndView.addObject("vlera", verificationResult);

        if (verificationResult) {
            modelAndView.setViewName("ROLE_USER/User/createUser1");
        } else {
            modelAndView.setViewName("ROLE_USER/User/verifyCodeUser");
        }


        return modelAndView;
    }
}