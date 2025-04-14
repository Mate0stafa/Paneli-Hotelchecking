package com.example.paneli.Models.Contract;

import com.example.paneli.Models.City;
import com.example.paneli.Models.Property;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "agreement")
public class Agreement {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int version;
    private String address;
    private String first_name;
    private String legal_business_name;
    private String last_name;
    @Column(name = "e_mail")
    private String email;
    private String phone_number;
    private String nuis;
    private Date date;
    private String street;
    private String zip_code;


    public Agreement() {
    }

    public Agreement(int version, String address, String first_name, String legal_business_name, String last_name,
                     String email, String phone_number, String nuis, Date date, City city, Property property,
                     String street, String zip_code, String taxExtractFileName) {
        this.version = version;
        this.address = address;
        this.first_name = first_name;
        this.legal_business_name = legal_business_name;
        this.last_name = last_name;
        this.email = email;
        this.phone_number = phone_number;
        this.nuis = nuis;
        this.date = date;
        this.city = city;
        this.property = property;
        this.street = street;
        this.zip_code = zip_code;
    }
    public Agreement(int version, String address, String first_name, String legal_business_name, String last_name,
                     String email, String phone_number, String nuis, Date date, City city, Property property,
                     String street, String zip_code) {
        this.version = version;
        this.address = address;
        this.first_name = first_name;
        this.legal_business_name = legal_business_name;
        this.last_name = last_name;
        this.email = email;
        this.phone_number = phone_number;
        this.nuis = nuis;
        this.date = date;
        this.city = city;
        this.property = property;
        this.street = street;
        this.zip_code = zip_code;
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

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLegal_bussines_name() {
        return legal_business_name;
    }

    public void setLegal_bussines_name(String legal_bussines_name) {
        this.legal_business_name = legal_bussines_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
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

    public String getZip_code() {
        return zip_code;
    }

    public void setZip_code(String zip_code) {
        this.zip_code = zip_code;
    }


    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "city_id")
    private City city;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "property_id")
    @JsonBackReference
    private Property property;

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

    @OneToMany(mappedBy = "agreement")
    private List<Changeofownership> changeofownerships;

    public List<Changeofownership> getChangeofownerships() {
        return changeofownerships;
    }

    public void setChangeofownerships(List<Changeofownership> changeofownerships) {
        this.changeofownerships = changeofownerships;
    }
}