package com.example.paneli.DataObjects;

public class NewCountry {
    private String idFips;
    private String idIso;
    private String countryName;
    private String file_name;
    private Float vatPercentage;


    public NewCountry() {
    }

    public NewCountry(String idFips, String idIso, String countryName) {
        this.idFips = idFips;
        this.idIso = idIso;
        this.countryName = countryName;
    }

    public String getIdFips() {
        return idFips;
    }

    public void setIdFips(String idFips) {
        this.idFips = idFips;
    }

    public String getIdIso() {
        return idIso;
    }

    public void setIdIso(String idIso) {
        this.idIso = idIso;
    }

    public Float getVatPercentage() {
        return vatPercentage;
    }

    public void setVatPercentage(Float vatPercentage) {
        this.vatPercentage = vatPercentage;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }
}
