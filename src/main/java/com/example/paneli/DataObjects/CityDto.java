package com.example.paneli.DataObjects;

import lombok.*;

@ToString
public class CityDto {
    private Long id;
    private String name;
    private String region;
    private String description;
    private Float cityTax;

    public Float getCityTax() {
        return cityTax;
    }

    public void setCityTax(Float cityTax) {
        this.cityTax = cityTax;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRegion() {
        return region;
    }

    private Boolean Promote;

    public Boolean getPromote() {
        return Promote;
    }

    public void setPromote(Boolean promote) {
        Promote = promote;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public CityDto(Long id, String name, String region, Boolean promote) {
        this.id = id;
        this.name = name;
        this.region = region;
        Promote = promote;
    }

    public CityDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public CityDto() {
    }
}
