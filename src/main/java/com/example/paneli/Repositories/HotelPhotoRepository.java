package com.example.paneli.Repositories;

import com.example.paneli.Models.HotelPhoto;
import com.example.paneli.Models.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelPhotoRepository extends JpaRepository<HotelPhoto, Long> {
    int countByProperty(Property property);

    @Query("SELECT hp FROM HotelPhoto hp WHERE hp.property.id =:propertyId")
    List<HotelPhoto> findAllByPropertyId(@Param("propertyId") Long propertyId);

    @Query("SELECT h FROM HotelPhoto h WHERE h.property.id = :propertyId AND h.set_primary = false")
    List<HotelPhoto> findNonPrimaryPhotosByPropertyId(@Param("propertyId") Long propertyId);

}
