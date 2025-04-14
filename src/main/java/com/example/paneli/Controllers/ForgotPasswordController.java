package com.example.paneli.Controllers;
import com.example.paneli.Models.OldPassword;
import com.example.paneli.Models.PanelUsers.Role;
import com.example.paneli.Models.PanelUsers.User;
import com.example.paneli.Models.UserApiToken;
import com.example.paneli.Repositories.OldPasswordRepository;
import com.example.paneli.Repositories.PropertyRepository;
import com.example.paneli.Repositories.UserApiTokenRepository;
import com.example.paneli.Repositories.UserPanel.UserRepository;
import com.example.paneli.Services.PasswordService;
import com.example.paneli.Services.PropertyService;
import com.example.paneli.DataObjects.PasswordDto;
import com.example.paneli.Services.TokenDeletionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.UUID;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;

@Controller
public class ForgotPasswordController {
    PasswordDto objectPasswordDto = new PasswordDto();

    @GetMapping(value = "/forgotPassword")
    public ModelAndView forgotPassword(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
            return new ModelAndView("redirect:/login");
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("loginandregister/forgotPassword");
        return modelAndView;
    }
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserApiTokenRepository userApiTokenRepository;
    @Autowired
    PropertyService propertyService;
    @Autowired
    JavaMailSender sender;
    @Autowired
    PropertyRepository propertyRepository;
    @Autowired
    PasswordService passwordService;
    @Autowired
    OldPasswordRepository oldPasswordRepository;
    @Autowired
    private TokenDeletionService tokenDeletionService;

