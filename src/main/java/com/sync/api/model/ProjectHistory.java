package com.sync.api.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;


@Entity
@Data
public class ProjectHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public String historyId;
    public String changeFields;
    public String newValues;
    public String oldValues;
    public LocalDate changeDate;

    @ManyToOne
    @JoinColumn(name = "projectId")
    public Project project;

    @ManyToOne
    @JoinColumn(name = "userId")
    public User user;
}
