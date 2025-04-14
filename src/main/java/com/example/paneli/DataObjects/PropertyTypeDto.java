package com.example.paneli.DataObjects;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PropertyTypeDto {

    private int id;
    private String type;
    private String description;
    private long  propertyListSize;

    public PropertyTypeDto(int id, String type, String description, long  propertyListSize) {
        this.id = id;
        this.type = type;
        this.description = description;
        this.propertyListSize = propertyListSize;
    }
}
