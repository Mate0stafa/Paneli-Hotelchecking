package com.example.paneli.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
@Table(name = "hotel_time")
public class HotelTime {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int version;
    private String check_in_from;
    private String check_out_from;
    private String check_in;
    private String check_out;
    private String check_in_to;
    private String check_out_to;
    private int arrivalTime;
    private int addressDetails;
    private int phoneNumber;
    private int agelimit;
    private int minAge;
    private int maxAge;

    private int Curfew;

    public HotelTime(int version, String check_in, String check_out, ZanaTimeZone zana_time_zone) {
        this.version = version;
        this.check_in = check_in;
        this.check_out = check_out;
        this.zana_time_zone = zana_time_zone;
    }

    @OneToOne(mappedBy = "hotel_time", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonBackReference
    private Property property;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "time_zone_id")
    @JsonBackReference
    private ZanaTimeZone zana_time_zone;

    public HotelTime() {

    }

    public ZanaTimeZone getZana_time_zone() {
        return zana_time_zone;
    }

    public void setZana_time_zone(ZanaTimeZone zana_time_zone) {
        this.zana_time_zone = zana_time_zone;
    }

    public Property getProperty() {
        return property;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getCheck_out_from() {
        return check_out_from;
    }

    public void setCheck_out_from(String check_out_from) {
        this.check_out_from = check_out_from;
    }

    public String getCheck_in_from() {
        return check_in_from;
    }

    public void setCheck_in_from(String check_in_from) {
        this.check_in_from = check_in_from;
    }

    public String getCheck_out() {
        return check_out;
    }

    public void setCheck_out(String check_out) {
        this.check_out = check_out;
    }

    public String getCheck_in() {
        return check_in;
    }

    public void setCheck_in(String check_in) {
        this.check_in = check_in;
    }

    public String getCheck_out_to() {
        return check_out_to;
    }

    public void setCheck_out_to(String check_out_to) {
        this.check_out_to = check_out_to;
    }

    public String getCheck_in_to() {
        return check_in_to;
    }

    public void setCheck_in_to(String check_in_to) {
        this.check_in_to = check_in_to;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getAddressDetails() {
        return addressDetails;
    }

    public void setAddressDetails(int addressDetails) {
        this.addressDetails = addressDetails;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getAgelimit() {
        return agelimit;
    }

    public void setAgelimit(int agelimit) {
        this.agelimit = agelimit;
    }

    public int getCurfew() {
        return Curfew;
    }

    public void setCurfew(int curfew) {
        Curfew = curfew;
    }

    public int getMinAge() {
        return minAge;
    }

    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }
}
