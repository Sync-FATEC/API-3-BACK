package com.sync.api.web.dto.address;

import lombok.Getter;

@Getter
public class UpdateAddressDTO {
    private String id;
    private String street;
    private String number;
    private String neighborhood;
    private String city;
    private String state;
    private String zipCode;
}
