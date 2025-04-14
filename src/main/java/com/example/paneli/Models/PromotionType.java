package com.example.paneli.Models;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "promotion_type")
public class PromotionType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "promotion_name")
    private String promotionName;

    @Column(name = "recommended_percentage")
    private Integer recommendedPercentage;

    @Column(name = "category")
    private String category;

    @Column(name = "description")
    @Size(max = 500)
    private String description;

    @OneToMany(mappedBy = "promotionType")
    @JsonManagedReference
    private List<Promotion> promotionList;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "is_active")
    private Boolean isActive;

    private String file_name;

    public PromotionType() {
    }

    public PromotionType(String promotionName, Integer recommendedPercentage, String category, String description, Date createdDate, Date startDate, Date endDate,String file_name, Boolean isActive) {
        this.promotionName = promotionName;
        this.recommendedPercentage = recommendedPercentage;
        this.category = category;
        this.description = description;
        this.createdDate = createdDate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.file_name = file_name;
        this.isActive = isActive;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPromotionName() {
        return promotionName;
    }

    public void setPromotionName(String promotionName) {
        this.promotionName = promotionName;
    }

    public Integer getRecommendedPercentage() {
        return recommendedPercentage;
    }

    public void setRecommendedPercentage(Integer recommendedPercentage) {
        this.recommendedPercentage = recommendedPercentage;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public List<Promotion> getPromotionList() {
        return promotionList;
    }

    public void setPromotionList(List<Promotion> promotionList) {
        this.promotionList = promotionList;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }
}
