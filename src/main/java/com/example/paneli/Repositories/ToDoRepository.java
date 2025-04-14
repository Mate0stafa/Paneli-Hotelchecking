package com.example.paneli.Repositories;

import com.example.paneli.Models.Property;
import com.example.paneli.Models.ToDo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ToDoRepository extends JpaRepository<ToDo,Long> {

    List<ToDo> findByPropertyId(Long propertyId);

    @Query("select b from ToDo b where b.property = ?1")
    ToDo findByProperty(Property property);
}
