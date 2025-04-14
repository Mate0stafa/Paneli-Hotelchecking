package com.example.paneli.Models.Auth;

import com.example.paneli.Models.PanelUsers.User;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "property_add_request")
public class PropertyAddRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "version")
    private Integer version;

    @Column(name = "status")
    private Integer status;

    @Column(name = "unique_key")
    private String uniqueKey;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "expiration_date")
    private Date expirationDate;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "property_id")
    private long property_id;

    public long getProperty_id() {
        return property_id;
    }

    public void setProperty_id(long property_id) {
        this.property_id = property_id;
    }

    public PropertyAddRequest() {
    }

    public PropertyAddRequest(Long id, Integer version, Integer status, String uniqueKey, Date createdDate, Date expirationDate, User user) {
        this.id = id;
        this.version = version;
        this.status = status;
        this.uniqueKey = uniqueKey;
        this.createdDate = createdDate;
        this.expirationDate = expirationDate;
        this.user = user;
    }

    public PropertyAddRequest(Integer version,
                              Integer status,
                              String uniqueKey,
                              Date createdDate,
                              Date expirationDate,
                              User user,
                              long property_id) {
        this.version = version;
        this.status = status;
        this.uniqueKey = uniqueKey;
        this.createdDate = createdDate;
        this.expirationDate = expirationDate;
        this.user = user;
        this.property_id = property_id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }
}
