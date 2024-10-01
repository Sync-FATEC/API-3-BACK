package com.sync.api.model;


import com.sync.api.enums.ProjectClassification;
import com.sync.api.enums.ProjectStatus;
import jakarta.persistence.*;
import lombok.Data;


import java.time.LocalDate;
import java.time.LocalDateTime;
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
    public String nameCoordinator;
    public Float projectValue;
    public LocalDate projectStartDate;
    public LocalDate projectEndDate;
    @Enumerated(EnumType.STRING)
    public ProjectClassification projectClassification;
    @Enumerated(EnumType.STRING)
    public ProjectStatus projectStatus;

    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE)
    public List<Documents> documents;
}
