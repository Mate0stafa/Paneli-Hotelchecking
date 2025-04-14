package com.example.paneli.Repositories.clientRepositories;


import com.example.paneli.Models.client.RoleClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleClientRepository extends JpaRepository<RoleClient, Long> {

    @Query("select r from RoleClient r where r.authority = ?1")
    public RoleClient findByAuthority(String authority);

    @Query("select r from RoleClient r where r.id = ?1")
    List<RoleClient> findAllById(Long i);
}
