package com.example.paneli.Repositories.clientRepositories;

import com.example.paneli.Models.client.UserClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserClientRepository extends JpaRepository<UserClient, Long> {

    @Query("select u from UserClient u where u.username = :username")
    public UserClient getUserClientByUsername(@Param("username") String username);

}