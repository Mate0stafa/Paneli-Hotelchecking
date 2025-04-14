package com.example.paneli.Repositories;


import com.example.paneli.Models.Country;
import com.example.paneli.Models.County;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CountyRepository extends JpaRepository<County, Long> {

    //Krijojme nje query per metoden(string,pageable)
    @Query("SELECT c FROM County c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<County> findByNameContainingIgnoreCase(String name);

    County findById(long id);

    County findByName(String name);

    @Query("select c from County c where c.country = ?1")
    List<County> findAllByCountry(Country country);
    @Query("select c from County c where c.country.id = ?1")
    List<County> findAllByCountryId(Long countyId);

    @Query("select c from County c where c.country.country_name = ?1")
    List<County> findAllByCountryName(String country_name);

    @Query("SELECT c FROM County c WHERE c.country.country_name = :countryName")
    List<County> findByCountry_country_name(@Param("countryName") String countryName);


}
