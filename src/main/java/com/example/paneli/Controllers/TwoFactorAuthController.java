package com.example.paneli.Controllers;


import com.example.paneli.Models.PanelUsers.Role;
import com.example.paneli.Models.PanelUsers.User;
import com.example.paneli.Models.UserLoginAttempts;
import com.example.paneli.Repositories.UserLoginAttemptsRepository;
import com.example.paneli.Repositories.UserPanel.UserRepository;
import com.example.paneli.Services.UserServices.UserDetails;
import com.example.paneli.config.TwoFactorAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class TwoFactorAuthController {


    @Autowired
    TwoFactorAuthenticationService twoFactorAuthenticationService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserLoginAttemptsRepository userLoginAttemptsRepository;

    @GetMapping("/start-2fa")
    public ModelAndView start2fa(HttpServletRequest request, HttpServletResponse response) throws IOException {

        if (request.getSession().getAttribute("goToReservationAfterLogin")!= null) {
            String url = (String) request.getSession().getAttribute("goToReservationAfterLogin");
            request.getSession().removeAttribute("goToReservationAfterLogin");
            response.sendRedirect(url);
        }

        ModelAndView modelAndView = new ModelAndView();
        System.out.println(request.isUserInRole("ROLE_ADMIN") + " -------------------");

        User user = userRepository.findByUsername(request.getUserPrincipal().getName());

        String email = user.getEmail();
        String code = twoFactorAuthenticationService.generateRandomCode();
        request.getSession().setAttribute("code", code);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        System.out.println("Roles after successful login:");
        authorities.forEach(authority -> System.out.println(authority.getAuthority()));

        HttpSession session = request.getSession();
        session.setAttribute("isInStart2FA", true);
        twoFactorAuthenticationService.sendVerificationCode(email, code);

        SecurityContextHolder.clearContext();

        char[] chars = email.toCharArray();
        int length = chars.length;

        // Obfuscate characters between the first and last letters
        for (int i = 1; i < length - 1; i++) {
            chars[i] = '*';
        }

        modelAndView.addObject("email", new String(chars));
        modelAndView.addObject("userEmail", email);
        modelAndView.setViewName("/loginandregister/2FA/2faPage");
        return modelAndView;
    }

    private RequestCache requestCache = new HttpSessionRequestCache();

    @PostMapping("/verify-2fa")
    @ResponseBody
    public Map<String, String> verifyTwoFactorAuth(@RequestParam String email, @RequestParam String code, HttpServletRequest request, HttpServletResponse servletResponse) {
        Map<String, String> response = new HashMap<>();
        System.out.println(email);
        System.out.println(code);
        System.out.println(request.isUserInRole("ROLE_ADMIN") + " ////////////////////////");
        String expectedCode = request.getSession().getAttribute("code").toString();
        if (twoFactorAuthenticationService.verifyCode(code, expectedCode)) {
            User user = userRepository.findByEmail(email);
            System.out.println(user.getUsername());
            UserDetails userDetails = new UserDetails(user);
            System.out.println(user.getRole().get(0).getAuthority() + " +++++++++++++++++++");
            List<Role> userRoles = user.getRole();
            List<String> stringList = new ArrayList<>();
            for (int i = 0; i < userRoles.size(); i++) {
                stringList.add(userRoles.get(i).getAuthority());
            }

            List<GrantedAuthority> authorities = stringList.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
//            System.out.println(authentication.getName());

            SecurityContextHolder.getContext().setAuthentication(authentication);
            request.getSession().setAttribute("twoFactorAuthCompleted", true);

            authorities.forEach(authority -> System.out.println(authority.getAuthority()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            request.getSession().setAttribute("twoFactorAuthCompleted", true);

            System.out.println("Two-factor authentication successful.");
            response.put("status", "success");
            response.put("username", user.getUsername());

            DefaultSavedRequest savedRequest = (DefaultSavedRequest) requestCache.getRequest(request, servletResponse);

            if (savedRequest != null) {
                String targetUrl = savedRequest.getRedirectUrl();
                response.put("targetUrl", targetUrl);
            } else {
                response.put("targetUrl", "/");
            }

            System.out.println("------------Login Successful.------------");
        } else {
            response.put("status", "error");
            response.put("message", "Invalid verification code");
        }

        return response;
    }

}
