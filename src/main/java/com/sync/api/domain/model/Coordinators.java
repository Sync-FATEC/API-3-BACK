package com.sync.api.domain.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Coordinators {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public String coordinatorId;
    public String coordinatorName;
    @Column(unique = true)
    public String coordinatorRG;
    @Column(unique = true)
    public String coordinatorCPF;
    @Column(unique = true)
    public String coordinatorTelefone;
    public String coordinatorNacionality;
    public String coordinatorMaritalStatus;
    public String coordinatorEconomicActivity;

    @OneToOne
    @JoinColumn(name = "coordinator_address_id")
    private Address address;
}
