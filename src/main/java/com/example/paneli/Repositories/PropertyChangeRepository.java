package com.example.paneli.Repositories;

import com.example.paneli.Models.PropertyChange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface PropertyChangeRepository extends JpaRepository<PropertyChange, Long> {


    @Modifying
    @Transactional
    @Query("delete from PropertyChange h where h.id = ?1")
    void deleteById(Long id);

}
