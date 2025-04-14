package com.example.paneli.Repositories;

import com.example.paneli.Models.Property;
import com.example.paneli.Models.ReviewsTab;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface ReviewsTabRepository extends JpaRepository<ReviewsTab, Long> {

    @Query("select r from ReviewsTab r where r.id = ?1")
    Optional<ReviewsTab> findById(Long id);

    @Query("select d from ReviewsTab d where d.property.id = ?1")
    Optional<ReviewsTab> findReviewsTabByPropertyId(Long id);

    @Query("select a from ReviewsTab a where a.property = ?1")
    List<ReviewsTab> findAllReviewsByProperty(Property property);

    @Query("select n from ReviewsTab n where n.id =  :id")
    ReviewsTab  findReviewsTabById(@Param ("id") Long id);


    @Modifying
    @Transactional
    @Query(value = "delete from ReviewsTab l where l.id = ?1")
    void deleteReviewsTabById(Long id);



}