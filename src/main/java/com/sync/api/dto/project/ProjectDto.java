package com.sync.api.dto.project;

import com.sync.api.dto.documents.DocumentListDTO;
import com.sync.api.dto.documents.DocumentUploadDto;

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
