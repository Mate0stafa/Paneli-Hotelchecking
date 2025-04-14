package com.example.paneli.Services;

import com.example.paneli.Models.PanelUsers.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

@Service
public class PasswordService {
    public boolean isNewPasswordValid(String newPass) {
        return newPass != null && newPass.length() >= 12 && newPass.matches(".*[a-zA-Z].*") && newPass.matches(".*\\d.*");
    }

    public boolean hasMoreThan90Days(User user) {
        LocalDate passwordLocalDate = user.getPasswordDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return ChronoUnit.DAYS.between(passwordLocalDate, LocalDate.now()) > 90;
    }

}
