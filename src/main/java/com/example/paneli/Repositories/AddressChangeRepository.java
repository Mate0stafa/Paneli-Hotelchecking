package com.example.paneli.Repositories;

import com.example.paneli.Models.AddressChange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressChangeRepository extends JpaRepository<AddressChange, Long> {
}
