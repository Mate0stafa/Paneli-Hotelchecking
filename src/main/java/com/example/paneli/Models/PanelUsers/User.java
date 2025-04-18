package com.example.paneli.Models.PanelUsers;


import com.example.paneli.Models.Auth.PropertyAddRequest;
import com.example.paneli.Models.OldPassword;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Entity
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name")
    private String full_name;

    @Column(name = "email")
    private String email;

    @Column(name = "username")
    private String username;

    @Column(name = "password_expired")
    @ColumnDefault("false")
    private boolean password_expired;

    @Column(name = "account_locked")
    @ColumnDefault("false")
    private boolean account_locked;

    @Column(name = "password")
    private String password;

    @Column(name = "enabled")
    @ColumnDefault("true")
    private boolean enabled;

    @Column(name = "is_admin")
    @ColumnDefault("false")
    private boolean is_admin;

    @Column(name = "is_new")
    @ColumnDefault("false")
    private boolean isNew;

    @Column(name = "twoFA")
    @ColumnDefault("true")
    private boolean twoFA;

    public User( String full_name, String email, String username, boolean password_expired, boolean account_locked, String password, boolean enabled, List<Role> role, boolean is_admin, boolean isNew, boolean twoFA) {
        this.full_name = full_name;
        this.email = email;
        this.username = username;
        this.password_expired = password_expired;
        this.account_locked = account_locked;
        this.password = password;
        this.enabled = enabled;
        this.role = role;
        this.is_admin = is_admin;
        this.isNew = isNew;
        this.twoFA = twoFA;
    }

    public User(String full_name, String email, String username, String password, boolean is_admin) {
        this.full_name = full_name;
        this.email = email;
        this.username = username;
        this.password = password;
        this.is_admin = is_admin;
    }

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "user_role",
            joinColumns = { @JoinColumn(name = "user_id")},
            inverseJoinColumns = { @JoinColumn (name = "role_id")})
    @JsonBackReference
    private List<Role> role;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<OldPassword> oldPasswords;

    public List<OldPassword> getOldPasswords() {
        Collections.sort(oldPasswords, Comparator.comparing(r -> r.getDate() != null ? r.getDate() : new Date(0)));
        return oldPasswords;
    }

    public Date getPasswordDate() {
        if (oldPasswords == null || oldPasswords.isEmpty()){
            return new Date(0);
        }else {
            Collections.sort(oldPasswords, Comparator.comparing(r -> r.getDate() != null ? r.getDate() : new Date(0)));
            return oldPasswords.get(oldPasswords.size()-1).getDate();
        }
    }

    public void setOldPasswords(List<OldPassword> oldPasswords) {
        this.oldPasswords = oldPasswords;
    }
    //New password can't be the same as last 4 password
    public boolean isPasswordUsedBefore(String newPassword) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        List<OldPassword> oldPasswords = this.getOldPasswords();
        for (OldPassword p : oldPasswords) {
            if (bCryptPasswordEncoder.matches(newPassword, p.getPassword())) {
                return true;
            }
        }
        return false;
    }

    public User() {}

    public Long getUserLoginId(){
        return this.id + 2654434L;
    }

    @OneToMany(mappedBy = "user")
    private List<PropertyAddRequest> propertyAddRequests;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Role> getRole() {
        return role;
    }

    public void setRole(List<Role> role) {
        this.role = role;
    }

    public Boolean getPassword_expired() {
        return password_expired;
    }

    public void setPassword_expired(boolean password_expired) {
        this.password_expired = password_expired;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getAccount_locked() {
        return account_locked;
    }

    public void setAccount_locked(boolean account_locked) {
        this.account_locked = account_locked;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean isIs_admin() {
        return is_admin;
    }

    public void setIs_admin(boolean is_admin) {
        this.is_admin = is_admin;
    }

    public Boolean getNew() {
        return isNew;
    }

    public void setNew(Boolean aNew) {
        isNew = aNew;
    }

    public Boolean getTwoFA() {
        return twoFA;
    }

    public void setTwoFA(Boolean twoFA) {
        this.twoFA = twoFA;
    }

}
