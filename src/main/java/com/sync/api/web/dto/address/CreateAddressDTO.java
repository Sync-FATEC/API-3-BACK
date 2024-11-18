package com.sync.api.web.dto.address;

import lombok.Getter;

@Getter
public class CreateAddressDTO {
    private String street;
    private String number;
    private String neighborhood;
    private String city;
    private String state;
    private String zipCode;
}
