package com.sync.api.web.dto.ScholarShipHolder;

import com.sync.api.web.dto.address.CreateAddressDTO;
import lombok.Data;

@Data
public class RegisterScholarShipHolderDto {
    public String name;
    public String email;
    public String cpf;
    public String rg;
    public String nationality;

    public CreateAddressDTO address;
    public String grantId;
    public String projectId;
}
