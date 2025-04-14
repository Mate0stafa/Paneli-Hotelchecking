package com.example.paneli.Models;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "hotel_type")
public class Hotel_Type {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "version")
    private int version;

    @Column(name = "type")
    private String type;

    @Column(name = "description")
    private String description;

    @Column(name = "file_name")
    String file_name;

    @OneToMany( mappedBy = "hotel_type")
    @JsonManagedReference
    private List<Property> propertyList;

    public Hotel_Type() {
    }

    public Hotel_Type(int version, String type, String description, String file_name) {
        this.version = version;
        this.type = type;
        this.description = description;
        this.file_name = file_name;
    }

    public List<Property> getPropertyList() {
        return propertyList;
    }

    public void setPropertyList(List<Property> propertyList) {
        this.propertyList = propertyList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Hotel_Type get() {
        return null;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }
}
