package com.sync.api.web.dto.coordinators;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterCoordinatorsDTO {
    public String coordinatorName;
    public String coordinatorCPF;
    public String coordinatorTelefone;
    public String coordinatorEconomicActivity;
    public String coordinatorRG;
    public String coordinatorNacionality;
    public String coordinatorMaritalStatus;
    public String AddressStreet;
    public String AddressNumber;
    public String AddressNeighborhood;
    public String AddressCity;
    public String AddressState;
    public String AddressZipCode;
}