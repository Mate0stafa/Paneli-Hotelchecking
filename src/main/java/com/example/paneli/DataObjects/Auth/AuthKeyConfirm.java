package com.example.paneli.DataObjects.Auth;

public class AuthKeyConfirm {
    private Long propertyId;
    private Long authId;
    private String authPin;

    public AuthKeyConfirm() {
    }

    public AuthKeyConfirm(Long propertyId, Long authId, String authPin) {
        this.propertyId = propertyId;
        this.authId = authId;
        this.authPin = authPin;
    }

    public Long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
    }

    public Long getAuthId() {
        return authId;
    }

    public void setAuthId(Long authId) {
        this.authId = authId;
    }

    public String getAuthPin() {
        return authPin;
    }

    public void setAuthPin(String authPin) {
        this.authPin = authPin;
    }


}
