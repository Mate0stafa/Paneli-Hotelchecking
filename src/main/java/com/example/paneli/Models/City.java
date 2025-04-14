package com.example.paneli.Models;

import com.example.paneli.Models.client.UserClient;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "city")
public class City {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int version;

    private String country_iso_id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "county_id")
    @JsonManagedReference
    private County county;

    // show_city -> shows the ranking of the cities that appear on the homepages
    @Column(name = "show_city", nullable = false)
    private int show_city;
    // Promote -> show in homepage or no
    private Boolean Promote;

    public Boolean getPromote() {
        return Promote;
    }

    public void setPromote(Boolean promote) {
        Promote = promote;
    }


    public City(int version) {
        this.version = version;
    }

    public City() {

    }

    @Column(name = "full_name", nullable = false)
    private String full_name;

    @Column(name="city_tax", columnDefinition = "FLOAT DEFAULT 3")
    private float cityTax;

    @Column(name = "city_description",columnDefinition = "LONGTEXT")
    private String cityDescription;

    public String getCityDescription() {
        return cityDescription;
    }

    public void setCityDescription(String cityDescription) {
        this.cityDescription = cityDescription;
    }

    public float getCityTax() {
        return cityTax;
    }

    public void setCityTax(float cityTax) {
        this.cityTax = cityTax;
    }

    public String cityImagePath(){
        return "/uploads/qytete/"+getFull_name()+".jpg";
    }

    @OneToMany(mappedBy = "city",
            cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<CityPhoto> cityPhotos;

    public List<CityPhoto> getCityPhotos() {
        return cityPhotos;
    }

    @Column(name = "cc_fips", nullable = false)
    private String cc_fips;

    public int getShow_city() {
        return show_city;
    }

    public void setShow_city(int show_city) {
        this.show_city = show_city;
    }

    public City(String full_name, String cc_fips, int show_city, County county) {
        this.full_name = full_name;
        this.cc_fips = cc_fips;
        this.show_city = show_city;
        this.county = county;
    }


    public City(int version, String country_iso_id, String full_name, String cc_fips, int show_city, County county,Boolean promote, Float cityTax, String cityDescription) {
        this.version = version;
        this.country_iso_id = country_iso_id;
        this.full_name = full_name;
        this.cc_fips = cc_fips;
        this.show_city = show_city;
        this.county = county;
        this.Promote = promote;
        this.cityTax = cityTax;
        this.cityDescription=cityDescription;
    }

    @OneToMany(targetEntity=Address.class, mappedBy="city",cascade=CascadeType.ALL)
    @JsonBackReference
    private List<Address> addresses = new ArrayList<>();

    public County getCounty() {
        return county;
    }

    public void setCounty(County county) {
        this.county = county;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public String getCc_fips() {
        return cc_fips;
    }

    public void setCc_fips(String cc_fips) {
        this.cc_fips = cc_fips;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getCountry_iso_id() {
        return country_iso_id;
    }

    public void setCountry_iso_id(String country_iso_id) {
        this.country_iso_id = country_iso_id;
    }

    public void setCityPhotos(List<CityPhoto> cityPhotos) {
        this.cityPhotos = cityPhotos;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }
}
