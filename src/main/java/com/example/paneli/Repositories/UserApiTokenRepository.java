package com.example.paneli.Repositories;

import com.example.paneli.Models.UserApiToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserApiTokenRepository extends JpaRepository<UserApiToken, Long> {

    @Query("select u from UserApiToken u where u.tokenValue = ?1")
    UserApiToken findByTokenValue(String token);

    @Query("select u from UserApiToken u where u.tokenId = ?1")
    UserApiToken findByTokenId(String token);


    UserApiToken findByUserId(Long userId);

    @Query("select u from UserApiToken u where u.userId = ?1")
    List<UserApiToken> findAllByUserId(Long userId);


    boolean existsByUserId(Long userId);
}