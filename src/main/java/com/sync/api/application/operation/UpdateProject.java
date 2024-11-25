package com.sync.api.application.operation;

import com.sync.api.domain.model.Company;
import com.sync.api.domain.model.Coordinators;
import com.sync.api.infra.repository.CompanyRepository;
import com.sync.api.infra.repository.CoordinatorsRepository;
import com.sync.api.web.dto.project.UpdateProjectDto;
import com.sync.api.domain.enums.ProjectStatus;
import com.sync.api.domain.model.Project;
import com.sync.api.infra.repository.ProjectRepository;
import com.sync.api.application.service.SensitiveFieldUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UpdateProject {
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private CoordinatorsRepository coordinatorsRepository;
    @Autowired
    private CompanyRepository companyRepository;

    public Project updateProject(UpdateProjectDto updateProjectDto, Project project, ProjectStatus projectStatus){
        if (updateProjectDto.projectReference() != null){
            project.setProjectReference(updateProjectDto.projectReference());
        }
        if(updateProjectDto.projectTitle() != null){
            project.setProjectTitle(updateProjectDto.projectTitle());
        }
        if(updateProjectDto.projectCompany() != null){
            Optional<Company> company = companyRepository.findByCorporateName(updateProjectDto.projectCompany());
            if (company.isEmpty()) {
                throw new IllegalArgumentException("Empresa não encontrada.");
            }
            project.setCompany(company.orElseThrow(() -> new IllegalArgumentException("Empresa não encontrada.")));        }
        if (updateProjectDto.projectObjective() != null){
            project.setProjectObjective(updateProjectDto.projectObjective());
        }
        if(updateProjectDto.projectDescription() != null){
            project.setProjectDescription(updateProjectDto.projectDescription());
        }
        if (updateProjectDto.nameCoordinator() != null){
            Optional<Coordinators> coordinator = coordinatorsRepository.findByCoordinatorName(updateProjectDto.nameCoordinator());
            if (coordinator.isEmpty()) {
                throw new IllegalArgumentException("Coordenador não encontrado.");
            }
            project.setCoordinators(coordinator.orElseThrow(() -> new IllegalArgumentException("Coordenador não encontrado.")));        }
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

        project.setDraft(updateProjectDto.isDraft());

        project.setSensitiveFields(SensitiveFieldUtil.getSensitiveFields(updateProjectDto));

        project.setProjectStatus(projectStatus);
        return projectRepository.save(project);
    }
}
