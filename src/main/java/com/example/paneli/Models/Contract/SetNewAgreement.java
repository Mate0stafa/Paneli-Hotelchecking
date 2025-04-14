package com.example.paneli.Models.Contract;

import com.example.paneli.Models.City;
import com.example.paneli.Models.Property;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "SetNewAgreement")
public class SetNewAgreement {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int version;
    private String address;
    private String firstName;
    private String legalBusinessName;
    private String lastName;
    @Column(name = "email")
    private String emailAddress;
    private String phoneNumber;
    private String nuis;
    private Date date;
    private String street;
    private String zipCode;
    private Boolean status;
    public SetNewAgreement() {
    }

    public SetNewAgreement(int version, String address, String first_name, String legal_bussines_name, String last_name, String email, String phone_number, String nuis, Date date,City city, Property property,String street,String zip_code, Boolean status) {
        this.version = version;
        this.address = address;
        this.firstName = first_name;
        this.legalBusinessName = legal_bussines_name;
        this.lastName = last_name;
        this.emailAddress = email;
        this.phoneNumber = phone_number;
        this.nuis = nuis;
        this.date = date;
        this.city = city;
        this.property = property;
        this.street = street;
        this.zipCode = zip_code;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLegalBusinessName() {
        return legalBusinessName;
    }

    public void setLegalBusinessName(String legalBusinessName) {
        this.legalBusinessName = legalBusinessName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getNuis() {
        return nuis;
    }

    public void setNuis(String nuis) {
        this.nuis = nuis;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "city_id")
    private City city;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "property_id")
    @JsonBackReference
    private Property property;

}
