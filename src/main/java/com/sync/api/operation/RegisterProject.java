package com.sync.api.operation;

import com.sync.api.dto.documents.DocumentListDTO;
import com.sync.api.dto.documents.DocumentUploadDto;
import com.sync.api.dto.project.ProjectDto;
import com.sync.api.dto.project.RegisterProjectDTO;
import com.sync.api.enums.ProjectClassification;
import com.sync.api.enums.ProjectStatus;
import com.sync.api.model.Documents;
import com.sync.api.model.Project;
import com.sync.api.repository.DocumentRepository;
import com.sync.api.repository.ProjectRepository;
import com.sync.api.service.DocumentService;
import com.sync.api.service.SensitiveFieldUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegisterProject {
    @Autowired
    private ProjectRepository projectRepository;

    public Project registerProject(RegisterProjectDTO projectDto, ProjectStatus projectStatus) {
        Project project = new Project();
        project.setProjectReference(projectDto.projectReference());
        project.setNameCoordinator(projectDto.nameCoordinator());
        project.setProjectCompany(projectDto.projectCompany());
        project.setProjectObjective(projectDto.projectObjective());
        project.setProjectValue(projectDto.projectValue());
        project.setProjectEndDate(projectDto.projectEndDate());
        project.setProjectStartDate(projectDto.projectStartDate());
        project.setProjectStatus(projectStatus);
        project.setProjectClassification(ProjectClassification.valueOf(projectDto.projectClassification()));
        project.setProjectDescription(projectDto.projectDescription());

        project.setSensitiveFields(SensitiveFieldUtil.getSensitiveFields(projectDto));

        return projectRepository.save(project);
    }
}
