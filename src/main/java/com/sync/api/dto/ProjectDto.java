package com.sync.api.dto;

import com.sync.api.model.Documents;
import com.sync.api.model.ProjectHistory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;


import java.time.LocalDate;
import java.util.List;

public record ProjectDto(
        @NotBlank String projectId,
        @NotBlank String projectReference,
        @NotBlank String nameCoordinator,
        @NotBlank String projectCompany,
        @NotBlank String projectObjective,
        @NotNull String projectDescription,
        @NotNull Float projectValue,
        @NotNull LocalDate projectEndDate,
        @NotNull LocalDate projectStartDate,
        @NotNull String projectClassification,
        @NotNull String projectStatus
) {}