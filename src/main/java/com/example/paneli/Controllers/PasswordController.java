package com.example.paneli.Controllers;


import com.example.paneli.DataObjects.Admin.AdminPassword;
import com.example.paneli.DataObjects.PasswordDto;
import com.example.paneli.Models.OldPassword;
import com.example.paneli.Models.PanelUsers.User;
import com.example.paneli.Repositories.OldPasswordRepository;
import com.example.paneli.Repositories.UserPanel.UserRepository;
import com.example.paneli.Services.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Controller
public class PasswordController {
    @Autowired
    PasswordService passwordService;
    @Autowired
    OldPasswordRepository oldPasswordRepository;
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "passwordEdit")
    public ModelAndView passwordEdit(HttpServletRequest request,
                                     ModelAndView modelAndView){

        if (request.isUserInRole("ROLE_ADMIN")) {
            AdminPassword adminPassword = new AdminPassword();
            modelAndView.addObject("newPassword", adminPassword);
            modelAndView.setViewName("ROLE_ADMIN/Admin/editPassword");
        }
        return modelAndView;
    }

    @Autowired
    UserRepository userRepository;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "setNewPassword")
    public ResponseEntity<String> passwordEditPost(HttpServletRequest request,
                                         ModelAndView modelAndView,
                                         @RequestBody AdminPassword adminPassword){

        if (request.isUserInRole("ROLE_ADMIN")){
            //If pass is not valid
            if (!passwordService.isNewPasswordValid(adminPassword.getNewPassword())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password is not valid.");
            }

            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            User user = userRepository.findByUsername("admin");

            //If pass is used before
            if (user.isPasswordUsedBefore(adminPassword.getNewPassword())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This password is used before, try using a new one.");
            }

            String passIri = bCryptPasswordEncoder.encode(adminPassword.getNewPassword());
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


    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_GROUP_ACCOUNT') || hasRole('ROLE_ADMIN')")
    @ResponseBody
    @PostMapping(value = "updatePassword")
    public ResponseEntity<String> updatePassword(HttpServletRequest request, @RequestBody PasswordDto passwordDto) {
        if (request.isUserInRole("ROLE_USER") || request.isUserInRole("ROLE_GROUP_ACCOUNT") || request.isUserInRole("ROLE_ADMIN")){

            //If pass is not valid
            if (!passwordService.isNewPasswordValid(passwordDto.getNewPassword())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password is not valid.");
            }

            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            User user = userRepository.findByUsername(request.getUserPrincipal().getName());

            //Current pass not correct
            if (!bCryptPasswordEncoder.matches(passwordDto.getOldPassword(), user.getPassword())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wrong password.");
            }

            //If pass is used before
            if (user.isPasswordUsedBefore(passwordDto.getNewPassword())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This password is used before, try using a new one.");
            }

            String passIri = bCryptPasswordEncoder.encode(passwordDto.getNewPassword());
            user.setPassword(passIri);
            userRepository.save(user);

            if (user.getOldPasswords().size() >= 4) {
                oldPasswordRepository.delete(user.getOldPasswords().get(0));
            }
            OldPassword oldPassword = new OldPassword(passIri, new Date(), user);
            oldPasswordRepository.save(oldPassword);

            return ResponseEntity.ok().body("Success");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something went wrong.");
    }
}
