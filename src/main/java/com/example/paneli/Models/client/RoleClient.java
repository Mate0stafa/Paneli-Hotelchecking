package com.example.paneli.Models.client;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "role_client")
public class RoleClient {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "version")
    private int version;
    @Column(name = "authority")
    private String authority;

    public RoleClient() {
    }

    public RoleClient(int version, String authority) {
        this.version = version;
        this.authority = authority;
    }

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "role_client")
    private List<UserClient> userClients;

    public Long getId() {
        return id;
    }

    public int getVersion() {
        return version;
    }

    public String getAuthority() {
        return authority;
    }
}
