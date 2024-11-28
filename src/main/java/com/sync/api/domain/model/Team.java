package com.sync.api.domain.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;
    private String institution;
    private String competence;

    @ManyToOne
    private WorkPlanCompleteData workPlan;

}
