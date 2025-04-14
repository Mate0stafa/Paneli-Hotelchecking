package com.example.paneli.DataObjects;

import lombok.Data;

import java.io.Serializable;

@Data
public class AmenityDto implements Serializable {
    private String amenityName;
    private String amenityType;
    private String amenityDescription;

    public AmenityDto() {
    }

    public AmenityDto(String amenityName, String amenityType, String amenityDescription) {
        this.amenityName = amenityName;
        this.amenityType = amenityType;
        this.amenityDescription = amenityDescription;
    }

    public String getAmenityName() {
        return amenityName;
    }

    public void setAmenityName(String amenityName) {
        this.amenityName = amenityName;
    }

    public String getAmenityType() {
        return amenityType;
    }

    public void setAmenityType(String amenityType) {
        this.amenityType = amenityType;
    }

    public String getAmenityDescription() {
        return amenityDescription;
    }

    public void setAmenityDescription(String amenityDescription) {
        this.amenityDescription = amenityDescription;
    }
}
