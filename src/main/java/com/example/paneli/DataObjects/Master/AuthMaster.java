package com.example.paneli.DataObjects.Master;

public class AuthMaster {
    private String apiKey;

    public AuthMaster() {
    }

    public AuthMaster(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}
