package com.example.paneli.Controllers;

import com.example.paneli.Models.UserLoginAttempts;
import com.example.paneli.Repositories.UserLoginAttemptsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class UserLoginAttemptsController {

    @Autowired
    UserLoginAttemptsRepository userLoginAttemptsRepository;

    @PostMapping(value = "/sendToUserLoginAttempts")
    @ResponseBody
    public Map<String, Object> sendToUserLoginAttempts(@RequestParam(name = "username") String username) {

        Map<String, Object> response = new HashMap<>();

        if (username != null) {
            UserLoginAttempts userLoginAttempts = userLoginAttemptsRepository.findByUsername(username);

            if (userLoginAttempts != null) {
                response.put("exists", true);
                if (userLoginAttempts.isUserBlocked() && userLoginAttempts.getAttempts() == 5) {
                    response.put("blockedUntil", userLoginAttempts.getBlockedUntil());
                }
                if (userLoginAttempts.isUserBlocked()) {
                    response.put("attempts", userLoginAttempts.getAttempts());
                } else {
                    response.put("exists", false);
                }
            } else {
                response.put("exists", false);
            }
        }

        return response;

    }

}
