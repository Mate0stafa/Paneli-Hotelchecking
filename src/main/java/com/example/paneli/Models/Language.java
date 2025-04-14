package com.example.paneli.Models;
import com.example.paneli.Models.client.UserClient;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "language")
public class Language {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private int version;
    private String code;
    private String name;
    private String englishname;


    @OneToMany(mappedBy = "language", fetch = FetchType.LAZY)
    private List<Property> propertyList;


    @OneToMany(mappedBy="language")
    private List<UserClient> userClientList;

    public Language() {
    }

    public Language(int version, String code, String name) {
        this.version = version;
        this.code = code;
        this.name = name;
    }
}