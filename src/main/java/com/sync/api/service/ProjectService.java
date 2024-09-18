package com.sync.api.service;

import com.sync.api.dto.DocumentUploadDto;
import com.sync.api.dto.ProjectDto;
import com.sync.api.enums.ClassificacaoProjetos;
import com.sync.api.enums.SituacaoProjetos;
import com.sync.api.exception.SystemContextException;
import com.sync.api.model.Documents;
import com.sync.api.model.Project;
import com.sync.api.operation.contract.Exporter;
import com.sync.api.operation.exporter.GeneratorExcel;
import com.sync.api.operation.exporter.GeneratorPdf;
import com.sync.api.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final DocumentService documentService;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, DocumentService documentService) {
        this.projectRepository = projectRepository;
        this.documentService = documentService;
    }

    public Project createProject(ProjectDto projectDto, List<DocumentUploadDto> documentUploadDtoList) {
        try {
            Project project = new Project();
            mapDtoToProject(projectDto, project);

            if (documentUploadDtoList != null && !documentUploadDtoList.isEmpty()) {
                for (DocumentUploadDto documentDto : documentUploadDtoList) {
                    Documents document = documentService.createDocument(documentDto, project);
                    project.getDocuments().add(document);
                }
            }

            return projectRepository.save(project);

        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Dados do projeto inválidos: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar o projeto: " + e.getMessage(), e);
        }
    }

    public ProjectDto readProject(String projectId) {
        return projectRepository.findById(projectId)
                .map(this::mapProjectToDto)
                .orElseThrow(() -> new IllegalArgumentException("Projeto com o ID " + projectId + " não encontrado"));
    }

    public List<ProjectDto> listProjects() {
        return projectRepository.findAll().stream()
                .map(this::mapProjectToDto)
                .collect(Collectors.toList());
    }

    public List<String> listCoordinators() {
        return projectRepository.findAll().stream()
                .map(Project::getNameCoordinator)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    public List<String> listCompanies() throws SystemContextException {
        try {
            return projectRepository.findAll().stream()
                    .map(Project::getProjectCompany)
                    .distinct()
                    .sorted()
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new SystemContextException("Erro ao listar empresas: " + e.getMessage());
        }
    }

    public Project updateProject(String projectId, ProjectDto projectDto) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Projeto com o ID " + projectId + " não encontrado"));

        updateProjectFromDto(projectDto, project);
        return projectRepository.save(project);
    }

    public Boolean deleteProject(String projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Projeto com o ID " + projectId + " não encontrado"));

        projectRepository.delete(project);
        return true;
    }

    public byte[] exportProject(String id, String format) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Projeto com o ID " + id + " não encontrado"));

        Exporter exporter = switch (format.toLowerCase()) {
            case "pdf" -> new GeneratorPdf();
            case "excel" -> new GeneratorExcel();
            default -> throw new IllegalArgumentException("Formato inválido: " + format);
        };

        return exporter.export(project);
    }

    public Project findProject(String id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Projeto com o ID " + id + " não encontrado"));
    }

    private void mapDtoToProject(ProjectDto projectDto, Project project) {
        project.setProjectReference(projectDto.getProjectReference());
        project.setNameCoordinator(projectDto.getNameCoordinator());
        project.setProjectCompany(projectDto.getProjectCompany());
        project.setProjectObjective(projectDto.getProjectObjective());
        project.setProjectValue(projectDto.getProjectValue());
        project.setProjectEndDate(projectDto.getProjectEndDate());
        project.setProjectStartDate(projectDto.getProjectStartDate());
        project.setProjectClassification(ClassificacaoProjetos.valueOf(projectDto.getProjectClassification()));
        project.setProjectStatus(SituacaoProjetos.valueOf(projectDto.getProjectStatus()));
    }

    private ProjectDto mapProjectToDto(Project project) {
        return new ProjectDto(
                project.getProjectId(),
                project.getProjectReference(),
                project.getNameCoordinator(),
                project.getProjectCompany(),
                project.getProjectObjective(),
                project.getProjectDescription(),
                project.getProjectValue(),
                project.getProjectEndDate(),
                project.getProjectStartDate(),
                project.getProjectClassification().toString(),
                project.getProjectStatus().toString()
        );
    }

    private void updateProjectFromDto(ProjectDto projectDto, Project project) {
        Optional.ofNullable(projectDto.getProjectReference()).ifPresent(project::setProjectReference);
        Optional.ofNullable(projectDto.getNameCoordinator()).ifPresent(project::setNameCoordinator);
        Optional.ofNullable(projectDto.getProjectCompany()).ifPresent(project::setProjectCompany);
        Optional.ofNullable(projectDto.getProjectObjective()).ifPresent(project::setProjectObjective);
        Optional.ofNullable(projectDto.getProjectValue()).ifPresent(project::setProjectValue);
        Optional.ofNullable(projectDto.getProjectEndDate()).ifPresent(project::setProjectEndDate);
        Optional.ofNullable(projectDto.getProjectStartDate()).ifPresent(project::setProjectStartDate);
        Optional.ofNullable(projectDto.getProjectClassification())
                .ifPresent(value -> project.setProjectClassification(ClassificacaoProjetos.valueOf(value)));
        Optional.ofNullable(projectDto.getProjectStatus())
                .ifPresent(value -> project.setProjectStatus(SituacaoProjetos.valueOf(value)));
    }
}
