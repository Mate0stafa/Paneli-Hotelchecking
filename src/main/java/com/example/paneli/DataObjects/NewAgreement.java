package com.example.paneli.DataObjects;

public class NewAgreement {


    private String firstName;
    private String lastName;
    private String legalName;
    private String nuis;

    public NewAgreement() {
    }

    public NewAgreement(String firstName, String lastName, String legalName, String nuis) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.legalName = legalName;
        this.nuis = nuis;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLegalName() {
        return legalName;
    }

    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }

    public String getNuis() {
        return nuis;
    }

    public void setNuis(String nuis) {
        this.nuis = nuis;
    }
}
