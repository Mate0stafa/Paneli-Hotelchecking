package com.example.paneli.DataObjects;

public class PasswordDto {
    private String token;
    private String oldPassword;
    private String newPassword;


    public PasswordDto(String token, String oldPassword, String newPassword) {
        this.token = token;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public PasswordDto() {
    }

    public PasswordDto(String token) {
        this.token = token;
    }

    public PasswordDto(String token, String newPassword) {
        this.token = token;
        this.newPassword = newPassword;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
