package com.example.paneli.Controllers;
import com.example.paneli.Models.OldPassword;
import com.example.paneli.Models.PanelUsers.Role;
import com.example.paneli.Models.PanelUsers.User;
import com.example.paneli.Models.UserApiToken;
import com.example.paneli.Repositories.OldPasswordRepository;
import com.example.paneli.Repositories.PropertyRepository;
import com.example.paneli.Repositories.UserApiTokenRepository;
import com.example.paneli.Repositories.UserPanel.UserRepository;
import com.example.paneli.Services.Mail.JavaMailService;
import com.example.paneli.Services.PasswordService;
import com.example.paneli.Services.PropertyService;
import com.example.paneli.DataObjects.PasswordDto;
import com.example.paneli.Services.TokenDeletionService;
import com.example.paneli.Services.TokenService;
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

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserApiTokenRepository userApiTokenRepository;
    @Autowired
    private PropertyService propertyService;
    @Autowired
    private PropertyRepository propertyRepository;
    @Autowired
    private PasswordService passwordService;
    @Autowired
    private OldPasswordRepository oldPasswordRepository;
    @Autowired
    private TokenDeletionService tokenDeletionService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private JavaMailService javaMailService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


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


    @PostMapping("/user/resetPassword")
    @ResponseBody
    public ModelAndView resetPassword(ModelAndView modelAndView, @RequestParam("privatData") String data) throws MessagingException {
        User user = null;

            // kontrolli nëse privatData është një email
            if (data.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}")) {
                List<User> users = userRepository.findAllByEmail(data);
                System.out.println("users : " + users);
                if (users.size() == 1) {
                    modelAndView.addObject("user1", 1);
                    user = userRepository.findByEmail(data);
                    if(!userApiTokenRepository.existsByUserId(user.getId()) || userApiTokenRepository.findAllByUserId(user.getId()).stream().allMatch(UserApiToken::isExpired)){
                        javaMailService.forgotPassEmail(user.getId());
                    }

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

                    if (!userApiTokenRepository.existsByUserId(user.getId()) || userApiTokenRepository.findAllByUserId(user.getId()).stream().allMatch(UserApiToken::isExpired)) {
                        javaMailService.forgotPassEmail(user.getId());
                    }

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

    @PostMapping("/user/resetPasswordLink")
    @ResponseBody
    public ModelAndView resetPasswordLink(ModelAndView modelAndView, @RequestParam("privatData") String data) throws MessagingException {
        User user;
        user = userRepository.findByUsername(data);
        // Gjej rolin special të përdoruesit
        Role specialRole = user.getRole().stream()
                .filter(role -> role.getId() != 1L && role.getId() != 2L && role.getId() != 3L)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No special role found for the user."));
        if(user != null) {

            System.out.println(user.getRole().get(1).getAuthority());
            System.out.println(user.getRole().get(0).getAuthority());

            if(!userApiTokenRepository.existsByUserId(user.getId()) || userApiTokenRepository.findAllByUserId(user.getId()).stream().allMatch(UserApiToken::isExpired)){
                    javaMailService.forgotPassEmail(user.getId());
            }

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
        }else {

            if(tokenService.validateToken(token)){
                modelAndView.addObject("token", token);
                modelAndView.addObject("expired", false);
                User user = userRepository.findById(tokenService.getTokenUserID(token)).get();
                modelAndView.addObject("user", user);
            }else{
                modelAndView.addObject("expired", true);
                modelAndView.setViewName("loginandregister/setNewPassword");
                return modelAndView;
            }
        }

        modelAndView.setViewName("loginandregister/setNewPassword");
        return modelAndView;
    }


    @PostMapping(value = "/confirmNewPassword")
    public ResponseEntity<String> confirmNewPassword( HttpServletRequest request,@RequestBody PasswordDto passwordDto){
        // Validate new password
        if (!passwordService.isNewPasswordValid(passwordDto.getNewPassword())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password is not valid.");
        }

        User user = userRepository.findById(passwordDto.getUserId()).get();

        // Check if the new password has been used before
        if (user.isPasswordUsedBefore(passwordDto.getNewPassword())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This password is used before, try using a new one.");
        }

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);
        String newPass = bCryptPasswordEncoder.encode(passwordDto.getNewPassword());
        user.setPassword(newPass);
        userRepository.save(user);

        // Manage old passwords
        if (user.getOldPasswords().size()>=4){
            oldPasswordRepository.delete(user.getOldPasswords().get(0));
        }
        OldPassword oldPassword = new OldPassword(newPass, new Date(), user);
        oldPasswordRepository.save(oldPassword);

        return ResponseEntity.ok().body("Success");
    }


    public void changeUserPassword(User user, String password){
        String newPass = bCryptPasswordEncoder.encode(password);
        user.setPassword(newPass);
        // Set status, first password is changed
        user.setNew(false);
        userRepository.save(user);

        OldPassword oldPassword = new OldPassword(newPass, new Date(), user);
        oldPasswordRepository.save(oldPassword);
    }

}
