package com.example.paneli.DataObjects.Property;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
@NoArgsConstructor
@Getter
@Setter
@ToString
public class VatAndTaxDTO {
    private Date registrationDate;
    private String nuis;
    private Integer taxesIncluded;

    public VatAndTaxDTO( Date registrationDate, Integer taxesIncluded, String nuis) {
        this.registrationDate = registrationDate;
        this.taxesIncluded = taxesIncluded;
        this.nuis = nuis;
    }

}
