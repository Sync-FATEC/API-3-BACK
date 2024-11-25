package com.sync.api.domain.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "coordinators")
public class Coordinators {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public String coordinatorId;
    public String coordinatorName;
    @Column(unique = true)
    public String coordinatorCPF;
    @Column(unique = true)
    public String coordinatorTelefone;
    public String coordinatorEconomicActivity;
}
