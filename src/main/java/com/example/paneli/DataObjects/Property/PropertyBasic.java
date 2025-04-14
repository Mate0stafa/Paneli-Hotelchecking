package com.example.paneli.DataObjects.Property;

import java.util.List;

public class PropertyBasic {
    private String name;
    private int numberOfRooms;
    private String country;
    private String preferredLanguage;
    private int stars;
    private String hotelType;
    private List<String> languages;
    private String status;
    private boolean checked_out;
    private String logo;

    public PropertyBasic(String name, int numberOfRooms, String country, String preferredLanguage, int stars, String hotelType, List<String> languages, String status, boolean checked_out, String logo) {
        this.name = name;
        this.numberOfRooms = numberOfRooms;
        this.country = country;
        this.preferredLanguage = preferredLanguage;
        this.stars = stars;
        this.hotelType = hotelType;
        this.languages = languages;
        this.status = status;
        this.checked_out = checked_out;
        this.logo = logo;
    }

    public PropertyBasic() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberOfRooms() {
        return numberOfRooms;
    }

    public void setNumberOfRooms(int numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPreferredLanguage() {
        return preferredLanguage;
    }

    public void setPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public String getHotelType() {
        return hotelType;
    }

    public void setHotelType(String hotelType) {
        this.hotelType = hotelType;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isChecked_out() {
        return checked_out;
    }

    public void setChecked_out(boolean checked_out) {
        this.checked_out = checked_out;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
