package com.example.paneli.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
@Table(name = "city_photo")
public class CityPhoto {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int version;
    private String file_name;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "city_id", nullable = false)
    @JsonBackReference
    private City city;

    public City getCity() {
        return city;
    }

    public String getImagePath(){
        return "/uploads/qytete/"+this.getFile_name();
    }

    public void setCity(City city) {
        this.city = city;
    }

    public CityPhoto(int version, String file_name, City city) {
        this.version = version;
        this.file_name = file_name;
        this.city = city;
    }

    public CityPhoto() {
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

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }
}