    @PostMapping("/user/resetPassword")
    @ResponseBody
    public ModelAndView resetPassword(ModelAndView modelAndView, @RequestParam("privatData") String data) throws MessagingException {
        User user = null;

//            // kontrolli nëse privatData është një numër
//            if (data.matches("-?\\d+")) {
//                Long propertyId = Long.parseLong(data) - 2654435L;
//                Optional<Property> propertyOptional = propertyRepository.findById(propertyId);
//                if (propertyOptional.isPresent()) {
//                    Property property = propertyOptional.get();
//                    if (!property.getRole().getUsers().isEmpty()) {
//                        modelAndView.addObject("user1", 1);
//                        user = property.getRole().getUsers().get(0);
//                        System.out.println(user.getRole().get(1).getAuthority());
//                        System.out.println(user.getRole().get(0).getAuthority());
//
//                        String token = UUID.randomUUID().toString();
//                        UserApiToken userApiToken = new UserApiToken(token, user.getUsername());
//                        userApiTokenRepository.save(userApiToken);
//
//                        MimeMessage message = sender.createMimeMessage();
//                        MimeMessageHelper helper = new MimeMessageHelper(message, true);
//                        helper.setTo(user.getEmail());
//                        helper.setSubject("Request to reset your password");
//                        String htmlContent = "<div style=\"background: #fb8402; display: flex;\">"
//                                + "<div style=\"margin-left: 27%; margin-top: 10px;\">"
//                                + "<img src=\"https://allbookers.com/images/jffj.png\" style=\"width: 280px; margin-bottom: 5px;\">"
//                                + "</div>"
//                                + "<div style=\"margin-left: 30%; margin-top: 10px; float: right;\">"
//                                + "<img src=\"https://join.allbookers.com/images/logoemail.png\" style=\"width: 30px\">"
//                                + "</div>"
//                                + "</div><div style=\"margin-left: 32%;\">"
//                                + "<h3>Forgot your password? \n</h3>"
//                                + "<p>Simply click on the button below to choose a new one.</p>"
//                                + "<a style=\"text-align: center;font-size: 20px;\" href='" + constructResetTokenEmail(token, user) + "'><button style=\"padding: 10px 50px; border-radius: 5px; border: 1px solid cornflowerblue; color: #417eeb; font-weight: 600; background-color: #e7effd;\" class=\"backlogin\" type=\"button\">Reset password</button></a>"
//                                + "</div><br><hr style=\"width: 35%; margin-left: auto; margin-right: auto;\"><br>"
//                                + "<p style=\"text-align: center;\">© Copyright 2024 Allbookers.com | All rights reserved."
//                                + "<br>This e-mail was sent by allbookers.com.</p>";
//
//                        helper.setText(htmlContent, true);
//                        sender.send(message);
//                        String email = user.getEmail();
//                        modelAndView.addObject("email", email);
//                    } else {
//                    modelAndView.addObject("usererror", 10);
//                    modelAndView.addObject("messageerror", "Please use your property ID, email or username to change your password. The data you have set does not exist.");
//                    }
//                } else {
//                    modelAndView.addObject("usererror", 10);
//                    modelAndView.addObject("messageerror", "Please use your property ID, email or username to change your password. The data you have set does not exist.");
//                }
//            }

            // kontrolli nëse privatData është një email
            if (data.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}")) {
                List<User> users = userRepository.findAllByEmail(data);
                System.out.println("users : " + users);
                if (users.size() == 1) {
                    modelAndView.addObject("user1", 1);
                    user = userRepository.findByEmail(data);
//                    System.out.println(user.getRole().get(1).getAuthority());
//                    System.out.println(user.getRole().get(0).getAuthority());

                    String token = UUID.randomUUID().toString();
                    UserApiToken userApiToken = new UserApiToken(token, user.getUsername());
                    userApiTokenRepository.save(userApiToken);
                    //Delete after 10 minutes
                    tokenDeletionService.deleteTokenAfterDelay(userApiTokenRepository, userApiToken);

                    MimeMessage message = sender.createMimeMessage();
                    MimeMessageHelper helper = new MimeMessageHelper(message, true);
                    helper.setTo(user.getEmail());
                    helper.setSubject("Request to reset your password");
                    String htmlContent = "<div style=\"font-family: system-ui;\">" +
                            "<div style=\"background: #dbdbdb; display: flex;\">" +
                            "<div style=\"margin: 15px auto; width: 248px;\">" +
                            "<img src=\"https://allbookers.com/images/logoallbookers.png\" style=\"width: 100%; margin-bottom: 5px; display: block;\">" +
                            "</div>" +
                            "<div style=\"width: 7%; margin-top: 1%; color: black;\">" +
                            "</div>" +
                            "</div>" +
                            "<div style=\"text-align: center; width: 75%; margin: 1% 10%;\">" +
                            "<div style=\"width: 60%; margin: 0 auto; font-size: 16px; color: black; font-family: BlinkMacSystemFont,-apple-system,Segoe UI,Roboto,Helvetica,Arial,sans-serif;\">" +
                            "<h3>Forgot your password?</h3>" +
                            "<p>Simply click on the button below to choose a new one.</p>" +
                            "<a style=\"text-align: center; font-size: 20px;\" href='" + constructResetTokenEmail(token, user) + "'>" +
                            "<button style=\"padding: 10px 50px; border-radius: 5px; border: 1px solid cornflowerblue; color: #417eeb; font-weight: 600; background-color: #e7effd;\" class=\"backlogin\" type=\"button\">Reset password</button>" +
                            "</a>" +
                            "</div>" +
                            "</div>" +
                            "<br><hr style=\"width: 35%; margin-left: auto; margin-right: auto;\"><br>" +
                            "<p style=\"text-align: center;\">© Copyright 2024 Allbookers.com | All rights reserved.<br>This e-mail was sent by allbookers.com.</p>" +
                            "</div>";

                    helper.setText(htmlContent, true);
                    sender.send(message);
                    String email = user.getEmail();
                    modelAndView.addObject("email", email);

                } else if (users.size() > 1) {
                    modelAndView.addObject("user2", 2);
                    modelAndView.addObject("message", "Please use username to change the password, because this email is used in more than one property.");
                } else {
                    modelAndView.addObject("message1", "This email does not exist. Please use username to change the password.");
                    modelAndView.addObject("user", 0);
                }
            }
            // kontrolli nëse privatData është një username
            else if(data.matches("[a-zA-Z0-9_-]{3,16}")) {
                    user = userRepository.findByUsername(data);
                if(user != null) {
                    modelAndView.addObject("user1", 1);
//                    System.out.println(user.getRole().get(1).getAuthority());
//                    System.out.println(user.getRole().get(0).getAuthority());

                    String token = UUID.randomUUID().toString();
                    UserApiToken userApiToken = new UserApiToken(token, user.getUsername());
                    userApiTokenRepository.save(userApiToken);

                    //Delete after 10 minutes
                    tokenDeletionService.deleteTokenAfterDelay(userApiTokenRepository, userApiToken);

                    MimeMessage message = sender.createMimeMessage();
                    MimeMessageHelper helper = new MimeMessageHelper(message, true);
                    helper.setTo(user.getEmail());
                    helper.setSubject("Request to reset your password");
                    String htmlContent = "<div style=\"font-family: system-ui;\">" +
                            "<div style=\"background: #dbdbdb; display: flex;\">" +
                            "<div style=\"margin: 15px auto; width: 248px;\">" +
                            "<img src=\"https://allbookers.com/images/logoallbookers.png\" style=\"width: 100%; margin-bottom: 5px; display: block;\">" +
                            "</div>" +
                            "<div style=\"width: 7%; margin-top: 1%; color: black;\">" +
                            "</div>" +
                            "</div>" +
                            "<div style=\"text-align: center; width: 75%; margin: 1% 10%;\">" +
                            "<div style=\"width: 60%; margin: 0 auto; font-size: 16px; color: black; font-family: BlinkMacSystemFont,-apple-system,Segoe UI,Roboto,Helvetica,Arial,sans-serif;\">" +
                            "<h3>Forgot your password?</h3>" +
                            "<p>Simply click on the button below to choose a new one.</p>" +
                            "<a style=\"text-align: center; font-size: 20px;\" href='" + constructResetTokenEmail(token, user) + "'>" +
                            "<button style=\"padding: 10px 50px; border-radius: 5px; border: 1px solid cornflowerblue; color: #417eeb; font-weight: 600; background-color: #e7effd;\" class=\"backlogin\" type=\"button\">Reset password</button>" +
                            "</a>" +
                            "</div>" +
                            "</div>" +
                            "<br><hr style=\"width: 35%; margin-left: auto; margin-right: auto;\"><br>" +
                            "<p style=\"text-align: center;\">© Copyright 2024 Allbookers.com | All rights reserved.<br>This e-mail was sent by allbookers.com.</p>" +
                            "</div>";

                    helper.setText(htmlContent, true);
                    sender.send(message);
                    String email = user.getEmail();
                    modelAndView.addObject("email", email);
                } else {
                    modelAndView.addObject("usererror", 10);
                    modelAndView.addObject("messageerror", "Please use email or username to change your password. The data you have set does not exist.");
                }
            } else {
                modelAndView.addObject("usererror", 10);
                modelAndView.addObject("messageerror", "Please use email or username to change your password. The data you have set does not exist.");
            }

