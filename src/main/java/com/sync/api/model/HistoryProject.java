package com.sync.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
@Entity
@Data
public class HistoryProject {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public String historyId;
    @Lob
    @Column(columnDefinition = "TEXT")
    public String changedFields;
    @Lob
    @Column(columnDefinition = "TEXT")
    public String newValues;
    @Lob
    @Column(columnDefinition = "TEXT")
    public String oldValues;
    public LocalDate changeDate;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id")
    public User user;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "project_id", nullable = true)
    public Project project;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "documents_id",nullable = true)
    public Documents documents;
}
