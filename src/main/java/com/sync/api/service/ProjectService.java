package com.sync.api.service;

import com.sync.api.dto.documents.DocumentListDTO;
import com.sync.api.dto.documents.DocumentUploadDto;
import com.sync.api.dto.project.ProjectDto;
import com.sync.api.dto.project.RegisterProjectDTO;
import com.sync.api.dto.project.UpdateProjectDto;
import com.sync.api.enums.ProjectClassification;
import com.sync.api.enums.ProjectStatus;
import com.sync.api.exception.SystemContextException;
import com.sync.api.model.Documents;
import com.sync.api.model.Project;
import com.sync.api.operation.RegisterProject;
import com.sync.api.operation.UpdateProject;
import com.sync.api.operation.contract.Exporter;
import com.sync.api.operation.exporter.GeneratorExcel;
import com.sync.api.operation.exporter.GeneratorPdf;
import com.sync.api.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private  DocumentService documentService;
    @Autowired
    private RegisterProject registerProject;
    @Autowired
    private UpdateProject updateProject;


    public Project createProject(RegisterProjectDTO registerProjectDTO) {
        try {
            return registerProject.registerProject(registerProjectDTO);
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
        return projectRepository.findAllByOrderByProjectStartDateDesc().stream()
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

    public Project updateProject(String projectId, UpdateProjectDto updateProjectDto) {
        try {
            Project project = projectRepository.findById(projectId)
                    .orElseThrow(() -> new IllegalArgumentException("Projeto com o ID " + projectId + " não encontrado"));
            return  updateProject.updateProject(updateProjectDto, project);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao editar o projeto: " + e.getMessage(), e);
        }
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

    public List<ProjectDto> filterProjects(String projectReference, String projectCompany, String nameCoordinator,
                                           ProjectClassification projectClassification, ProjectStatus projectStatus,
                                           LocalDate projectStartDate, LocalDate projectEndDate) {
        List<Project> projects = projectRepository.filterProjects(
                projectReference, projectCompany, nameCoordinator,
                projectClassification, projectStatus,
                projectStartDate, projectEndDate
        );

        return projects.stream()
                .map(this::mapProjectToDto)
                .collect(Collectors.toList());
    }

    public Project findProject(String id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Projeto com o ID " + id + " não encontrado"));
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
                project.getProjectStatus().toString(),
                project.getDocuments() != null
                        ? project.getDocuments().stream().map(this::mapDocToDTO).collect(Collectors.toList())
                        : Collections.emptyList()
        );
    }


    private DocumentListDTO mapDocToDTO(Documents document){
        return new DocumentListDTO(
            document.getDocuments_id(),
            document.getFileName(),
            document.getFileType(),
            document.getFileUrl(),
            document.getUploadedAt()
        );
    }

}
