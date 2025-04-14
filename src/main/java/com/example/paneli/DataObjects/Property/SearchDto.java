package com.example.paneli.DataObjects.Property;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchDto {
    private Long id;
    private String name;
    private String City;
    private String Country;

    public SearchDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public SearchDto(Long id, String name, String city, String country) {
        this.id = id;
        this.name = name;
        City = city;
        Country = country;
    }

    public Long getLoginId(){
        return this.id + 2654435L;
    }
}