        modelAndView.setViewName("loginandregister/forgotpassemail");
        return modelAndView;
    }
    private String constructResetTokenEmail(
             String token, User user) {
        String url = "http://panel.allbookers.com/user/changePassword?token=" + token;
        return url;
    }
    @PostMapping("/user/resetPasswordLink")
    @ResponseBody
    public ModelAndView resetPasswordLink(ModelAndView modelAndView, @RequestParam("privatData") String data) throws MessagingException {
        User user = null;
            user = userRepository.findByUsername(data);
            // Gjej rolin special të përdoruesit
            Role specialRole = user.getRole().stream()
                    .filter(role -> role.getId() != 1L && role.getId() != 2L && role.getId() != 3L)
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("No special role found for the user."));
            if(user != null) {
                System.out.println(user.getRole().get(1).getAuthority());
                System.out.println(user.getRole().get(0).getAuthority());

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
                        + "<h3>Forgot your password? \n</h3>"
                        + "<p>Simply click on the button below to choose a new one.</p>"
                        + "<a style=\"text-align: center;font-size: 20px;\" href='" + constructResetTokenEmail(token, user) + "'><button style=\"padding: 10px 50px; border-radius: 5px; border: 1px solid cornflowerblue; color: #417eeb; font-weight: 600; background-color: #e7effd;\" class=\"backlogin\" type=\"button\">Reset password</button></a>"
                        + "</div><br><hr style=\"width: 35%; margin-left: auto; margin-right: auto;\"><br>"
                        + "<p style=\"text-align: center;\">© Copyright 2024 Allbookers.com | All rights reserved."
                        + "<br>This e-mail was sent by allbookers.com.</p>";

                helper.setText(htmlContent, true);
                sender.send(message);
                String email = user.getEmail();
                modelAndView.setViewName("redirect:/editUser?id=" + user.getId());
            }
        modelAndView.addObject("user", user);
        modelAndView.addObject("property", specialRole.getProperties().get(0));
        return modelAndView;
    }




    @GetMapping(value = "/user/changePassword")
    public ModelAndView changeUserPassword(ModelAndView modelAndView, @RequestParam(value = "token", required = false) String token) {
        if (token == null) {
            modelAndView.addObject("expired", true);
            modelAndView.setViewName("loginandregister/setNewPassword");
            return modelAndView;
        }
        UserApiToken userApiToken = userApiTokenRepository.findByTokenValue(token);

        if (userApiToken == null || userApiToken.isExpired()) {
            modelAndView.addObject("expired", true);
        } else {
            modelAndView.addObject("token", token);
            modelAndView.addObject("expired", false);
            User user = userRepository.findByUsername(userApiToken.getUsername());
            modelAndView.addObject("user", user);
        }

        modelAndView.setViewName("loginandregister/setNewPassword");
        return modelAndView;
    }

