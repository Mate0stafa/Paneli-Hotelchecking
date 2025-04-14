package com.example.paneli.Models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Entity
@Table(name = "address_change")
public class AddressChange {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "property_id")
    private Property property;

    @Column(name = "streetName")
    private String streetName;

    @Column(name = "cityId")
    private Long cityId;

    @Column(name = "cityName")
    private String cityName;

    @Column(name = "zipCode")
    private String zipCode;

}
