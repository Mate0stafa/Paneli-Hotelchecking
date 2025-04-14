package com.example.paneli.Repositories;

import com.example.paneli.Models.PointsOfInterest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PointsOfInterestRepository extends JpaRepository<PointsOfInterest, Long> {

    @Query("select p from PointsOfInterest p where p.name = ?1")
    List<PointsOfInterest> findAllByName(String name);
}