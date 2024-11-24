package com.sync.api.web.dto.company;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCompanyDTO {
    public String id;
    public String corporateName;
    public String cnpj;
    public String phone;
    public boolean privateCompany;
    public String addressId;
    public String addressStreet;
    public String addressNumber;
    private String addressNeighborhood;
    public String addressCity;
    public String addressState;
    public String addressZipCode;
}
