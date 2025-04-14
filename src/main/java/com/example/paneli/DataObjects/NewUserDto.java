package com.example.paneli.DataObjects;

public class NewUserDto {

    private Long version;
    private String username;
    private String email;
    private String password;

    public NewUserDto() {
    }

    public NewUserDto(Long version, String username, String email, String password) {
        this.version = version;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
