package com.example.paneli.Repositories;

import com.example.paneli.Models.UserLoginAttempts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserLoginAttemptsRepository extends JpaRepository<UserLoginAttempts, Long> {

    @Query("SELECT u FROM UserLoginAttempts u WHERE u.username = :username")
    UserLoginAttempts findByUsername(@Param("username") String username);

}
