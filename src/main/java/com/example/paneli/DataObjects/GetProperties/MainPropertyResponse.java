package com.example.paneli.DataObjects.GetProperties;

import java.util.ArrayList;

public class MainPropertyResponse {
    private ArrayList<PropertyResponse> getProperties;

    public MainPropertyResponse() {
    }

    public MainPropertyResponse(ArrayList<PropertyResponse> getProperties) {
        this.getProperties = getProperties;
    }

    public ArrayList<PropertyResponse> getGetProperties() {
        return getProperties;
    }

    public void setGetProperties(ArrayList<PropertyResponse> getProperties) {
        this.getProperties = getProperties;
    }
}
