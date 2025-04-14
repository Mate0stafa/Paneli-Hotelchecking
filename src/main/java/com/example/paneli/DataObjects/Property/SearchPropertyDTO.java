package com.example.paneli.DataObjects.Property;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchPropertyDTO {
    private Long id;
    private String name;
    private String status;
    private String city;

    public SearchPropertyDTO(Long id, String name, String status, String city) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.city = city;
    }

    public Long getLoginId(){
        return this.id + 2654435L;
    }
}
