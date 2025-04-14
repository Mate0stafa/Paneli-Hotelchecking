package com.example.paneli.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;

@Service
public class TwoFactorAuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(TwoFactorAuthenticationService.class);

    @Autowired
    private JavaMailSender javaMailSender;

    public String generateRandomCode() {
        String characters = "0123456789";
        SecureRandom random = new SecureRandom();

        StringBuilder codeBuilder = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            int randomIndex = random.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            codeBuilder.append(randomChar);
        }

        return codeBuilder.toString();
    }

    @Async
    public void sendVerificationCode(String email, String code) {
        System.out.println(code + " CODE");
        logger.info("Sending verification code to email: {}", email);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Two-Factor Authentication Code");
        message.setText("Your verification code is: " + code);
        try {
            javaMailSender.send(message);
            logger.info("Verification code sent successfully to email: {}", email);
        } catch (Exception e) {
            logger.error("Failed to send verification code to email: {}", email, e);
        }
    }

    public boolean verifyCode(String enteredCode, String expectedCode) {
        return enteredCode.equals(expectedCode);
    }
}
