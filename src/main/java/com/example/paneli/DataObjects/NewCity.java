package com.example.paneli.DataObjects;

public class NewCity {

    private String ccFips;
    private String countryIsoId;
    private String fullName;
    private int showCity;
    private Long countyId;
    private String countryName;
    public String getCcFips() {
        return ccFips;
    }

    public void setCcFips(String ccFips) {
        this.ccFips = ccFips;
    }

    public String getCountryIsoId() {
        return countryIsoId;
    }

    public void setCountryIsoId(String countryIsoId) {
        this.countryIsoId = countryIsoId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }


    public Long getCountyId() {
        return countyId;
    }

    public void setCountyId(Long countyId) {
        this.countyId = countyId;
    }

    public int getShowCity() {
        return showCity;
    }

    public void setShowCity(int showCity) {
        this.showCity = showCity;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
}
