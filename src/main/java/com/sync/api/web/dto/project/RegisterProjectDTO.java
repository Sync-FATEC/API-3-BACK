package com.sync.api.web.dto.project;

import com.sync.api.domain.model.Company;
import com.sync.api.domain.model.Coordinators;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record RegisterProjectDTO(
		@NotBlank String projectReference,
		boolean projectReferenceSensitive,
		@NotBlank String projectTitle,
		boolean projectTitleSensitive,
		@NotBlank String nameCoordinator,
		boolean nameCoordinatorSensitive,
		java.util.Optional<Coordinators> Coordinator,
		boolean CoordinatorSensitive,
		@NotBlank String projectCompany,
		boolean projectCompanySensitive,
		java.util.Optional<Company> company,
		boolean companySensitive,
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
		boolean projectClassificationSensitive,

		boolean isDraft
) {
}


