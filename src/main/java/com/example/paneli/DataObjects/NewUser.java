package com.example.paneli.DataObjects;

public class NewUser {

    private Long version;
    private boolean password_expired;
    private String username;
    private boolean account_locked;
    private String password;
    private boolean account_expired;
    private boolean enabled;
    private String fullname;
    private String email;

    public NewUser() {
    }

    public NewUser(Long version, boolean password_expired, String username, boolean account_locked, String password, boolean account_expired, boolean enabled, String fullname, String email) {
        this.version = version;
        this.password_expired = password_expired;
        this.username = username;
        this.account_locked = account_locked;
        this.password = password;
        this.account_expired = account_expired;
        this.enabled = enabled;
        this.fullname = fullname;
        this.email = email;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public boolean isPassword_expired() {
        return password_expired;
    }

    public void setPassword_expired(boolean password_expired) {
        this.password_expired = password_expired;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isAccount_locked() {
        return account_locked;
    }

    public void setAccount_locked(boolean account_locked) {
        this.account_locked = account_locked;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAccount_expired() {
        return account_expired;
    }

    public void setAccount_expired(boolean account_expired) {
        this.account_expired = account_expired;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
