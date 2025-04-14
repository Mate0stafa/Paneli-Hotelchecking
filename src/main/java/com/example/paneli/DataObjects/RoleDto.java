package com.example.paneli.DataObjects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RoleDto {
    private String authority;

    public RoleDto(String authority) {
        this.authority = authority;
    }
}
