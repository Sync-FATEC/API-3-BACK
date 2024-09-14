package com.sync.api.controller;

import com.sync.api.dto.ProjectDto;
import com.sync.api.model.Project;
import com.sync.api.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @PostMapping("/create/new/project")
    public ResponseEntity<Project> createProject(@Valid @RequestBody ProjectDto projectDto) {
        try {
            Project project = projectService.createProject(projectDto);
            return new ResponseEntity<>(project, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/read/project/{id}")
    public ResponseEntity<ProjectDto> readProject(@Valid @PathVariable String id){
        try {
            ProjectDto projectDto = projectService.readProject(id);
            return new ResponseEntity<>(projectDto, HttpStatus.OK);
        }catch (RuntimeException e){
            return  new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            return  new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/list/projects")
    public ResponseEntity<List<ProjectDto>> listProjects(){
        try {
            List<ProjectDto> projectDtoList = projectService.listProjects();
            return new ResponseEntity<>(projectDtoList, HttpStatus.OK);
        }catch (RuntimeException e){
            return  new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            return  new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/project/{id}")
    public ResponseEntity<Project> projectUpdate(String id, ProjectDto  projectDto){
        try {
            Project project = projectService.updateProject(id, projectDto);
            return new ResponseEntity<>(project, HttpStatus.OK);
        }catch (RuntimeException e){
            return  new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            return  new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/project/{id}")
    public ResponseEntity<?> deleteProject(@Valid @PathVariable String id){
        try {
            boolean projectDeletado = projectService.deleteProject(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (RuntimeException e){
            return  new ResponseEntity<>( HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            return  new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/export/project/{id}/{format}")
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
}