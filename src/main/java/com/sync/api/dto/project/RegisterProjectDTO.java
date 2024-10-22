package com.sync.api.dto.project;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public record RegisterProjectDTO(
        @NotBlank String projectReference,
        boolean projectReferenceSensitive,
        @NotBlank String nameCoordinator,
        boolean nameCoordinatorSensitive,
        @NotBlank String projectCompany,
        boolean projectCompanySensitive,
        @NotBlank String projectObjective,
        boolean projectObjectiveSensitive,
        @NotNull String projectDescription,
        boolean projectDescriptionSensitive,
        @NotNull Float projectValue,
        boolean projectValueSensitive,
        @NotNull LocalDate projectEndDate,
        boolean projectEndDateSensitive,
        @NotNull LocalDate projectStartDate,
        boolean projectStartDateSensitive,
        @NotNull String projectClassification,
        boolean projectClassificationSensitive
) {}


