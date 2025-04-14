package com.example.paneli.Models.client;



import com.example.paneli.Models.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.*;


@Entity
@Table(name = "user_client")
public class UserClient {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private int version;
    private int password_expired;
    @Column(nullable = true)
    private String street;
    private Date birth_day;
    @Column(unique = true)
    private String username;
    private String first_name;
    private String last_name;
    private String password;
    @Column(unique = true)
    private String email;
    private String zip_code;
    private String phone_number;
    private int account_expired;
    private int account_locked;
    private int enabled;

    public UserClient() {
    }


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_client_role_client",
            joinColumns = { @JoinColumn(name = "user_client_id")},
            inverseJoinColumns = { @JoinColumn (name = "role_client_id")})
    private List<RoleClient> role_client;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "country_id", nullable = false)
    @JsonManagedReference
    private Country country;

    @ManyToOne
    @JoinColumn(name = "language_id")
    private Language language;

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
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

    public int getPassword_expired() {
        return password_expired;
    }

    public String getStreet() {
        return street;
    }

    public Date getBirth_day() {
        return birth_day;
    }

    public String getUsername() {
        return username;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getZip_code() {
        return zip_code;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public int getAccount_expired() {
        return account_expired;
    }

    public int getAccount_locked() {
        return account_locked;
    }

    public List<RoleClient> getRole_client() {
        return role_client;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public void setPassword_expired(int password_expired) {
        this.password_expired = password_expired;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setBirth_day(Date birth_day) {
        this.birth_day = birth_day;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setZip_code(String zip_code) {
        this.zip_code = zip_code;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public void setAccount_expired(int account_expired) {
        this.account_expired = account_expired;
    }

    public void setAccount_locked(int account_locked) {
        this.account_locked = account_locked;
    }

    public void setRole_client(List<RoleClient> role_client) {
        this.role_client = role_client;
    }

    public int getEnabled() {
        return enabled;
    }

    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }

}



