package com.sync.api.application.operation;

import com.sync.api.domain.model.Coordinators;
import com.sync.api.infra.repository.CoordinatorsRepository;
import com.sync.api.web.dto.project.UpdateProjectDto;
import com.sync.api.domain.enums.ProjectStatus;
import com.sync.api.domain.model.Project;
import com.sync.api.infra.repository.ProjectRepository;
import com.sync.api.application.service.SensitiveFieldUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdateProject {
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private CoordinatorsRepository coordinatorsRepository;

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
            Coordinators coordinator = coordinatorsRepository.findByCoordinatorName(updateProjectDto.nameCoordinator());
            if (coordinator == null) {
                throw new IllegalArgumentException("Coordenador não encontrado.");
            }
            project.setCoordinators(coordinator);
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

        project.setSensitiveFields(SensitiveFieldUtil.getSensitiveFields(updateProjectDto));

        project.setProjectStatus(projectStatus);
        return projectRepository.save(project);
    }
}
