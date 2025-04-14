package com.example.paneli.Repositories;



import com.example.paneli.DataObjects.CategoryDTO;
import com.example.paneli.DataObjects.PropertyTypeDto;
import com.example.paneli.Models.Hotel_Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Hotel_TypeRepository extends JpaRepository<Hotel_Type, Integer> {
    Hotel_Type findById(Long id);
    Hotel_Type findByType(String type);

    @Query("select h from Hotel_Type h where h.version > ?1")
    List<Hotel_Type> findByVersionGreaterThan(int num);

    @Query("SELECT new com.example.paneli.DataObjects.CategoryDTO(h.id, h.type) FROM Hotel_Type h")
    List<CategoryDTO> findAllIdsAndTypes();

    @Query("SELECT new com.example.paneli.DataObjects.CategoryDTO(h.id, h.type) FROM Hotel_Type h WHERE h.version > ?1")
    List<CategoryDTO> findAllIdsAndTypesAndVersion(int num);

    @Query("SELECT new com.example.paneli.DataObjects.PropertyTypeDto(h.id, h.type, h.description, "
            + "(SELECT COUNT(p) FROM Property p WHERE p.hotel_type.id = h.id)) "
            + "FROM Hotel_Type h")
    List<PropertyTypeDto> findAllPropertyTypes();

}
