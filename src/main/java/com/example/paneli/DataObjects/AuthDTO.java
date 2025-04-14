package com.example.paneli.DataObjects;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AuthDTO {

    private Integer preAuthorizeCards;
    private Integer preAuthorizePolicy;
    private Integer amountHold;
    private Integer flexiblePolicies;

    public AuthDTO(Integer preAuthorizeCards, Integer preAuthorizePolicy, Integer amountHold, Integer flexiblePolicies) {
        this.preAuthorizeCards = preAuthorizeCards;
        this.preAuthorizePolicy = preAuthorizePolicy;
        this.amountHold = amountHold;
        this.flexiblePolicies = flexiblePolicies;
    }
}
