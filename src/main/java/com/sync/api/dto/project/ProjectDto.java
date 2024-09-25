package com.sync.api.dto.project;

import com.sync.api.dto.documents.DocumentListDTO;
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

    private String projectId;

    private String projectReference;

    private String nameCoordinator;

    private String projectCompany;

    private String projectObjective;

    private String projectDescription;

    private Float projectValue;

    private LocalDate projectEndDate;

    private LocalDate projectStartDate;

    private String projectClassification;

    private String projectStatus;

    private List<DocumentListDTO> documents;
}