package com.example.paneli.Repositories;


import com.example.paneli.DataObjects.Property.FacilityHotelProjection;
import com.example.paneli.Models.HotelFacility;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelFacilityRepository extends JpaRepository<HotelFacility, Long>{

    HotelFacility findById(int id);
    HotelFacility findByName(String name);

    //search dhe pageable
    @Query("SELECT c FROM HotelFacility c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<HotelFacility> findByNameContainingIgnoreCase(String name, Pageable pageable);

    @Query("SELECT c FROM HotelFacility c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<HotelFacility> findByNameContainingIgnoreCases(String name);


    @Query("select h from HotelFacility h where h.name = ?1")
    List<HotelFacility> findAllByName(String name);


    @Query(value = "select facility.id AS id, facility.file_name AS photo, facility.name as name, facility.description as description FROM HotelFacility facility")
    List<FacilityHotelProjection> getAll();

    @Query(value = "SELECT facility.id AS id, facility.file_name AS photo, facility.name as name, facility.description as description FROM HotelFacility facility WHERE facility.id = :id")
    List<FacilityHotelProjection> getProjectionById(@Param("id")Long id);

    @Query(value ="select facility.id AS id, facility.file_name AS photo, facility.name as name, facility.description as description FROM HotelFacility facility where Lower(facility.name) like lower(concat('%',:search,'%')) or lower(facility.file_name) like lower(concat('%',:search,'%')) or lower(facility.description) like lower(concat('%',:search,'%')) ")
    List<FacilityHotelProjection> getFacilityBySearch(@Param("search")String search);
}
