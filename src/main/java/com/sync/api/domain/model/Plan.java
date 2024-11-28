package com.sync.api.domain.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String description;
    private String total;

    @ManyToOne()
    private WorkPlanCompleteData workPlan;
}
