package com.sync.api.model;


import jakarta.persistence.*;
import lombok.Data;


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
    public String cpfCoordinator;
    public Double projectValue;
    public String projectStartDate;
    public String projectEndDate;

    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE)
    public List<Documents> documents;

    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE)
    public List<ProjectHistory> projectHistoryList;
}
