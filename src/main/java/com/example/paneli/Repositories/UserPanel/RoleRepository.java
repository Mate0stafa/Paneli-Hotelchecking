package com.example.paneli.Repositories.UserPanel;

import com.example.paneli.Models.PanelUsers.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;

public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query("select r from Role r where r.authority = ?1")
    public ArrayList<Role> findAllByAuthority(String authority);
}
