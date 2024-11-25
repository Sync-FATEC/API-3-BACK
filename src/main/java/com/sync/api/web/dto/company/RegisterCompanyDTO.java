package com.sync.api.web.dto.company;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterCompanyDTO {
    public String corporateName;
    public String cnpj;
    public String phone;
    public boolean privateCompany;
    public String AddressStreet;
    public String AddressNumber;
    public String AddressNeighborhood;
    public String AddressCity;
    public String AddressState;
    public String AddressZipCode;
}
