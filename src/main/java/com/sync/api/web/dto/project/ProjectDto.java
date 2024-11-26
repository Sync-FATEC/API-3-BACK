package com.sync.api.web.dto.project;

import com.sync.api.domain.model.DraftEditProject;
import com.sync.api.domain.model.Project;
import com.sync.api.web.dto.documents.DocumentListDTO;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public record ProjectDto(
        String projectId,
        String projectReference,
        String projectTitle,
        String nameCoordinator,
        String projectCompany,
        String projectObjective,
        String projectDescription,
        Float projectValue,
        LocalDate projectEndDate,
        LocalDate projectStartDate,
        String projectClassification,
        String projectStatus,
        List<DocumentListDTO> documents,
        List<String> sensitiveFields
) {

    public static ProjectDto fromDraftEditProject(DraftEditProject draft) {
        return new ProjectDto(
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
                draft.getDraftEditProjectClassification().toString(),
                null,
                null,
                draft.getSensitiveFields()
        );
    }


    public static ProjectDto fromProjectWithStatusAndDocNull(Project project) {
        return new ProjectDto(
                project.getProjectId(),
                project.getProjectReference(),
                project.getProjectTitle(),
                project.getCoordinators().coordinatorName,
                project.getCompany().getCorporateName(),
                project.getProjectObjective(),
                project.getProjectDescription(),
                project.getProjectValue(),
                project.getProjectEndDate(),
                project.getProjectStartDate(),
                project.getProjectClassification().toString(),
                null, // projectStatus is not available in DraftEditProject
                null,
                Optional.ofNullable(project.getSensitiveFields()).orElse(Collections.emptyList())
        );
    }
}
