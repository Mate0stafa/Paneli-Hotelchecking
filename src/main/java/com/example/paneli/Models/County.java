package com.example.paneli.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="county")
public class County {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    private int version;

    @OneToMany(mappedBy="county", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<City> cities;

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "country_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private Country country;

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }


    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }

    public County() {}

    public County(String name, int version, Country country) {
        this.name = name;
        this.version = version;
        this.country = country;
    }

    public List<City> getCities() {
        return cities;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
