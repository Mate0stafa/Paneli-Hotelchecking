package com.example.paneli.CrudRepositories;

import com.example.paneli.Models.PointsOfInterest;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface PointsOfInterestCrudRepository extends CrudRepository<PointsOfInterest, Long> {

    @Transactional
    @Modifying
    @Query("delete from PointsOfInterest p where p.id = ?1")
    void deleteById(Long id);
}