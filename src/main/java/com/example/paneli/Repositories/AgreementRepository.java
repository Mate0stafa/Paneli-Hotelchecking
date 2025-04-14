package com.example.paneli.Repositories;

import com.example.paneli.Models.Contract.Agreement;
import com.example.paneli.Models.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AgreementRepository extends JpaRepository<Agreement, Long> {

    @Query("select a from Agreement a where a.property = ?1")
    Agreement findByProperty(Property property);

    List<Agreement> findByCityId(Long city);

    List<Agreement> findAllByProperty(Property property);
}