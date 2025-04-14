package com.example.paneli.DataObjects.Property;

public class PropertyAddress {
    private String locationCity;
    private String latitude;
    private String longitude;
    private String street;
    private String region;
    private String zip_code;
    private String website;

    public PropertyAddress(String locationCity, String latitude, String longitude, String street, String region, String zip_code, String website) {
        this.locationCity = locationCity;
        this.latitude = latitude;
        this.longitude = longitude;
        this.street = street;
        this.region = region;
        this.zip_code = zip_code;
        this.website = website;
    }

    public PropertyAddress() {
    }

    public String getLocationCity() {
        return locationCity;
    }

    public void setLocationCity(String locationCity) {
        this.locationCity = locationCity;
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

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getZip_code() {
        return zip_code;
    }

    public void setZip_code(String zip_code) {
        this.zip_code = zip_code;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
