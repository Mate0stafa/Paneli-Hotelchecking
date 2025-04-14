package com.example.paneli.Repositories;

import com.example.paneli.Models.Contract.Changeofownership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChangeofownershipRepository extends JpaRepository<Changeofownership, Long> {
    @Query("SELECT co FROM Changeofownership co WHERE co.agreement.id = ?1 AND co.status = 0")
    List<Changeofownership> findAllByAgreement_IdAndStatus( Long id);

    @Query("SELECT co FROM Changeofownership co WHERE co.agreement.id = ?1 ")
    List<Changeofownership> findAllByAgreement_Id( Long id);
}
