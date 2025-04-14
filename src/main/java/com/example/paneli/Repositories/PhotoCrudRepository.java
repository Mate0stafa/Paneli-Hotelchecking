package com.example.paneli.Repositories;


import com.example.paneli.Models.HotelPhoto;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoCrudRepository extends CrudRepository<HotelPhoto, Long> {

    @Modifying
    @Query("delete from HotelPhoto h where h.id = ?1")
    void deleteById(Long id);

}
