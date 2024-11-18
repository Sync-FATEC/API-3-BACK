package com.sync.api.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sync.api.domain.enums.ProjectClassification;
import com.sync.api.domain.enums.ProjectStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public String projectId;
    public String projectReference;
    public String projectCompany;

    @Lob
    @Column(columnDefinition = "TEXT")
    public String projectObjective;

    @Lob
    @Column(columnDefinition = "TEXT")
    public String projectDescription;
    public Float projectValue;
    public LocalDate projectStartDate;
    public LocalDate projectEndDate;

    @Enumerated(EnumType.STRING)
    public ProjectClassification projectClassification;

    @Enumerated(EnumType.STRING)
    public ProjectStatus projectStatus;

    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE)
    public List<Documents> documents;

    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE)
    public List<HistoryProject> historyProjectList;

    @ElementCollection
    private List<String> sensitiveFields;

    @ManyToOne
    @JoinColumn(name = "coordinator_id")
    public Coordinators coordinators;


    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id")
    public User user;

}

