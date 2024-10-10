package com.sync.api.operation;

import com.sync.api.dto.HistoryProjectDto;
import com.sync.api.dto.project.UpdateProjectDto;
import com.sync.api.model.Project;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class CompareChanges {

    public HistoryProjectDto compare(Project oldProject, UpdateProjectDto updateProjectDto) {
        StringBuilder changesFields = new StringBuilder();
        StringBuilder newValues = new StringBuilder();
        StringBuilder oldValues = new StringBuilder();

        if (!oldProject.getProjectReference().equals(updateProjectDto.projectReference())) {
            changesFields.append("projectReference,");
            oldValues.append(oldProject.getProjectReference()).append(",");
            newValues.append(updateProjectDto.projectReference()).append(",");
        }
        if (!oldProject.getProjectCompany().equals(updateProjectDto.projectCompany())) {
            changesFields.append("projectCompany,");
            oldValues.append(oldProject.getProjectCompany()).append(",");
            newValues.append(updateProjectDto.projectCompany()).append(",");
        }
        if (!oldProject.getProjectObjective().equals(updateProjectDto.projectObjective())) {
            changesFields.append("projectObjective,");
            oldValues.append(oldProject.getProjectObjective()).append(",");
            newValues.append(updateProjectDto.projectObjective()).append(",");
        }
        if (!oldProject.getProjectDescription().equals(updateProjectDto.projectDescription())) {
            changesFields.append("projectDescription,");
            oldValues.append(oldProject.getProjectDescription()).append(",");
            newValues.append(updateProjectDto.projectDescription()).append(",");
        }
        if (!oldProject.getNameCoordinator().equals(updateProjectDto.nameCoordinator())) {
            changesFields.append("nameCoordinator,");
            oldValues.append(oldProject.getNameCoordinator()).append(",");
            newValues.append(updateProjectDto.nameCoordinator()).append(",");
        }
        if (!oldProject.getProjectValue().equals(updateProjectDto.projectValue())) {
            changesFields.append("projectValue,");
            oldValues.append(oldProject.getProjectValue()).append(",");
            newValues.append(updateProjectDto.projectValue()).append(",");
        }
        if (!oldProject.getProjectStartDate().equals(updateProjectDto.projectStartDate())) {
            changesFields.append("projectStartDate,");
            oldValues.append(oldProject.getProjectStartDate()).append(",");
            newValues.append(updateProjectDto.projectStartDate()).append(",");
        }
        if (!oldProject.getProjectEndDate().equals(updateProjectDto.projectEndDate())) {
            changesFields.append("projectEndDate,");
            oldValues.append(oldProject.getProjectEndDate()).append(",");
            newValues.append(updateProjectDto.projectEndDate()).append(",");
        }
        if (!oldProject.getProjectClassification().equals(updateProjectDto.projectClassification())) {
            changesFields.append("projectClassification,");
            oldValues.append(oldProject.getProjectClassification()).append(",");
            newValues.append(updateProjectDto.projectClassification()).append(",");
        }
        if (!oldProject.getProjectStatus().equals(updateProjectDto.projectStatus())) {
            changesFields.append("projectStatus,");
            oldValues.append(oldProject.getProjectStatus()).append(",");
            newValues.append(updateProjectDto.projectStatus()).append(",");
        }


        if (changesFields.isEmpty()) {
            return null;
        }

        return new HistoryProjectDto(
                changesFields.toString(),
                newValues.toString(),
                oldValues.toString(),
                oldProject,
                null,
                null
        );
    }

}