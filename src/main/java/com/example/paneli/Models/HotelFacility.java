package com.example.paneli.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "hotel_facility")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelFacility {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "version")
    private int version;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "hotel_facility")
    @JsonBackReference
    private Set<Property> properties = new HashSet<>();

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = true)
    private String description;

    private String file_name;

    public HotelFacility(int version, String name, String description, String file_name) {
        this.version = version;
        this.name = name;
        this.description = description;
        this.file_name = file_name;
    }

    @Override
    public String toString() {
        return name;
    }
}
