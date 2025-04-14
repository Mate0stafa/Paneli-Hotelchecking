package com.example.paneli.Repositories;

import com.example.paneli.DataObjects.HotelTimeDTO;
import com.example.paneli.Models.HotelTime;
import com.example.paneli.Models.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelTimeRepository extends JpaRepository<HotelTime, Long> {

    @Query("select h from HotelTime h where h.property = ?1")
    HotelTime findByProperty(Property property);

    @Query("SELECT new com.example.paneli.DataObjects.HotelTimeDTO(h.check_in, h.check_out, h.addressDetails, h.phoneNumber, h.agelimit, h.minAge, h.maxAge, h.Curfew) FROM HotelTime h WHERE h.property = ?1")
    HotelTimeDTO findHotelTimeByProp(Property property);

}

