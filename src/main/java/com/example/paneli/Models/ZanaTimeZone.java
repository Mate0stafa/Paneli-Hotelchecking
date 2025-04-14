package com.example.paneli.Models;


import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "zana_time_zone")
public class ZanaTimeZone {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String version;
    private String name;
    private String description;



    public Long getId() {
        return id;
    }



    public String getVersion() {
        return version;
    }

    @OneToMany(mappedBy="zana_time_zone")
    @JsonManagedReference
    private List<HotelTime> hotelTimeList;

    public void setId(Long id) {
        this.id = id;
    }

    public void setHotelTimeList(List<HotelTime> hotelTimeList) {
        this.hotelTimeList = hotelTimeList;
    }

    public List<HotelTime> getHotelTimeList() {
        return hotelTimeList;
    }
    public void setVersion(String version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
