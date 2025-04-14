package com.example.paneli.Models;


import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "reviews_tab")
public class ReviewsTab {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Integer staff_review;
    private Integer facilities_review;
    private  Integer cleanless_review;
    private Integer comfort_review;
    private Integer value_of_money_review;
    private Integer location_review;
    private String good_review;
    private String bad_review;
    private Date data;
    private Double mesatare;

    //0 - visible, not - visible
    private Integer status;

    @ManyToOne
    @JoinColumn(name = "property_id")
    @JsonBackReference
    private Property property;

    public ReviewsTab() {
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStaff_review() {
        return staff_review;
    }

    public void setStaff_review(Integer staff_review) {
        this.staff_review = staff_review;
    }

    public Integer getFacilities_review() {
        return facilities_review;
    }

    public void setFacilities_review(Integer facilities_review) {
        this.facilities_review = facilities_review;
    }

    public Integer getCleanless_review() {
        return cleanless_review;
    }

    public void setCleanless_review(Integer cleanless_review) {
        this.cleanless_review = cleanless_review;
    }

    public Integer getComfort_review() {
        return comfort_review;
    }

    public void setComfort_review(Integer comfort_review) {
        this.comfort_review = comfort_review;
    }

    public Integer getValue_of_money_review() {
        return value_of_money_review;
    }

    public void setValue_of_money_review(Integer value_of_money_review) {
        this.value_of_money_review = value_of_money_review;
    }

    public Integer getLocation_review() {
        return location_review;
    }

    public void setLocation_review(Integer location_review) {
        this.location_review = location_review;
    }

    public String getGood_review() {
        return good_review;
    }

    public void setGood_review(String good_review) {
        this.good_review = good_review;
    }

    public String getBad_review() {
        return bad_review;
    }

    public void setBad_review(String bad_review) {
        this.bad_review = bad_review;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Double getMesatare() {
        return mesatare;
    }

    public void setMesatare(Double mesatare) {
        this.mesatare = mesatare;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
