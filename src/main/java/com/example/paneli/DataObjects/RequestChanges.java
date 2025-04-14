package com.example.paneli.DataObjects;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RequestChanges {

    private String streetName;
    private Long cityId;
    private String cityName;
    private String zipCode;
    private String propertyName;
}
