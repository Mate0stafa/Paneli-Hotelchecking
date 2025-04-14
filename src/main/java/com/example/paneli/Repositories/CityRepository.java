package com.example.paneli.Repositories;


import com.example.paneli.DataObjects.CityDto;
import com.example.paneli.Models.City;
import com.example.paneli.Models.County;

import com.example.paneli.Models.Property;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface CityRepository  extends JpaRepository<City, Long> {

   @Query("SELECT COUNT(c) FROM City c WHERE c.Promote = :value")
   int countByPromote(@Param("value") Boolean value);

   List<City> findByCounty(Long county);
   @Query("SELECT c FROM City c WHERE c.show_city BETWEEN :lowerBound AND :upperBound")
   List<City> findByShowCityBetween(int lowerBound, int upperBound);

   @Query("select c from City c where lower(c.full_name) like lower(concat('%', :full_name, '%'))")
   Page<City> findByFull_nameContainingIgnoreCase(String full_name, Pageable pageable);

   @Query("select c from City c where lower(c.full_name) like lower(concat('%', :full_name, '%'))")
   List<City> findByFull_nameContainingIgnoreCase(String full_name);

   @Query("select c from City c where lower(c.full_name) like lower(concat('%', :full_name, '%'))")
   List<CityDto> findByFull_nameContainingIgnoreCases(String full_name);

   @Query("select c from City c where c.full_name = ?1")
   List<CityDto> findByFull_names();

   @Query("select c from City c where c.Promote = ?1")
   List<City> findAllByPromote(Boolean value);

   @Query("select c from City c where c.full_name = ?1")
   City findByFull_name(String city);

   @Query("select c from City c where c.full_name = ?1")
   List<City> findAllByFull_name(String name);


   @Query("select c from City c where c.cc_fips like ?1")
   List<City> findCitiesByCc_fipsIsLike(String cc_fips);

   @Query("select p from Property p where p.name = ?1")
   List<Property> findAllByName(String name);

   @Query("select c from City c where c.show_city = ?1")
   List<City> findAllByShow_city(int id);

   @Query(value = "SELECT * FROM city c WHERE c.show_city = ?1", nativeQuery = true)
   List<City> findByShowCityValue(int showCityValue);

   @Query("select c from City c where c.id = ?1")
   City findById(City city);

   @Query("select c from City c where c.county = ?1")
   List<City> findAllByCounty(County county);

   @Query("select c from City c where c.full_name like %?1%")
   List<City> findAllByFull_nameContaining(String cityname);

   @Modifying
   @Transactional
   @Query("DELETE FROM City c WHERE c.id = :id")
           void deleteCityById(@Param("id") Long id);

   @Query("SELECT new com.example.paneli.DataObjects.CityDto(c.id, c.full_name) FROM City c")
   List<CityDto> findCityIdAndName();

   @Query("SELECT c FROM City c WHERE c.county.name = :countyName")
   List<City> findByCounty_name(@Param("countyName") String countyName);



}
