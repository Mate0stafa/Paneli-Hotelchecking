package com.example.paneli.DataObjects;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CategoryDTO {

    private int id;
    private String type;

    public CategoryDTO(int id, String type) {
        this.id = id;
        this.type = type;
    }
}
