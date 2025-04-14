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
    private int version;
    private String full_name;
    private String email;
    private String username;
    private int password_expired;
    private int account_locked;
    private String password;
    private int account_expired;
    private int enabled;
    private boolean is_admin;

    @Column(name = "isNew")
    @ColumnDefault("false")
    private boolean isNew;

    @Column(name = "twoFA")
    @ColumnDefault("true")
    private boolean twoFA;

    public User(int version, String full_name, String email, String username, int password_expired, int account_locked, String password, int account_expired, int enabled, List<Role> role,boolean is_admin, Boolean isNew, Boolean twoFA) {
        this.version = version;
        this.full_name = full_name;
        this.email = email;
        this.username = username;
        this.password_expired = password_expired;
        this.account_locked = account_locked;
        this.password = password;
        this.account_expired = account_expired;
        this.enabled = enabled;
        this.role = role;
        this.is_admin = is_admin;
        this.isNew = isNew;
        this.twoFA = twoFA != null ? twoFA : false;
    }

    public User(String full_name,
                String email,
                String username,
                String password,
                boolean is_admin
                 ) {
        this.full_name = full_name;
        this.email = email;
        this.username = username;
        this.password = password;
        this.is_admin = is_admin;

    }

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
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
        return this.id + 2654434l;
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

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getPassword_expired() {
        return password_expired;
    }

    public void setPassword_expired(int password_expired) {
        this.password_expired = password_expired;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAccount_locked() {
        return account_locked;
    }

    public void setAccount_locked(int account_locked) {
        this.account_locked = account_locked;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAccount_expired() {
        return account_expired;
    }

    public void setAccount_expired(int account_expired) {
        this.account_expired = account_expired;
    }

    public int getEnabled() {
        return enabled;
    }

    public void setEnabled(int enabled) {
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

    public boolean isIs_admin() {
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
