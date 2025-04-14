package com.example.paneli.Repositories;

import com.example.paneli.Models.HotelierId;
import com.example.paneli.Models.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;


@Repository
public interface HotelierRepository extends JpaRepository<HotelierId, Long> {

    @Query("select b from HotelierId b where b.property = ?1")
    HotelierId findByProperty(Property property);




    @Query("select p from HotelierId p where p.id = ?1")
    Optional<HotelierId> findById(Long id);


    @Modifying
    @Transactional
    @Query("delete from HotelierId h where h.id = ?1")
    void deleteById(Long id);


}
