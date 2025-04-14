package com.example.paneli.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Table(name="hotelier_id")
public class HotelierId {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="logo")
    private String logo;

    @Column(name = "id_card")
    private String idCard;


    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "date_of_birth")
    private LocalDate dateofbirth;

    @Column(name = "first_name")
    private String firstname;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "property_id")
    @JsonBackReference
    private Property property;


    public HotelierId(String idCard, String logo, Property property) {
        this.idCard = idCard;
        this.logo = logo;
        this.property = property;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getLogo() {
        return "uploads/hotelierId/" + logo;
    }

    public String getLogo2() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

