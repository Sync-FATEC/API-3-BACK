package com.sync.api.dto.project;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RegisterProjectDTO {

    @NotBlank
    private String projectReference;

    @NotBlank
    private String nameCoordinator;

    @NotBlank
    private String projectCompany;

    @NotBlank
    private String projectObjective;

    @NotNull
    private String projectDescription;

    @NotNull
    private Float projectValue;

    @NotNull
    private LocalDate projectEndDate;

    @NotNull
    private LocalDate projectStartDate;

    @NotNull
    private String projectClassification;
}
