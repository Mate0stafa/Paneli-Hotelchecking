package com.example.paneli.DataObjects.Auth;

public class UserRequest {

    private String username;
    private String password;
    private long property_id;
    public UserRequest() {
    }

    public UserRequest(String username, String password, long property_id) {
        this.username = username;
        this.password = password;
        this.property_id = property_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getProperty_id() {
        return property_id;
    }

    public void setProperty_id(long property_id) {
        this.property_id = property_id;
    }
}
