package com.example.paneli.DataObjects;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FlexibleDaysDTO {

    private Long id;
    private String name;

    public FlexibleDaysDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
