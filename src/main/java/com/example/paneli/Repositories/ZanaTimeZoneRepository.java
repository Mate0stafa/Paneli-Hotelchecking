package com.example.paneli.Repositories;

import com.example.paneli.Models.ZanaTimeZone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ZanaTimeZoneRepository extends JpaRepository<ZanaTimeZone, Long> {

    @Query("select z from ZanaTimeZone z where z.name = ?1")
    public ZanaTimeZone findByName(String name);
}
