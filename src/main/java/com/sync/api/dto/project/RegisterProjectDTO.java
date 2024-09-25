package com.sync.api.dto.project;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record RegisterProjectDTO(
        @NotBlank String projectReference,
        @NotBlank String nameCoordinator,
        @NotBlank String projectCompany,
        @NotBlank String projectObjective,
        @NotNull String projectDescription,
        @NotNull Float projectValue,
        @NotNull LocalDate projectEndDate,
        @NotNull LocalDate projectStartDate,
        @NotNull String projectClassification
) {}
