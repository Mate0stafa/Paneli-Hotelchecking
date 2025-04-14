package com.example.paneli.DataObjects.GetUniqueProperty;

import java.util.ArrayList;

public class SinglePropertyRootResponse {

    private ArrayList<SinglePropertyResponse> getProperty;

    public SinglePropertyRootResponse() {
    }

    public SinglePropertyRootResponse(ArrayList<SinglePropertyResponse> getProperty) {
        this.getProperty = getProperty;
    }

    public ArrayList<SinglePropertyResponse> getGetProperty() {
        return getProperty;
    }

    public void setGetProperty(ArrayList<SinglePropertyResponse> getProperty) {
        this.getProperty = getProperty;
    }
}
