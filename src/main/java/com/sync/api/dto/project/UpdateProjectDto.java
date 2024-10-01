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
        String nameCoordinator,
        String projectCompany,
        @Lob
        @Column(columnDefinition = "TEXT")
        String projectObjective,
        @Lob
        @Column(columnDefinition = "TEXT")
        String projectDescription,
        Float projectValue,
        LocalDate projectEndDate,
        LocalDate projectStartDate,
        @Enumerated(EnumType.STRING)
        ProjectClassification projectClassification,
        @Enumerated(EnumType.STRING)
        ProjectStatus projectStatus
) {
}
