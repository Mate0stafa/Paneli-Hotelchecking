package com.example.paneli.Repositories;

import com.example.paneli.Models.UserApiToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserApiTokenRepository extends JpaRepository<UserApiToken, Long> {

    @Query("select u from UserApiToken u where u.tokenValue = ?1")
    UserApiToken findByTokenValue(String token);
}