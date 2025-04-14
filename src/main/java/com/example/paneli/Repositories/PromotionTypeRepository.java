package com.example.paneli.Repositories;

import com.example.paneli.Models.PromotionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PromotionTypeRepository extends JpaRepository<PromotionType, Long> {

    @Query("select p from PromotionType p where p.promotionName = ?1")
    List<PromotionType> findAllByPromotionName(String promotionName);
}