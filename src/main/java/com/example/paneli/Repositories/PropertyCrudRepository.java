package com.example.paneli.Repositories;

import com.example.paneli.Models.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyCrudRepository extends CrudRepository<Property, Long> {

    @Modifying
    @Query("delete from Property p where p.id = ?1")
    void deleteById(Long id);



}