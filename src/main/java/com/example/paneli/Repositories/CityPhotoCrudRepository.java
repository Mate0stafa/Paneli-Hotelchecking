package com.example.paneli.Repositories;

import com.example.paneli.Models.CityPhoto;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface CityPhotoCrudRepository extends CrudRepository<CityPhoto, Long> {

    @Modifying
    @Query("delete from CityPhoto r where r.id = ?1")
    void deleteById(Long id);
}