package com.sync.api.application.service;

import com.sync.api.application.operation.UpdateDraftEditProject;
import com.sync.api.domain.model.DraftEditProject;
import com.sync.api.domain.model.Project;
import com.sync.api.domain.model.User;
import com.sync.api.infra.repository.DraftEditProjectRepository;
import com.sync.api.infra.repository.ProjectRepository;
import com.sync.api.web.dto.project.UpdateProjectDto;
import com.sync.api.web.exception.SystemContextException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class DraftEditProjectService {

    @Autowired
    DraftEditProjectRepository draftEditProjectRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    private UpdateDraftEditProject updateDraftEditProject;

    public DraftEditProject getByProjectId(String id) {
        return draftEditProjectRepository.findByProjectId(id).orElse(null);
    }

    public DraftEditProject getById(String id) {
        return draftEditProjectRepository.findById(id).orElse(null);
    }

    public List<DraftEditProject> getAll() {
        return draftEditProjectRepository.findAll();
    }

    public DraftEditProject save(DraftEditProject draftEditProject) {
        return draftEditProjectRepository.save(draftEditProject);
    }

    public DraftEditProject updateDraft(String projectId, UpdateProjectDto updateProjectDto, User user) throws SystemContextException {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new SystemContextException("Projeto com o ID " + projectId + " não encontrado"));

        Optional<DraftEditProject> projectDraftOp = draftEditProjectRepository.findByProjectId(projectId);

        DraftEditProject projectDraft;

        projectDraft = projectDraftOp.orElseGet(() -> DraftEditProject.from(project));

        var newSensitiveFields = SensitiveFieldUtil.getSensitiveFields(updateProjectDto);

        if (newSensitiveFields.equals(projectDraft.getSensitiveFields())) {
        }

        return updateDraftEditProject.update(updateProjectDto, projectDraft);
    }

    public void delete(String id) {
        try {
            draftEditProjectRepository.findById(id)
                    .ifPresent(draftEditProjectRepository::delete);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao deletar projeto", e);
        }
    }

}
