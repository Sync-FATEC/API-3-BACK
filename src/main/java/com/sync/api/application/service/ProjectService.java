package com.sync.api.application.service;

import com.sync.api.application.operation.*;
import com.sync.api.domain.model.*;
import com.sync.api.infra.repository.CoordinatorsRepository;
import com.sync.api.infra.repository.DraftEditProjectRepository;
import com.sync.api.web.dto.project.HistoryProjectDto;
import com.sync.api.web.dto.documents.DocumentListDTO;
import com.sync.api.web.dto.project.ProjectDto;
import com.sync.api.web.dto.project.RegisterProjectDTO;
import com.sync.api.web.dto.project.UpdateProjectDto;
import com.sync.api.domain.enums.ProjectClassification;
import com.sync.api.domain.enums.ProjectStatus;
import com.sync.api.web.exception.SystemContextException;
import com.sync.api.application.operation.contract.Exporter;
import com.sync.api.application.operation.exporter.GeneratorExcel;
import com.sync.api.application.operation.exporter.GeneratorPdf;
import com.sync.api.infra.repository.HistoryProjectRepository;
import com.sync.api.infra.repository.ProjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class ProjectService {

    @Autowired
    private AuthenticationService authService;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private HistoryProjectRepository historyProjectRepository;
    @Autowired
    private RegisterProject registerProject;
    @Autowired
    private UpdateProject updateProject;
    @Autowired
    private UpdateDraftEditProject updateDraftEditProject;
    @Autowired
    private RegisterHistoryProject registerHistoryProject;
    @Autowired
    private DraftEditProjectRepository draftEditProjectRepository;
    @Autowired
    private CompareChanges compareChanges;
    @Autowired
    private CoordinatorsRepository coordinatorsRepository;
    private static final Logger logger = LoggerFactory.getLogger(ProjectService.class);

    public Project createProject(RegisterProjectDTO registerProjectDTO) {
        try {
            var projectStatus = VerifyProjectStatus(registerProjectDTO.projectStartDate(), registerProjectDTO.projectEndDate());

            RegisterProjectDTO registerProjectWithCoordinator = registerCoordinator(registerProjectDTO);

            return registerProject.registerProject(registerProjectWithCoordinator, projectStatus);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Dados do projeto inválidos: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar o projeto: " + e.getMessage(), e);
        }
    }

    private RegisterProjectDTO registerCoordinator(RegisterProjectDTO registerProjectDTO) {
        Coordinators coordinator = coordinatorsRepository.findByCoordinatorName(registerProjectDTO.nameCoordinator());
        if (coordinator == null) {
            throw new IllegalArgumentException("Coordenador não encontrado: " + registerProjectDTO.nameCoordinator());
        }

        RegisterProjectDTO dtoWithCoordinator = new RegisterProjectDTO(
                registerProjectDTO.projectReference(),
                registerProjectDTO.projectReferenceSensitive(),
                registerProjectDTO.nameCoordinator(),
                registerProjectDTO.nameCoordinatorSensitive(),
                coordinator,
                registerProjectDTO.projectCompany(),
                registerProjectDTO.projectCompanySensitive(),
                registerProjectDTO.projectObjective(),
                registerProjectDTO.projectObjectiveSensitive(),
                registerProjectDTO.projectDescription(),
                registerProjectDTO.projectDescriptionSensitive(),
                registerProjectDTO.projectValue(),
                registerProjectDTO.projectValueSensitive(),
                registerProjectDTO.projectEndDate(),
                registerProjectDTO.projectEndDateSensitive(),
                registerProjectDTO.projectStartDate(),
                registerProjectDTO.projectStartDateSensitive(),
                registerProjectDTO.projectClassification(),
                registerProjectDTO.projectClassificationSensitive()
        );

        return dtoWithCoordinator;
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

    public List<String> listCoordinators() throws SystemContextException {
        try {
            return coordinatorsRepository.findAll().stream()
                    .map(Coordinators::getCoordinatorName)
                    .distinct()
                    .sorted()
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new SystemContextException("Erro ao listar coordenadores: " + e.getMessage());
        }
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

    public Project updateProject(String projectId, UpdateProjectDto updateProjectDto, User user) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Projeto com o ID " + projectId + " não encontrado"
                ));


        HistoryProjectDto historyProjectDto = compareChanges.compare(project,updateProjectDto, user);

        var newSensitiveFields = SensitiveFieldUtil.getSensitiveFields(updateProjectDto);

        if (historyProjectDto == null && newSensitiveFields.equals(project.getSensitiveFields())) {
            return project;
        }

        var projectStatus = VerifyProjectStatus(updateProjectDto.projectStartDate(), updateProjectDto.projectEndDate());
        Project projectUpdated = updateProject.updateProject(updateProjectDto, project, projectStatus);



        registerHistoryProject.registerLog(historyProjectDto);

        return projectUpdated;
    }

    public DraftEditProject UpdateDraft(String projectId, UpdateProjectDto updateProjectDto, User user)
    {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new IllegalArgumentException("Projeto com o ID " + projectId + " não encontrado"));

        Optional<DraftEditProject> projectDraftOp = draftEditProjectRepository.findById(projectId);

        DraftEditProject projectDraft;

        projectDraft = projectDraftOp.orElseGet(() -> DraftEditProject.from(project));

        var newSensitiveFields = SensitiveFieldUtil.getSensitiveFields(updateProjectDto);

        if (newSensitiveFields.equals(project.getSensitiveFields())) {
            return projectDraft;
        }

        return updateDraftEditProject.update(updateProjectDto, projectDraft);
    }


    public List<HistoryProjectDto> listHistoryChanges(String projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Projeto com o ID " + projectId + " não encontrado"));

        return historyProjectRepository.findByProject_ProjectIdOrderByChangeDateDesc(projectId).stream()
                .map(this::mapHistoryProjectToDto)
                .collect(Collectors.toList());
    }

    private HistoryProjectDto mapHistoryProjectToDto(HistoryProject historyProject) {
        return new HistoryProjectDto(
                historyProject.getChangedFields(),
                historyProject.getNewValues(),
                historyProject.getOldValues(),
                historyProject.getChangeDate(),
                historyProject.getProject(),
                historyProject.getDocuments(),
                historyProject.getUser(),
                historyProject.getUser().getUserEmail()
        );
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
                ProjectStatus projectStatus = VerifyProjectStatus(project.projectStartDate, project.projectEndDate);
                project.setProjectStatus(projectStatus);
                projectRepository.save(project);
            } else {
                logger.warn("Projeto com ID {} ignorado: data de início não disponível.", project.getProjectId());
            }
        }
        logger.info("Verificação de status dos projetos concluída.");
    }




    private ProjectDto mapProjectToDto(Project project) {
        project = RemoveSensitiveData(project);
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
                        : Collections.emptyList(),
                project.getSensitiveFields()
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

    private ProjectStatus VerifyProjectStatus(LocalDate projectStartDate, LocalDate projectEndDate) {
        if (projectStartDate.isAfter(LocalDate.now())) {
            return ProjectStatus.NAO_INICIADOS;
        } else if (projectEndDate.isBefore(LocalDate.now())) {
            return ProjectStatus.FINALIZADOS;
        } else {
            return ProjectStatus.EM_ANDAMENTO;
        }
    }

    private Project RemoveSensitiveData(Project project) {
        if (!authService.verifyLoggedIn()) {
            if (!project.getSensitiveFields().isEmpty()) {
                // Itera sobre todos os campos do projeto
                for (Field field : project.getClass().getDeclaredFields()) {
                    field.setAccessible(true);
                    // Se o campo for sensível e o usuário não for admin, seta o campo como nulo
                    if (project.getSensitiveFields().contains(field.getName())) {
                        try {
                            field.set(project, null);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } else {
            return project;
        }
        return project;
    }
}