package com.sync.api.domain.model;

import com.sync.api.domain.enums.ProjectClassification;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
public class DraftEditProject {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public String draftEditProjectId;
    public String draftEditProjectReference;
    public String draftEditProjectCompany;

    @Lob
    @Column(columnDefinition = "TEXT")
    public String draftEditProjectObjective;

    @Lob
    @Column(columnDefinition = "TEXT")
    public String draftEditProjectDescription;

    public Float draftEditProjectValue;
    public LocalDate draftEditProjectStartDate;
    public LocalDate draftEditProjectEndDate;

    @Enumerated(EnumType.STRING)
    public ProjectClassification draftEditProjectClassification;

    @ElementCollection
    private List<String> sensitiveFields;

    @OneToOne
    @JoinColumn(name = "project_id")
    public Project project;

    public static DraftEditProject from(Project project) {
        DraftEditProject draftEditProject = new DraftEditProject();
        draftEditProject.draftEditProjectReference = project.projectReference;
        draftEditProject.draftEditProjectCompany = project.company.getCorporateName();
        draftEditProject.draftEditProjectObjective = project.projectObjective;
        draftEditProject.draftEditProjectDescription = project.projectDescription;
        draftEditProject.draftEditProjectValue = project.projectValue;
        draftEditProject.draftEditProjectStartDate = project.projectStartDate;
        draftEditProject.draftEditProjectEndDate = project.projectEndDate;
        draftEditProject.draftEditProjectClassification = project.projectClassification;
        draftEditProject.sensitiveFields = project.getSensitiveFields();
        draftEditProject.project = project;
        return draftEditProject;
    }
}
