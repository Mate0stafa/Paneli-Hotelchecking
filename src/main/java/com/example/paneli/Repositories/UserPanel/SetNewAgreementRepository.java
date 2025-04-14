package com.example.paneli.Repositories.UserPanel;

import com.example.paneli.Models.Contract.SetNewAgreement;
import com.example.paneli.Models.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface SetNewAgreementRepository extends JpaRepository<SetNewAgreement, Long> {

    @Query("select a from SetNewAgreement a where a.property = ?1")
    SetNewAgreement findByProperty(Property property);

    @Transactional
    @Modifying
    @Query("delete from SetNewAgreement r where r.id = ?1")
    void deleteById(Long id);
}
