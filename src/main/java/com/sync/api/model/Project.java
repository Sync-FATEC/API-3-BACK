package com.sync.api.model;


import jakarta.persistence.*;
import lombok.Data;


import java.util.List;

@Entity
@Data
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public String projectId;
    public String projectTitle;
    public String nameCoordinator;
    public String cpfCoordinator;
    public String projectDescription;
    public Double projectValue;
    public String projectStartDate;

    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE)
    public List<Documents> documents;

    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE)
    public List<ProjectHistory> projectHistoryList;
}
