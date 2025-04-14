package com.example.paneli.CrudRepositories;

import com.example.paneli.Models.Address;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface AddressCrudRepository extends CrudRepository<Address, Long> {

    @Transactional
    @Modifying
    @Query("delete from Address a where a.id = ?1")
    void deleteById(Long id);
}