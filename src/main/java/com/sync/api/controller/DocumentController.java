package com.sync.api.controller;

import com.sync.api.dto.DocumentUploadDto;
import com.sync.api.dto.ProjectDto;
import com.sync.api.enums.TiposAnexos;
import com.sync.api.model.Documents;
import com.sync.api.model.Project;
import com.sync.api.service.DocumentService;
import com.sync.api.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private ProjectService projectService;

    @PostMapping("/create/documents")
    public ResponseEntity<?> createDocument(@RequestParam("projectId") String projectId,
                                            @RequestParam("file") MultipartFile file,
                                            @RequestParam("typeFile") TiposAnexos typeFile) {
        try {

            DocumentUploadDto documentUploadDto = new DocumentUploadDto(file, typeFile);

            Project project = projectService.findProject(projectId);

            Documents document = documentService.createDocument(documentUploadDto, project);

            return new ResponseEntity<>(document, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
