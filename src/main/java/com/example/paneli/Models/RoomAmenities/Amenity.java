package com.example.paneli.Models.RoomAmenities;

import javax.persistence.*;

@Entity
@Table(name = "amenity")
public class Amenity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "amenity_name")
    private String amenityName;

    @Column(name = "amenity_type")
    private String amenityType;

    @Column(name = "version")
    private Integer version;

    @Column(name = "amenity_description")
    private String amenityDescription;

    @Column(name = "file_name")
    private String fileName;

    public Amenity() {}

    public Amenity(String amenityName, String amenityType, Integer version, String amenityDescription, String fileName) {
        this.amenityName = amenityName;
        this.amenityType = amenityType;
        this.version = version;
        this.amenityDescription = amenityDescription;
        this.fileName = fileName;
    }

    // getters and setters for all fields including fileName

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getAmenityDescription() {
        return amenityDescription;
    }

    public void setAmenityDescription(String amenityDescription) {
        this.amenityDescription = amenityDescription;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
