package com.example.paneli.Repositories;

import com.example.paneli.Models.HotelFacility;
import com.example.paneli.Models.RoomAmenities.Amenity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AmenityRepository extends JpaRepository<Amenity, Long> {

    //search dhe pageable
    @Query("select c from Amenity c where LOWER(c.amenityName) like LOWER(CONCAT('%', :name, '%'))")
    Page<Amenity> findByNameContainingIgnoreCase(String name, Pageable pageable);

    @Query("select c from Amenity c where LOWER(c.amenityName) like LOWER(CONCAT('%', :name, '%'))")
    List<Amenity> findByNameContainingIgnoreCase(String name);

    @Query("select a from Amenity a where a.amenityName = ?1")
    List<Amenity> findAllByAmenityName(String name);


    @Query("select a from Amenity a where a.amenityType = ?1")
    List<Amenity> findAllByAmenityType(String type);
}