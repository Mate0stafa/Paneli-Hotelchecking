package com.example.paneli.config;

import com.example.paneli.Models.UserLoginAttempts;
import com.example.paneli.Repositories.UserLoginAttemptsRepository;
import com.example.paneli.Repositories.UserPanel.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Autowired
    UserLoginAttemptsRepository userLoginAttemptsRepository;
    @Autowired
    UserRepository userRepository;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {

        String username = request.getParameter("email");

        if (userRepository.findByUsername(username) != null) {

            UserLoginAttempts userLoginAttempts = userLoginAttemptsRepository.findByUsername(username);
            boolean userDeleted = false;

            if (userLoginAttempts != null) {

                if (userLoginAttempts.isUserBlocked()) {
                    if (userLoginAttempts.getAttempts() < 5) {
                        userLoginAttempts.setAttempts(userLoginAttempts.getAttempts() + 1);
                        userLoginAttemptsRepository.save(userLoginAttempts);
                    }
                } else {
                    userLoginAttemptsRepository.deleteById(userLoginAttempts.getId());
                    userDeleted = true;
                }

            }

            if (userLoginAttempts == null || userDeleted) {

                userLoginAttempts = new UserLoginAttempts();
                userLoginAttempts.setUsername(username);
                userLoginAttempts.setAttempts(1);
                userLoginAttempts.blockUserForGivenMinutes(5);
                userLoginAttemptsRepository.save(userLoginAttempts);

            }

        }

        response.sendRedirect("/login");

    }

}
