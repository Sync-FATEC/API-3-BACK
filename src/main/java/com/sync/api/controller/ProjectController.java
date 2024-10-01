package com.sync.api.controller;

import com.sync.api.dto.project.ProjectDto;
import com.sync.api.dto.project.RegisterProjectDTO;
import com.sync.api.dto.project.UpdateProjectDto;
import com.sync.api.dto.web.ResponseModelDTO;
import com.sync.api.enums.ProjectClassification;
import com.sync.api.enums.ProjectStatus;
import com.sync.api.exception.SystemContextException;
import com.sync.api.model.Project;
import com.sync.api.repository.ProjectRepository;
import com.sync.api.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
    public ResponseEntity<ResponseModelDTO> listProjects() {
        try {
            List<ProjectDto> projectDtoList = projectService.listProjects();

            List<EntityModel<ProjectDto>> entityModels = projectDtoList.stream()
                    .map(projectDto -> {
                        EntityModel<ProjectDto> model = EntityModel.of(projectDto);
                        addLinksToProjectDto(model, projectDto.projectId()); // Adicionando links ao modelo
                        return model;
                    })
                    .toList();
            ResponseModelDTO response = new ResponseModelDTO(entityModels);
            return ResponseEntity.ok(response);
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
            return ResponseEntity.badRequest().body(new ResponseModelDTO(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseModelDTO(e.getMessage()));
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
    public ResponseEntity<ResponseModelDTO> updateProject(@PathVariable String id, @RequestBody UpdateProjectDto updateProjectDto) {
        try {
            Project project = projectService.updateProject(id, updateProjectDto );
            ResponseModelDTO response = new ResponseModelDTO(project);
            return ResponseEntity.ok(response);
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
        model.add(linkTo(methodOn(ProjectController.class).listProjects()).withRel("list"));
    }

}
