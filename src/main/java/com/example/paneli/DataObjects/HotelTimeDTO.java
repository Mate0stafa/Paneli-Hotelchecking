package com.example.paneli.DataObjects;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class HotelTimeDTO {

    private String check_in;
    private String check_out;
    private int addressDetails;
    private int phoneNumber;
    private int agelimit;
    private int minAge;
    private int maxAge;
    private int Curfew;

    public HotelTimeDTO(String check_in, String check_out, int addressDetails, int phoneNumber, int agelimit, int minAge, int maxAge, int curfew) {
        this.check_in = check_in;
        this.check_out = check_out;
        this.addressDetails = addressDetails;
        this.phoneNumber = phoneNumber;
        this.agelimit = agelimit;
        this.minAge = minAge;
        this.maxAge = maxAge;
        Curfew = curfew;
    }
}
