package com.example.paneli.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "hotel_photo")
public class HotelPhoto {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean set_primary;

    public HotelPhoto() {
    }

    public HotelPhoto(Property property, int version, String file_name) {
        this.property = property;
        this.version = version;
        this.file_name = file_name;
    }

    public boolean isSet_primary() {
        return set_primary;
    }

    public void setSet_primary(boolean set_primary) {
        this.set_primary = set_primary;
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "property_id", nullable = false)
    @JsonBackReference
    private Property property;
    private int version;
    private String file_name;

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public Long getId() {
        return id;
    }

    public int getVersion() {
        return version;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getImagePath(){
        return "/klienti/"+getFolderName()+"/"+this.getFile_name();
    }

    public String getFolderName(){
        return "property_id_" + getPropertyLongId();
    }

    public String getPropertyLongId(){
        Long propId = this.getProperty().getId()+ 2654435l;
        return propId.toString();
    }
}
