package com.example.paneli.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "hotel_status")
public class HotelStatus {
    @Id
    private int id;
    private int version;
    private String status;

    @OneToMany(mappedBy = "hotel_status")
    @JsonBackReference
    private List<Property> properties;

    public List<Property> getProperties() {
        return properties;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
