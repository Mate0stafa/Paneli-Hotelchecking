package com.example.paneli.Models;


import javax.persistence.*;

@Entity
@Table(name = "user_api_token")
public class UserApiToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token_value")
    private String tokenValue;
    @Column(name = "username")
    private String username;
    @Column(name = "expired", nullable = false)
    private boolean expired = false;

    public UserApiToken(Long id, String tokenValue, String username) {
        this.id = id;
        this.tokenValue = tokenValue;
        this.username = username;
    }

    public UserApiToken(String tokenValue, String username) {
        this.tokenValue = tokenValue;
        this.username = username;
    }

    public UserApiToken() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTokenValue() {
        return tokenValue;
    }

    public void setTokenValue(String tokenValue) {
        this.tokenValue = tokenValue;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

}
