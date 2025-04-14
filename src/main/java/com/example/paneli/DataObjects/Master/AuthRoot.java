package com.example.paneli.DataObjects.Master;

public class AuthRoot {
    private AuthMaster authentication;

    public AuthRoot() {
    }

    public AuthRoot(AuthMaster authentication) {
        this.authentication = authentication;
    }

    public AuthMaster getAuthentication() {
        return authentication;
    }

    public void setAuthentication(AuthMaster authentication) {
        this.authentication = authentication;
    }
}
