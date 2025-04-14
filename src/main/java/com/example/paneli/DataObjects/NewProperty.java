package com.example.paneli.DataObjects;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class NewProperty {
    private String propertyName;
    private String propertyCategory;
    private int propertyRooms;
    private int propertyStars;
    private String propertyLanguage;
    private String propertyMealPlan;
    private String propertyCheckIn;
    private String propertyCheckOut;
    private String propertyCurrency;
    private String propertyTimeZone;
    private String propertyCountry;
    private String propertyCounty;
    private String propertyCity;
    private String propertyStreet;
    private String propertyZipCode;
    private String propertyLatitude;
    private String propertyLongitude;
    private String propertyWebAddress;
    private String propertyPhoneNumber;
    private String propertyAdminName;
    private String firstName;
    private String lastName;
    private String propertyAdminEmail;
    private String propertyUsernameEmail;

    private String NUIS;
    private String idCard;

    private String taxname;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateofbirth;


    public NewProperty() {
    }

    public NewProperty(String propertyName, String propertyCategory, int propertyRooms, int propertyStars, String propertyLanguage, String propertyMealPlan, String propertyCheckIn, String propertyCheckOut, String propertyCurrency, String propertyTimeZone, String propertyCountry, String propertyCounty, String propertyCity, String propertyStreet, String propertyZipCode, String propertyLatitude, String propertyLongitude, String propertyWebAddress, String propertyPhoneNumber, String propertyAdminName, String propertyAdminEmail, String propertyUsernameEmail, String firstName, String lastName, String NUIS) {
        this.propertyName = propertyName;
        this.propertyCategory = propertyCategory;
        this.propertyRooms = propertyRooms;
        this.propertyStars = propertyStars;
        this.propertyLanguage = propertyLanguage;
        this.propertyMealPlan = propertyMealPlan;
        this.propertyCheckIn = propertyCheckIn;
        this.propertyCheckOut = propertyCheckOut;
        this.propertyCurrency = propertyCurrency;
        this.propertyTimeZone = propertyTimeZone;
        this.propertyCountry = propertyCountry;
        this.propertyCounty = propertyCounty;
        this.propertyCity = propertyCity;
        this.propertyStreet = propertyStreet;
        this.propertyZipCode = propertyZipCode;
        this.propertyLatitude = propertyLatitude;
        this.propertyLongitude = propertyLongitude;
        this.propertyWebAddress = propertyWebAddress;
        this.propertyPhoneNumber = propertyPhoneNumber;
        this.propertyAdminName = propertyAdminName;
        this.propertyAdminEmail = propertyAdminEmail;
        this.propertyUsernameEmail = propertyUsernameEmail;
        this.firstName = firstName;
        this.lastName = lastName;
        this.NUIS = NUIS;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyCategory() {
        return propertyCategory;
    }

    public void setPropertyCategory(String propertyCategory) {
        this.propertyCategory = propertyCategory;
    }

    public int getPropertyRooms() {
        return propertyRooms;
    }

    public void setPropertyRooms(int propertyRooms) {
        this.propertyRooms = propertyRooms;
    }

    public int getPropertyStars() {
        return propertyStars;
    }

    public void setPropertyStars(int propertyStars) {
        this.propertyStars = propertyStars;
    }

    public String getPropertyLanguage() {
        return propertyLanguage;
    }

    public void setPropertyLanguage(String propertyLanguage) {
        this.propertyLanguage = propertyLanguage;
    }

    public String getPropertyMealPlan() {
        return propertyMealPlan;
    }

    public void setPropertyMealPlan(String propertyMealPlan) {
        this.propertyMealPlan = propertyMealPlan;
    }

    public String getPropertyCheckIn() {
        return propertyCheckIn;
    }

    public void setPropertyCheckIn(String propertyCheckIn) {
        this.propertyCheckIn = propertyCheckIn;
    }

    public String getPropertyCheckOut() {
        return propertyCheckOut;
    }

    public void setPropertyCheckOut(String propertyCheckOut) {
        this.propertyCheckOut = propertyCheckOut;
    }

    public String getPropertyCurrency() {
        return propertyCurrency;
    }

    public void setPropertyCurrency(String propertyCurrency) {
        this.propertyCurrency = propertyCurrency;
    }

    public String getPropertyTimeZone() {
        return propertyTimeZone;
    }

    public void setPropertyTimeZone(String propertyTimeZone) {
        this.propertyTimeZone = propertyTimeZone;
    }

    public String getPropertyCountry() {
        return propertyCountry;
    }

    public void setPropertyCountry(String propertyCountry) {
        this.propertyCountry = propertyCountry;
    }

    public String getPropertyCounty() {
        return propertyCounty;
    }

    public void setPropertyCounty(String propertyCounty) {
        this.propertyCounty = propertyCounty;
    }

    public String getPropertyCity() {
        return propertyCity;
    }

    public void setPropertyCity(String propertyCity) {
        this.propertyCity = propertyCity;
    }

    public String getPropertyStreet() {
        return propertyStreet;
    }

    public void setPropertyStreet(String propertyStreet) {
        this.propertyStreet = propertyStreet;
    }

    public String getPropertyZipCode() {
        return propertyZipCode;
    }

    public void setPropertyZipCode(String propertyZipCode) {
        this.propertyZipCode = propertyZipCode;
    }

    public String getPropertyLatitude() {
        return propertyLatitude;
    }

    public void setPropertyLatitude(String propertyLatitude) {
        this.propertyLatitude = propertyLatitude;
    }

    public String getPropertyLongitude() {
        return propertyLongitude;
    }

    public void setPropertyLongitude(String propertyLongitude) {
        this.propertyLongitude = propertyLongitude;
    }

    public String getPropertyWebAddress() {
        return propertyWebAddress;
    }

    public void setPropertyWebAddress(String propertyWebAddress) {
        this.propertyWebAddress = propertyWebAddress;
    }

    public String getPropertyPhoneNumber() {
        return propertyPhoneNumber;
    }

    public void setPropertyPhoneNumber(String propertyPhoneNumber) {
        this.propertyPhoneNumber = propertyPhoneNumber;
    }

    public String getPropertyAdminName() {
        return propertyAdminName;
    }

    public void setPropertyAdminName(String propertyAdminName) {
        this.propertyAdminName = propertyAdminName;
    }

    public String getPropertyAdminEmail() {
        return propertyAdminEmail;
    }

    public void setPropertyAdminEmail(String propertyAdminEmail) {
        this.propertyAdminEmail = propertyAdminEmail;
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

    public String getNUIS() {
        return NUIS;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public void setNUIS(String NUIS) {
        this.NUIS = NUIS;
    }

    public String getPropertyUsernameEmail() {
        return propertyUsernameEmail;
    }

    public void setPropertyUsernameEmail(String propertyUsernameEmail) {
        this.propertyUsernameEmail = propertyUsernameEmail;
    }

    public String getTaxname() {
        return taxname;
    }

    public void setTaxname(String taxname) {
        this.taxname = taxname;
    }

    public LocalDate getDateofbirth() {
        return dateofbirth;
    }

    public void setDateofbirth(LocalDate dateofbirth) {
        this.dateofbirth = dateofbirth;
    }

}
