package com.sync.api.controller;

import com.sync.api.dto.project.ProjectDto;
import com.sync.api.dto.project.RegisterProjectDTO;
import com.sync.api.dto.web.ResponseModelDTO;
import com.sync.api.exception.SystemContextException;
import com.sync.api.model.Project;
import com.sync.api.repository.ProjectRepository;
import com.sync.api.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> createProject(@RequestBody @Valid RegisterProjectDTO projectDto) {
        try {
            Project project = projectService.createProject(projectDto);
            var response = new ResponseModelDTO(project);
            return ResponseEntity.ok().body(response);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/read/{id}")
    public ResponseEntity<?> readProject(@Valid @PathVariable String id) {
        try {
            ProjectDto projectDto = projectService.readProject(id);
            addLinksToProjectDto(projectDto, id);
            var response = new ResponseModelDTO(projectDto);
            return ResponseEntity.ok().body(response);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<?> listProjects() {
        try {
            List<ProjectDto> projectDtoList = projectService.listProjects();
            projectDtoList.forEach(projectDto -> addLinksToProjectDto(projectDto, projectDto.getProjectId()));
            var response = new ResponseModelDTO(projectDtoList);
            return ResponseEntity.ok().body(response);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/list/coordinators")
    public ResponseEntity<?> listCoordinators() {
        try {
            List<String> coordinators = projectService.listCoordinators();
            var response = new ResponseModelDTO(coordinators);
            return ResponseEntity.ok().body(response);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/list/companies")
    public ResponseEntity<?> listCompanies() throws SystemContextException {
        try {
            List<String> companies = projectService.listCompanies();
            var response = new ResponseModelDTO(companies);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            throw new SystemContextException(e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> projectUpdate(@PathVariable String id, @RequestBody ProjectDto projectDto) {
        try {
            Project project = projectService.updateProject(id, projectDto);
            var response = new ResponseModelDTO(project);
            return ResponseEntity.ok().body(response);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProject(@Valid @PathVariable String id) {
        try {
            boolean projectDeletado = projectService.deleteProject(id);
            var response = new ResponseModelDTO(projectDeletado);
            return ResponseEntity.ok().body(response);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/export/{id}/{format}")
    public ResponseEntity<byte[]> exportProject(@PathVariable("id") String id, @PathVariable("format") String format) {
        byte[] fileBytes;
        try {
            fileBytes = projectService.exportProject(id, format);
            HttpHeaders headers = new HttpHeaders();
            if ("pdf".equalsIgnoreCase(format)) {
                headers.setContentType(MediaType.APPLICATION_PDF);
                headers.setContentDispositionFormData("inline", "project.pdf");
            } else if ("excel".equalsIgnoreCase(format)) {
                headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
                headers.setContentDispositionFormData("attachment", "project.xlsx");
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(fileBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void addLinksToProjectDto(ProjectDto projectDto, String projectId) {
        projectDto.add(linkTo(methodOn(ProjectController.class).readProject(projectId)).withSelfRel().withType("GET"));
        projectDto.add(linkTo(methodOn(ProjectController.class).listProjects()).withRel("list").withType("GET"));
    }
}