package com.sync.api.domain.model;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String activity;
    private String physicalIndicator;
    private String startDate;
    private String endDate;

    @ManyToOne
    private WorkPlanCompleteData workPlan;
}
