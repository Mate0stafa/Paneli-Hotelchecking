package com.example.paneli.Models;

import com.example.paneli.Models.client.UserClient;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="country")
public class Country {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String id_ips;
    private String id_iso;
    private String country_name;
    private String file_name;
    private String version;

    @Column(name="vat_percentage")
    private Float vatPercentage;


    @Column(name = "id_fips", length = 10000)
    private String id_fips;

    public Float getVatPercentage() {
        return vatPercentage;
    }

    public void setVatPercentage(Float vatPercentage) {
        this.vatPercentage = vatPercentage;
    }

    public Country() {}

    public String getId_fips() {
        return id_fips;
    }

    public List<County> getCounties() {
        return counties;
    }

    public void setCounties(List<County> counties) {
        this.counties = counties;
    }

    public void setId_fips(String id_fips) {
        this.id_fips = id_fips;
    }

    @OneToMany(mappedBy="country", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<County> counties;

    @OneToMany(mappedBy="country", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<UserClient> userClientList;

    public Country(String id_fips, String id_iso, String country_name,String file_name, String version) {
        this.id_fips = id_fips;
        this.id_iso = id_iso;
        this.country_name = country_name;
        this.file_name = file_name;
        this.version = version;
    }
    public Country( String country_name,String file_name, String version) {
        this.country_name = country_name;
        this.file_name = file_name;
        this.version = version;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getId_ips() {
        return id_ips;
    }

    public void setId_ips(String id_ips) {
        this.id_ips = id_ips;
    }

    public String getId_iso() {
        return id_iso;
    }

    public void setId_iso(String id_iso) {
        this.id_iso = id_iso;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public List<UserClient> getUserClientList() {
        return userClientList;
    }

    public void setUserClientList(List<UserClient> userClientList) {
        this.userClientList = userClientList;
    }
}
