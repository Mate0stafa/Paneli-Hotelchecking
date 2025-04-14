package com.example.paneli.Repositories;

import com.example.paneli.Models.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface CountryRepository extends JpaRepository<Country, Long> {

    //Krijojme nje query per country/pagination
    @Query("SELECT c FROM Country c WHERE LOWER(c.country_name) LIKE LOWER(CONCAT('%', :country_name, '%'))")
    List<Country> findByCountry_nameContainingIgnoreCase(String country_name);


    @Query("select c from Country c where c.country_name = ?1")
    Country findByCountry_name(String name);

    List<Country> findAllById(Long id);

    @Modifying
    @Transactional
    @Query("DELETE FROM Country c WHERE c.id = :id")
    void deleteCountry_ById(@Param("id") Long id);

    @Query("SELECT c FROM Country c")
    List<Country> findAllCountries();

    @Query("SELECT c.country_name FROM Country c")
    List<String> findAllCountryNames();

}