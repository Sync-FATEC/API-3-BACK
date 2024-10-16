package com.sync.api.operation;

import com.sync.api.dto.project.UpdateProjectDto;
import com.sync.api.enums.ProjectStatus;
import com.sync.api.model.Project;
import com.sync.api.repository.ProjectRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdateProject {
    @Autowired
    private ProjectRepository projectRepository;

    public Project updateProject(UpdateProjectDto updateProjectDto, Project project, ProjectStatus projectStatus){
        if (updateProjectDto.projectReference() != null){
            project.setProjectReference(updateProjectDto.projectReference());
        }
        if(updateProjectDto.projectCompany() != null){
            project.setProjectCompany(updateProjectDto.projectCompany());
        }
        if (updateProjectDto.projectObjective() != null){
            project.setProjectObjective(updateProjectDto.projectObjective());
        }
        if(updateProjectDto.projectDescription() != null){
            project.setProjectDescription(updateProjectDto.projectDescription());
        }
        if (updateProjectDto.nameCoordinator() != null){
            project.setNameCoordinator(updateProjectDto.nameCoordinator());
        }
        if(updateProjectDto.projectValue() != null){
            project.setProjectValue(updateProjectDto.projectValue());
        }
        if(updateProjectDto.projectStartDate() != null){
            project.setProjectStartDate(updateProjectDto.projectStartDate());
        }
        if(updateProjectDto.projectEndDate() != null){
            project.setProjectEndDate(updateProjectDto.projectEndDate());
        }
        if( updateProjectDto.projectClassification() != null){
            project.setProjectClassification(updateProjectDto.projectClassification());
        }
        project.setProjectStatus(projectStatus);
        return projectRepository.save(project);
    }
}
