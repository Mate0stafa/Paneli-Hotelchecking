package com.example.paneli.DataObjects.Admin;

import com.example.paneli.DataObjects.RoleDto;
import com.example.paneli.Models.PanelUsers.Role;
import lombok.*;

import java.util.List;


public interface UserProjection {
    Long getId();
    String getUsername();
    String getFull_name();
    String getEmail();
    Integer getPassword_expired();
    Integer getAccount_locked();
}
