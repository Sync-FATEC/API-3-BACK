package com.sync.api.domain.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Duration;

@Entity
@Data
public class Phases {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String description;

    private String phase;

    private Duration duration;

    private boolean isDraft;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;
}
