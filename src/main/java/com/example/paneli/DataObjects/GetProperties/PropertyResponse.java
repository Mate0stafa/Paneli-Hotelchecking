package com.example.paneli.DataObjects.GetProperties;

import java.util.ArrayList;

public class PropertyResponse {

    private String name;
    private String propId;
    private String propTypeId;
    private String ownerId;
    private String currency;
    private String address;
    private String city;
    private String state;
    private String country;
    private String postcode;
    private String latitude;
    private String longitude;
    private ArrayList<RoomTypeResponse> roomTypes;

    public PropertyResponse() {
    }

    public PropertyResponse(String name, String propId, String propTypeId, String ownerId, String currency, String address, String city, String state, String country, String postcode, String latitude, String longitude, ArrayList<RoomTypeResponse> roomTypes) {
        this.name = name;
        this.propId = propId;
        this.propTypeId = propTypeId;
        this.ownerId = ownerId;
        this.currency = currency;
        this.address = address;
        this.city = city;
        this.state = state;
        this.country = country;
        this.postcode = postcode;
        this.latitude = latitude;
        this.longitude = longitude;
        this.roomTypes = roomTypes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPropId() {
        return propId;
    }

    public void setPropId(String propId) {
        this.propId = propId;
    }

    public String getPropTypeId() {
        return propTypeId;
    }

    public void setPropTypeId(String propTypeId) {
        this.propTypeId = propTypeId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public ArrayList<RoomTypeResponse> getRoomTypes() {
        return roomTypes;
    }

    public void setRoomTypes(ArrayList<RoomTypeResponse> roomTypes) {
        this.roomTypes = roomTypes;
    }
}
