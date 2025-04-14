package com.example.paneli.Models.PanelUsers;

import com.example.paneli.Models.Property;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private int version;
    private String authority;

    @ManyToMany(mappedBy = "role")
    @JsonManagedReference
    private List<User> users;

    public List<User> getUsers() {
        return users;
    }

    public Role(String authority) {
        this.authority = authority;
    }

    @ManyToMany(mappedBy = "roles")
    @JsonManagedReference
    private List<Property> properties ;

    public Role() {

    }

    public void setUsers(List<User> users) {
        this.users = users;
    }


    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
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

    public void setVersion(int version) {
        this.version = version;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public Long getRoleLoginId(){
        return this.id + 2654434l;
    }
}
