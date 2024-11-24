package com.sync.api.web.dto.coordinators;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCoordinatorsDTO {
    public String coordinatorId;
    public String coordinatorName;
    public String coordinatorCPF;
    public String coordinatorTelefone;
    public String coordinatorEconomicActivity;
}
