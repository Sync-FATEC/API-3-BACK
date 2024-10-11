package com.sync.api.service;

import com.sync.api.dto.HistoryProjectDto;
import com.sync.api.dto.documents.DocumentListDTO;
import com.sync.api.dto.project.ProjectDto;
import com.sync.api.dto.project.RegisterProjectDTO;
import com.sync.api.dto.project.UpdateProjectDto;
import com.sync.api.enums.ProjectClassification;
import com.sync.api.enums.ProjectStatus;
import com.sync.api.exception.SystemContextException;
import com.sync.api.model.Documents;
import com.sync.api.model.Project;
import com.sync.api.operation.CompareChanges;
import com.sync.api.operation.RegisterHistoryProject;
import com.sync.api.operation.RegisterProject;
import com.sync.api.operation.UpdateProject;
import com.sync.api.operation.contract.Exporter;
import com.sync.api.operation.exporter.GeneratorExcel;
import com.sync.api.operation.exporter.GeneratorPdf;
import com.sync.api.repository.ProjectRepository;
import com.sync.api.specification.ProjectSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
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
    @Autowired
    private RegisterHistoryProject registerHistoryProject;
    @Autowired
    private CompareChanges compareChanges;

    private static final Logger logger = LoggerFactory.getLogger(ProjectService.class);

    public Project createProject(RegisterProjectDTO registerProjectDTO) {
        try {
            var projectStatus = VerifyProjectStatus(registerProjectDTO);
            return registerProject.registerProject(registerProjectDTO, projectStatus);
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

    public List<ProjectDto> listProjectsFiltered(String keyword, LocalDate projectStartDate, LocalDate projectEndDate, ProjectStatus status, ProjectClassification classification) {
        List<Project> projects = projectRepository.findAllByOrderByProjectStartDateDesc();

        String keywordLower = (keyword != null) ? keyword.toLowerCase() : null;

        return projects.stream()
                .filter(project -> (keywordLower == null ||
                        project.getProjectReference().toLowerCase().contains(keywordLower) ||
                        project.getProjectCompany().toLowerCase().contains(keywordLower) ||
                        project.getNameCoordinator().toLowerCase().contains(keywordLower)))
                .filter(project -> (projectStartDate == null || !project.getProjectStartDate().isBefore(projectStartDate)))
                .filter(project -> (projectEndDate == null || !project.getProjectEndDate().isAfter(projectEndDate)))
                .filter(project -> (status == null || project.getProjectStatus() == status))
                .filter(project -> (classification == null || project.getProjectClassification() == classification))
                .map(this::mapProjectToDto)
                .collect(Collectors.toList());
    }


    public List<ProjectDto> listProjectsNearEnd() {
        LocalDate startOfWeek = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        return projectRepository.findProjectsEndingThisWeek(startOfWeek,endOfWeek).stream()
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
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Projeto com o ID " + projectId + " não encontrado"
                ));

        HistoryProjectDto historyProjectDto = compareChanges.compare(project,updateProjectDto);

        if (historyProjectDto == null) {
            return project;
        }

        Project newValues = updateProject.updateProject(updateProjectDto, project);

        registerHistoryProject.registerLog(historyProjectDto);

        return newValues;
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
                .filter(project -> project.getProjectStartDate() != null)
                .sorted(Comparator.comparing(Project::getProjectStartDate).reversed())
                .map(this::mapProjectToDto)
                .collect(Collectors.toList());
    }



    public Project findProject(String id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Projeto com o ID " + id + " não encontrado"));
    }

    @Scheduled(cron = "0 0 0 * * *", zone = "America/Sao_Paulo")
    public void VerifyAllProjects() {
        logger.info("Verificação de status dos projetos iniciada.");
        List<Project> projects = projectRepository.findAll();
        for (Project project : projects) {
            if (project.getProjectStartDate() != null || project.getProjectEndDate() != null) {
                ProjectStatus projectStatus = VerifyProjectStatus(project);
                project.setProjectStatus(projectStatus);
                projectRepository.save(project);
            } else {
                logger.warn("Projeto com ID {} ignorado: data de início não disponível.", project.getProjectId());
            }
        }
        logger.info("Verificação de status dos projetos concluída.");
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
                document.getUploadedAt(),
                document.isRemoved()
        );
    }

    private ProjectStatus VerifyProjectStatus(RegisterProjectDTO project) {
        if (project.projectStartDate().isAfter(LocalDate.now())) {
            return ProjectStatus.NAO_INICIADOS;
        } else if (project.projectEndDate().isBefore(LocalDate.now())) {
            return ProjectStatus.FINALIZADOS;
        } else {
            return ProjectStatus.EM_ANDAMENTO;
        }
    }

    private ProjectStatus VerifyProjectStatus(Project project) {
        if (project.projectStartDate.isAfter(LocalDate.now())) {
            return ProjectStatus.NAO_INICIADOS;
        } else if (project.projectEndDate.isBefore(LocalDate.now())) {
            return ProjectStatus.FINALIZADOS;
        } else {
            return ProjectStatus.EM_ANDAMENTO;
        }
    }
}