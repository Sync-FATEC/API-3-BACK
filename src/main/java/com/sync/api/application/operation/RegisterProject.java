package com.sync.api.application.operation;

import com.sync.api.web.dto.project.RegisterProjectDTO;
import com.sync.api.domain.enums.ProjectClassification;
import com.sync.api.domain.enums.ProjectStatus;
import com.sync.api.domain.model.Project;
import com.sync.api.infra.repository.ProjectRepository;
import com.sync.api.application.service.SensitiveFieldUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegisterProject {
    @Autowired
    private ProjectRepository projectRepository;

    public Project registerProject(RegisterProjectDTO projectDto, ProjectStatus projectStatus) {
        Project project = new Project();
        project.setProjectReference(projectDto.projectReference());
        project.setProjectTitle(projectDto.projectTitle());
        project.setCoordinators(projectDto.Coordinator());
        project.setProjectCompany(projectDto.projectCompany());
        project.setProjectObjective(projectDto.projectObjective());
        project.setProjectValue(projectDto.projectValue());
        project.setProjectEndDate(projectDto.projectEndDate());
        project.setProjectStartDate(projectDto.projectStartDate());
        project.setProjectStatus(projectStatus);
        project.setProjectClassification(ProjectClassification.valueOf(projectDto.projectClassification()));
        project.setProjectDescription(projectDto.projectDescription());
        project.setDraft(projectDto.isDraft());

        project.setSensitiveFields(SensitiveFieldUtil.getSensitiveFields(projectDto));

        return projectRepository.save(project);
    }
}
