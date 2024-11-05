package com.sync.api.web.dto.project;

import com.sync.api.web.dto.documents.DocumentListDTO;

import java.time.LocalDate;
import java.util.List;

public record ProjectDto(
        String projectId,
        String projectReference,
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
) {}