//    @GetMapping(value = "/checkemailforforgotPassword")
//    public ModelAndView forgetpassemail(ModelAndView modelAndView){
//        modelAndView.setViewName("loginandregister/forgotpassemail");
//        return modelAndView;
//    }

    @PostMapping(value = "/confirmNewPassword")
    public ResponseEntity<String> confirmNewPassword( HttpServletRequest request,@RequestBody PasswordDto passwordDto ,ModelAndView modelAndView){
    // Validate new password
    if (!passwordService.isNewPasswordValid(passwordDto.getNewPassword())){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password is not valid.");
    }

    UserApiToken userApiToken = userApiTokenRepository.findByTokenValue(passwordDto.getToken());
    if (userApiToken == null || userApiToken.isExpired()) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This link expired.");
    }

    User user = userRepository.findByUsername(userApiToken.getUsername());

    // Check if the new password has been used before
    if (user.isPasswordUsedBefore(passwordDto.getNewPassword())){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This password is used before, try using a new one.");
    }

    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    String newPass = bCryptPasswordEncoder.encode(passwordDto.getNewPassword());
    user.setPassword(newPass);
    userRepository.save(user);

    // Manage old passwords
    if (user.getOldPasswords().size()>=4){
        oldPasswordRepository.delete(user.getOldPasswords().get(0));
    }
    OldPassword oldPassword = new OldPassword(newPass, new Date(), user);
    oldPasswordRepository.save(oldPassword);

    // Mark the token as expired
    userApiToken.setExpired(true);
    userApiTokenRepository.save(userApiToken);

        return ResponseEntity.ok().body("Success");
    }



    private boolean isTokenValid(String token){
        final UserApiToken userApiToken = userApiTokenRepository.findByTokenValue(token);
        return isTokenFound(userApiToken);
    }

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    public void changeUserPassword(User user, String password){
        String newPass = bCryptPasswordEncoder.encode(password);
        user.setPassword(newPass);
        // Set status, first password is changed
        user.setNew(false);
        userRepository.save(user);

        OldPassword oldPassword = new OldPassword(newPass, new Date(), user);
        oldPasswordRepository.save(oldPassword);
    }

    private boolean isTokenFound(UserApiToken token){
        return token!=null;
    }




}
