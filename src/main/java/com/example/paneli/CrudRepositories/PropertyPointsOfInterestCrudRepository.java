package com.example.paneli.CrudRepositories;

import com.example.paneli.Models.PropertyPointsOfInterest;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface PropertyPointsOfInterestCrudRepository extends CrudRepository<PropertyPointsOfInterest, Long> {
    @Transactional
    @Modifying
    @Query("delete from PropertyPointsOfInterest p where p.id = ?1")
    void deleteById(Long id);
}