package com.example.paneli.DataObjects.GetUniqueProperty;

public class PropertyAuth {
    private String apiKey;
    private String propKey;

    public PropertyAuth() {
    }

    public PropertyAuth(String apiKey, String propKey) {
        this.apiKey = apiKey;
        this.propKey = propKey;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getPropKey() {
        return propKey;
    }

    public void setPropKey(String propKey) {
        this.propKey = propKey;
    }
}
