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
import java.util.stream.Collectors;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private DocumentService documentService;

    public Project createProject(ProjectDto projectDto, List<DocumentUploadDto> documentUploadDtoList) {
            try {
                Project project = new Project();
                project.setProjectReference(projectDto.projectReference());
                project.setNameCoordinator(projectDto.nameCoordinator());
                project.setProjectCompany(projectDto.projectCompany());
                project.setProjectObjective(projectDto.projectObjective());
                project.setProjectValue(projectDto.projectValue());
                project.setProjectEndDate(projectDto.projectEndDate());
                project.setProjectStartDate(projectDto.projectStartDate());
                project.setProjectClassification(ClassificacaoProjetos.valueOf(projectDto.projectClassification()));
                project.setProjectStatus(SituacaoProjetos.valueOf(projectDto.projectStatus()));

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
        try {
            Project project = projectRepository.findById(projectId)
                    .orElseThrow(() -> new IllegalArgumentException("Projeto com o ID " + projectId + " não encontrado"));

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

        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Dados do projeto inválidos: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao ler o projeto: " + e.getMessage(), e);
        }
    }

    public List<ProjectDto> listProjects() {
        try {
            List<ProjectDto> projects = projectRepository.findAll()
                    .stream()
                    .map(project -> new ProjectDto(
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
                    ))
                    .collect(Collectors.toList());

            return projects.isEmpty() ? Collections.emptyList() : projects;

        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Dados do projeto inválidos: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao listar os projetos: " + e.getMessage(), e);
        }
    }

    public List<String> listCoordinators() {
        try {
            List<String> coordinators = projectRepository.findAll()
                    .stream()
                    .map(Project::getNameCoordinator)
                    .distinct()
                    .sorted()
                    .collect(Collectors.toList());

            return coordinators.isEmpty() ? Collections.emptyList() : coordinators;

        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Dados do projeto inválidos: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao listar os coordenadores: " + e.getMessage(), e);
        }
    }

    public List<String> listCompanies() throws SystemContextException {
        try {
            List<String> companies = projectRepository.findAll()
                    .stream()
                    .map(Project::getProjectCompany)
                    .distinct()
                    .sorted()
                    .collect(Collectors.toList());

            return companies.isEmpty() ? Collections.emptyList() : companies;

        } catch (Exception e) {
            throw new SystemContextException(e.getMessage());
        }
    }

    public Project updateProject(String projectId, ProjectDto projectDto) {
        try {
            Project project = projectRepository.findById(projectId)
                    .orElseThrow(() -> new IllegalArgumentException("Projeto com o ID " + projectId + " não encontrado"));

            if (projectDto.projectReference() != null) {
                project.setProjectReference(projectDto.projectReference());
            }
            if (projectDto.nameCoordinator() != null) {
                project.setNameCoordinator(projectDto.nameCoordinator());
            }
            if (projectDto.projectCompany() != null) {
                project.setProjectCompany(projectDto.projectCompany());
            }
            if (projectDto.projectObjective() != null) {
                project.setProjectObjective(projectDto.projectObjective());
            }
            if (projectDto.projectValue() != null) {
                project.setProjectValue(projectDto.projectValue());
            }
            if (projectDto.projectEndDate() != null) {
                project.setProjectEndDate(projectDto.projectEndDate());
            }
            if (projectDto.projectStartDate() != null) {
                project.setProjectStartDate(projectDto.projectStartDate());
            }
            if (projectDto.projectClassification() != null) {
                project.setProjectClassification(ClassificacaoProjetos.valueOf(projectDto.projectClassification()));
            }
            if (projectDto.projectStatus() != null) {
                project.setProjectStatus(SituacaoProjetos.valueOf(projectDto.projectStatus()));
            }

            return projectRepository.save(project);

        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Dados do projeto inválidos: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar o projeto: " + e.getMessage(), e);
        }
    }

    public Boolean deleteProject(String projectId) {
        try {
            Project project = projectRepository.findById(projectId)
                    .orElseThrow(() -> new IllegalArgumentException("Projeto com o ID " + projectId + " não encontrado"));

            projectRepository.deleteById(projectId);
            return true;

        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Dados do projeto inválidos: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao deletar o projeto: " + e.getMessage(), e);
        }
    }

    public byte[] exportProject(String id, String format) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Projeto com o ID " + id + " não encontrado"));

        if (project == null) {
            throw new IllegalArgumentException("Project not found with ID: " + id);
        }

        Exporter exporter;
        if (format.equalsIgnoreCase("pdf")) {
            exporter = new GeneratorPdf();
        } else {
            exporter = new GeneratorExcel();
        }

        return exporter.export(project);
    }

    public Project findProject(String id){
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Projeto com o ID " + id + " não encontrado"));

        if (project == null) {
            throw new IllegalArgumentException("Project not found with ID: " + id);
        }
        return project;
    }
}
