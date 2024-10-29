package com.sync.api.dto.project;

import com.sync.api.enums.ProjectClassification;
import com.sync.api.enums.ProjectStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Lob;

import java.time.LocalDate;

public record UpdateProjectDto(
        String projectReference,
        boolean projectReferenceSensitive,
        String nameCoordinator,
        boolean nameCoordinatorSensitive,
        String projectCompany,
        boolean projectCompanySensitive,
        @Lob
        @Column(columnDefinition = "TEXT")
        String projectObjective,
        boolean projectObjectiveSensitive,
        @Lob
        @Column(columnDefinition = "TEXT")
        String projectDescription,
        boolean projectDescriptionSensitive,
        Float projectValue,
        boolean projectValueSensitive,
        LocalDate projectEndDate,
        boolean projectEndDateSensitive,
        LocalDate projectStartDate,
        boolean projectStartDateSensitive,
        @Enumerated(EnumType.STRING)
        ProjectClassification projectClassification,
        boolean projectClassificationSensitive,
        @Enumerated(EnumType.STRING)
        ProjectStatus projectStatus,
        boolean projectStatusSensitive
) {
}
