package com.example.paneli.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
@Table(name = "property_point_of_interest")
public class PropertyPointsOfInterest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String pointName;

    @Column(name = "distance")
    private Float pointDistance;

    @Column(name = "status")
    private Integer status;

    @Column(name = "distance_type")
    private String distanceType;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "points_of_interest_id")
    private PointsOfInterest pointsOfInterest;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "property_id")
    @JsonBackReference
    private Property property;

    public PropertyPointsOfInterest() {
    }

    public PropertyPointsOfInterest(String pointName, Float pointDistance, Integer status, String distanceType, PointsOfInterest pointsOfInterest, Property property) {
        this.pointName = pointName;
        this.pointDistance = pointDistance;
        this.status = status;
        this.distanceType = distanceType;
        this.pointsOfInterest = pointsOfInterest;
        this.property = property;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPointName() {
        return pointName;
    }

    public void setPointName(String pointName) {
        this.pointName = pointName;
    }

    public Float getPointDistance() {
        return pointDistance;
    }

    public void setPointDistance(Float pointDistance) {
        this.pointDistance = pointDistance;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDistanceType() {
        return distanceType;
    }

    public void setDistanceType(String distanceType) {
        this.distanceType = distanceType;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public PointsOfInterest getPointsOfInterest() {
        return pointsOfInterest;
    }

    public void setPointsOfInterest(PointsOfInterest pointsOfInterest) {
        this.pointsOfInterest = pointsOfInterest;
    }


}
