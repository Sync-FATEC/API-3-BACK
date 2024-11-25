package com.sync.api.web.dto.project;

import com.sync.api.domain.model.DraftEditProject;
import com.sync.api.domain.model.Project;
import com.sync.api.web.dto.documents.DocumentListDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectDraftEditDTO {
    private String projectId;
    private String projectReference;
    private String projectTitle;
    private String nameCoordinator;
    private String projectCompany;
    private String projectObjective;
    private String projectDescription;
    private Float projectValue;
    private LocalDate projectEndDate;
    private LocalDate projectStartDate;
    private String projectClassification;
    private String projectStatus;
    private List<DocumentListDTO> documents;
    private List<String> sensitiveFields;

    // Constructor
    public ProjectDraftEditDTO(String projectId, String projectReference, String projectTitle, String nameCoordinator,
                               String projectCompany, String projectObjective, String projectDescription, Float projectValue,
                               LocalDate projectEndDate, LocalDate projectStartDate, String projectClassification,
                               String projectStatus, List<DocumentListDTO> documents, List<String> sensitiveFields) {
        this.projectId = projectId;
        this.projectReference = projectReference;
        this.projectTitle = projectTitle;
        this.nameCoordinator = nameCoordinator;
        this.projectCompany = projectCompany;
        this.projectObjective = projectObjective;
        this.projectDescription = projectDescription;
        this.projectValue = projectValue;
        this.projectEndDate = projectEndDate;
        this.projectStartDate = projectStartDate;
        this.projectClassification = projectClassification;
        this.projectStatus = projectStatus;
        this.documents = documents;
        this.sensitiveFields = sensitiveFields;
    }

    // Static method to create ProjectDraftEditDTO from DraftEditProject and Project
    public static ProjectDraftEditDTO fromDraft(DraftEditProject draft, Project project) {
        return new ProjectDraftEditDTO(
                draft.getDraftEditProjectId(),
                draft.getDraftEditProjectReference(),
                draft.getDraftEditTitle(),
                draft.getDraftEditNameCoordinator(),
                draft.getDraftEditProjectCompany(),
                draft.getDraftEditProjectObjective(),
                draft.getDraftEditProjectDescription(),
                draft.getDraftEditProjectValue(),
                draft.getDraftEditProjectEndDate(),
                draft.getDraftEditProjectStartDate(),
                draft.getDraftEditProjectClassification().name(),
                project.getProjectStatus().name(),
                project.getDocuments().stream()
                        .map(d -> new DocumentListDTO(
                                d.getDocuments_id(),
                                d.getFileName(),
                                d.getFileType(),
                                d.getFileUrl(),
                                d.getUploadedAt(),
                                d.isRemoved()))
                        .collect(Collectors.toList()),
                project.getSensitiveFields()
        );
    }

    // Getters and setters (if needed)
}