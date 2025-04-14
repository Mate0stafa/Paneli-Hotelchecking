package com.example.paneli.Repositories;


import com.example.paneli.Models.Address;
import com.example.paneli.Models.Property;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepostitory extends JpaRepository<Address, Long> {
    @Query("select a from Address a where a.property = ?1")
    Address findByProperty(Property property);

    @Query("select a from Address a where a.city.id = :cityId")
    Page<Address> findByCityId(@Param("cityId") Long cityId, Pageable pageable);

    // Paginim dhe Search methods nepermjet cityId
    @Query("select a from Address a where a.city.id = :cityId and a.property.name like %:search%")
    Page<Address> findByCityIdAndPropertyNameContaining(@Param("cityId") Long cityId, @Param("search") String search, Pageable pageable);

    Optional<Address> findByEmail(String email);

    @Modifying
    @Query("UPDATE Address a SET a.city = null WHERE a.city.id = :cityId")
    void dissociateCityFromAddresses(@Param("cityId") Long cityId);
}

