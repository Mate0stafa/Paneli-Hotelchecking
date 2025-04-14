package com.example.paneli.DataObjects;

import com.example.paneli.Models.Address;
import com.example.paneli.Models.City;
import lombok.*;

import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AgreementDto {


    private String first_name;
    private String last_name;
    private String email;
    private String phone_number;

    private String property_name;
    private String address;
    private String nuis;
    private String city;
    private String street;
    private String zip_code;
}
