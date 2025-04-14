package com.example.paneli.Controllers;

import com.example.paneli.Services.UserServices.UserDetails;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class MyErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return "error";
        }

        Object principal = authentication.getPrincipal();
        if (principal == null) {
            return "error";
        }

        if (principal instanceof UserDetails) {
            // User is authenticated, return the "error2" page
            return "error2";
        } else {
            // User is not authenticated, return the "error" page
            return "error";
        }
    }

}

