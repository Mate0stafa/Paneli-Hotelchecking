package com.example.paneli.config;


import com.example.paneli.Models.PanelUsers.User;
import com.example.paneli.Models.UserLoginAttempts;
import com.example.paneli.Repositories.UserLoginAttemptsRepository;
import com.example.paneli.Repositories.UserPanel.UserRepository;
import com.example.paneli.Services.UserServices.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private RequestCache requestCache = new HttpSessionRequestCache();

    @Bean
    public UserDetailsService userDetailsService(){return new UserDetailsServiceImpl();}
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(userDetailsService());
        return authenticationProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.authenticationProvider(authenticationProvider());
    }

    @Autowired
    UserRepository userRepository;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .headers()
                .addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
                .and()
                .authorizeRequests()
                .antMatchers("/start-2fa","/")
                .authenticated()
                .antMatchers("/login","/calendarMobile","/check-statusM","/testoM","/changeOccupiedM","/changePriceM","/changeminstayM","/changemaxstayM",
                "/createPriceAtDateM","closeRoomsM","/editRangeTotalRoomsM","/editPricesM","/maxmumStayM")
                .permitAll()
                .anyRequest()
                .permitAll()
                .and()
                .formLogin()
                .loginPage("/login")
                .usernameParameter("email")
                .passwordParameter("userpassword")
                .loginProcessingUrl("/loginToAllbookers")
                .failureHandler(authenticationFailureHandler()) // Custom authentication failure handler
                .successHandler(authenticationSuccessHandler()) // Custom authentication success handler
                .permitAll()
                .and()
                .logout()
                .and()
                .exceptionHandling()
                .accessDeniedPage("/403")
                .and()
                .csrf()
                .disable();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {

        return (request, response, authentication) -> {

            DefaultSavedRequest savedRequest = (DefaultSavedRequest) requestCache.getRequest(request, response);

            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(authentication);
            User user = userRepository.findByUsername(request.getParameter("email"));

            customSecurityAuth(user, response);

            if (user.getNew() != null && user.getNew()) {
                response.sendRedirect("/");
            } else {
                if (user.getTwoFA() == null || user.getTwoFA()) {
                    response.sendRedirect("/start-2fa");
                } else {
                    if (savedRequest != null) {
                        String targetUrl = savedRequest.getRedirectUrl();
                        response.sendRedirect(targetUrl);
                    } else {
                        response.sendRedirect("/");
                    }
                }
            }
        };
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }

    @Autowired
    UserLoginAttemptsRepository userLoginAttemptsRepository;

    public void customSecurityAuth(User user, HttpServletResponse response) throws IOException {

        UserLoginAttempts userLoginAttempts = userLoginAttemptsRepository.findByUsername(user.getUsername());
        if (userLoginAttempts != null) {
            if (userLoginAttempts.isUserBlocked() && userLoginAttempts.getAttempts() >= 5) {
                SecurityContextHolder.clearContext();
                response.sendRedirect("/login");
            } else {
                userLoginAttemptsRepository.deleteById(userLoginAttempts.getId());
            }
        }
    }

}
