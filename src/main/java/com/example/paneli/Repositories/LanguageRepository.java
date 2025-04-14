package com.example.paneli.Repositories;

import com.example.paneli.DataObjects.LanguageDTO;
import com.example.paneli.Models.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LanguageRepository extends JpaRepository<Language, Long> {
    @Query("select l from Language l where l.id = ?1")
    Optional<Language> findById(Long id);

    @Query("select l from Language l where l.name = ?1")
    public Language findByName(String name);

    @Query("SELECT new com.example.paneli.DataObjects.LanguageDTO(l.id, l.name, l.englishname) FROM Language l")
    List<LanguageDTO> findAllIdsAndNames();
}
