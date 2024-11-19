package com.sync.api.application.operation;

import com.sync.api.web.dto.project.HistoryProjectDto;
import com.sync.api.web.dto.project.UpdateProjectDto;
import com.sync.api.domain.model.Project;
import com.sync.api.domain.model.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CompareChanges {

    public HistoryProjectDto compare(Project oldProject, UpdateProjectDto updateProjectDto, User user) {
        StringBuilder changesFields = new StringBuilder();
        StringBuilder newValues = new StringBuilder();
        StringBuilder oldValues = new StringBuilder();

        if (!oldProject.getProjectReference().equals(updateProjectDto.projectReference())) {
            changesFields.append("projectReference,");
            oldValues.append(oldProject.getProjectReference()).append(",");
            newValues.append(updateProjectDto.projectReference()).append(",");
        }
        if (!oldProject.getProjectTitle().equals(updateProjectDto.projectTitle())) {
            changesFields.append("projectTitle,");
            oldValues.append(oldProject.getProjectTitle()).append(",");
            newValues.append(updateProjectDto.projectTitle()).append(",");
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
        if (!oldProject.getCoordinators().coordinatorName.equals(updateProjectDto.nameCoordinator())) {
            changesFields.append("nameCoordinator,");
            oldValues.append(oldProject.getCoordinators().coordinatorName).append(",");
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
                LocalDateTime.now(),
                oldProject,
                null,
                user,
                user.getUserEmail()

        );
    }

}
