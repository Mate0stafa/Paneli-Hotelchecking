package com.example.paneli.Models;


import com.fasterxml.jackson.databind.DatabindException;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "user_api_token")
public class UserApiToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "token_id")
    private String tokenId;

    @Column(name = "token_value")
    private String tokenValue;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "expired", nullable = false)
    private boolean expired = false;

    @Column(name = "expiration_date")
    private Date expirationDate;

    public UserApiToken(String tokenId, String tokenValue, Long userId, boolean expired, Date expirationDate){
        this.tokenId = tokenId;
        this.tokenValue = tokenValue;
        this.userId = userId;
        this.expired = expired;
        this.expirationDate = expirationDate;
    }

    public UserApiToken() {
    }

    //zgjidhje paraprake
    public UserApiToken(String tokenValue, String username) {
        this.tokenId = tokenValue;     // first parameter as tokenId now
        this.tokenValue = username;    // second parameter as tokenValue now
        this.userId = null;
        this.expired = false;
        this.expirationDate = new Date(System.currentTimeMillis() + 86400000); // 24H
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userID) {
        this.userId = userId;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public boolean isExpired() {
        return (expired && expirationDate.before(new Date()));
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

}
