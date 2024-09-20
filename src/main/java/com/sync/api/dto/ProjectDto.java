package com.sync.api.dto;

import com.sync.api.model.Documents;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.EntityModel;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDto extends EntityModel<ProjectDto> {

    @NotBlank
    private String projectId;

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

    @NotNull
    private String projectStatus;

    @NotNull
    private List<Documents> documents;


}