package com.sync.api.web.controller;

import com.sync.api.web.dto.project.HistoryProjectDto;
import com.sync.api.web.dto.project.ProjectDto;
import com.sync.api.web.dto.project.RegisterProjectDTO;
import com.sync.api.web.dto.project.UpdateProjectDto;
import com.sync.api.web.dto.web.ResponseModelDTO;
import com.sync.api.domain.enums.ProjectClassification;
import com.sync.api.domain.enums.ProjectStatus;
import com.sync.api.web.exception.SystemContextException;
import com.sync.api.domain.model.Project;
import com.sync.api.infra.repository.ProjectRepository;
import com.sync.api.application.service.AuthenticationService;
import com.sync.api.application.service.ProjectService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/create")
    public ResponseEntity<ResponseModelDTO> createProject(@RequestBody @Valid RegisterProjectDTO registerProjectDTO) {
        try {
            Project project = projectService.createProject(registerProjectDTO);
            ResponseModelDTO response = new ResponseModelDTO(project);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ResponseModelDTO(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseModelDTO(e.getMessage()));
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ResponseModelDTO> readProject(@PathVariable String id) {
        try {
            ProjectDto projectDto = projectService.readProject(id);
            EntityModel<ProjectDto> model = EntityModel.of(projectDto);
            addLinksToProjectDto(model, id);
            ResponseModelDTO response = new ResponseModelDTO(projectDto);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ResponseModelDTO(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseModelDTO(e.getMessage()));
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<ResponseModelDTO> listProjects(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "dataInicio", required = false) String projectStartDate,
            @RequestParam(value = "dataFim", required = false) String projectEndDate,
            @RequestParam(value = "status", required = false) ProjectStatus status,
            @RequestParam(value = "classificacao", required = false) ProjectClassification classification
    ) {
        try {
            List<ProjectDto> projectDtoList = projectService.listProjectsFiltered(
                    keyword,
                    projectStartDate != null ? stringToLocalDate(projectStartDate) : null,
                    projectEndDate != null ? stringToLocalDate(projectEndDate) : null,
                    status,
                    classification
            );

            List<EntityModel<ProjectDto>> entityModels = projectDtoList.stream()
                    .map(projectDto -> {
                        EntityModel<ProjectDto> model = EntityModel.of(projectDto);
                        addLinksToProjectDto(model, projectDto.projectId());
                        return model;
                    })
                    .toList();

            return ResponseEntity.ok(new ResponseModelDTO(entityModels));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ResponseModelDTO(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseModelDTO(e.getMessage()));
        }
    }

        @GetMapping("/get/all/near-end")
    public ResponseEntity<ResponseModelDTO> listProjectsEarlyEnd() {
        try {
            List<ProjectDto> projectDtoList = projectService.listProjectsNearEnd();
            List<EntityModel<ProjectDto>> entityModels = projectDtoList.stream()
                    .map(projectDto -> {
                        EntityModel<ProjectDto> model = EntityModel.of(projectDto);
                        addLinksToProjectDto(model, projectDto.projectId());
                        return model;
                    })
                    .toList();
            return ResponseEntity.ok(new ResponseModelDTO(entityModels));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ResponseModelDTO(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseModelDTO(e.getMessage()));
        }
    }



    @GetMapping("/get/coordinators")
    public ResponseEntity<ResponseModelDTO> listCoordinators() {
        try {
            List<String> coordinators = projectService.listCoordinators();
            ResponseModelDTO response = new ResponseModelDTO(coordinators);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ResponseModelDTO(HttpStatus.BAD_REQUEST, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseModelDTO(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
        }
    }

    @GetMapping("/get/companies")
    public ResponseEntity<ResponseModelDTO> listCompanies() throws SystemContextException {
        try {
            List<String> companies = projectService.listCompanies();
            ResponseModelDTO response = new ResponseModelDTO(companies);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new SystemContextException(e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseModelDTO> updateProject(
            @PathVariable String id,
            @Valid @RequestBody UpdateProjectDto updateProjectDto) {
        try {
            var user = authenticationService.getLoggedUser();
            Project project = projectService.updateProject(id, updateProjectDto, user);
            ResponseModelDTO response = new ResponseModelDTO(project);
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseModelDTO("Project not found: " + e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ResponseModelDTO("Invalid data: " + e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModelDTO("Runtime exception: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModelDTO("An unexpected error occurred: " + e.getMessage()));
        }
    }

    @PutMapping("/update/draft/{id}")
    public ResponseEntity<ResponseModelDTO> updateDraftProject(
            @PathVariable String id,
            @Valid @RequestBody UpdateProjectDto updateProjectDto) {
        try {
            var user = authenticationService.getLoggedUser();
            Project project = projectService.updateProject(id, updateProjectDto, user);
            ResponseModelDTO response = new ResponseModelDTO(project);
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseModelDTO("Project not found: " + e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ResponseModelDTO("Invalid data: " + e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModelDTO("Runtime exception: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModelDTO("An unexpected error occurred: " + e.getMessage()));
        }
    }

    @GetMapping("/get/history-projects/{id}")
    public ResponseEntity<ResponseModelDTO> listHistoryProjects(@PathVariable String id) {
        try {
            List<HistoryProjectDto> projectDtoList = projectService.listHistoryChanges(id);
            ResponseModelDTO response = new ResponseModelDTO(projectDtoList);
            return ResponseEntity.ok().body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ResponseModelDTO(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseModelDTO(e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseModelDTO> deleteProject(@PathVariable String id) {
        try {
            boolean projectDeleted = projectService.deleteProject(id);
            ResponseModelDTO response = new ResponseModelDTO(projectDeleted);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ResponseModelDTO(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseModelDTO(e.getMessage()));
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<?> filterProjects(
            @RequestParam(required = false) String projectReference,
            @RequestParam(required = false) String projectCompany,
            @RequestParam(required = false) String nameCoordinator,
            @RequestParam(required = false) ProjectClassification projectClassification,
            @RequestParam(required = false) ProjectStatus projectStatus,
            @RequestParam(required = false) LocalDate projectStartDate,
            @RequestParam(required = false) LocalDate projectEndDate) {

        List<ProjectDto> filteredProjects = projectService.filterProjects(
                projectReference, projectCompany, nameCoordinator,
                projectClassification, projectStatus,
                projectStartDate, projectEndDate
        );

        var response = new ResponseModelDTO(filteredProjects);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/export/{id}/{format}")
    public ResponseEntity<byte[]> exportProject(@PathVariable("id") String id, @PathVariable("format") String format) {
        try {
            byte[] fileBytes = projectService.exportProject(id, format);
            HttpHeaders headers = new HttpHeaders();
            if ("pdf".equalsIgnoreCase(format)) {
                headers.setContentType(MediaType.APPLICATION_PDF);
                headers.setContentDispositionFormData("inline", "project.pdf");
            } else if ("excel".equalsIgnoreCase(format)) {
                headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
                headers.setContentDispositionFormData("attachment", "project.xlsx");
            } else {
                return ResponseEntity.badRequest().build();
            }
            return new ResponseEntity<>(fileBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private void addLinksToProjectDto(EntityModel<ProjectDto> model, String projectId) {
        model.add(linkTo(methodOn(ProjectController.class).readProject(projectId)).withSelfRel());
        model.add(linkTo(methodOn(ProjectController.class).listProjects(null, null, null, null, null)).withRel("list"));
    }

    private LocalDate stringToLocalDate(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return null; // ou lance uma exceção personalizada
        }
        DateTimeFormatter parser = DateTimeFormatter.ofPattern("uuuu-MM-dd");
        try {
            return LocalDate.parse(dateString, parser);
        } catch (DateTimeParseException e) {
            throw new RuntimeException("Data inválida: " + dateString);
        }
    }
}
