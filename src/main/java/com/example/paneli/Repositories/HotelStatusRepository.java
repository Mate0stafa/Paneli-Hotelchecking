package com.example.paneli.Repositories;

import com.example.paneli.Models.HotelStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface HotelStatusRepository extends JpaRepository<HotelStatus, Integer> {


}
