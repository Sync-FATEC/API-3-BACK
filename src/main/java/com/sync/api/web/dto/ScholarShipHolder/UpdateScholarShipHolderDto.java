package com.sync.api.web.dto.ScholarShipHolder;

import com.sync.api.web.dto.address.CreateAddressDTO;
import com.sync.api.web.dto.address.UpdateAddressDTO;
import lombok.Data;

@Data
public class UpdateScholarShipHolderDto {
    public String id;
    public String name;
    public String email;
    public String cpf;
    public String rg;
    public String nationality;

    public UpdateAddressDTO address;
    public String grantId;
}
