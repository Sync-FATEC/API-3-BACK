package com.sync.api.web.controller;

import com.sync.api.web.dto.documents.DocumentUploadDto;
import com.sync.api.web.dto.web.ResponseModelDTO;
import com.sync.api.domain.enums.FileType;
import com.sync.api.web.exception.SystemContextException;
import com.sync.api.domain.model.Documents;
import com.sync.api.domain.model.Project;
import com.sync.api.application.service.AuthenticationService;
import com.sync.api.application.service.DocumentService;
import com.sync.api.application.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/create")
    public ResponseEntity<?> createDocument(@RequestParam("projectId") String projectId,
                                            @RequestParam("file") MultipartFile file,
                                            @RequestParam("typeFile") FileType typeFile) {
        try {

            DocumentUploadDto documentUploadDto = new DocumentUploadDto(file, typeFile);
            System.out.println(documentUploadDto);

            Project project = projectService.findProject(projectId);

            Documents document = documentService.createDocument(documentUploadDto, project);

            return new ResponseEntity<>(document, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get/{documentId}")
    public ResponseEntity<?> getDocument(@PathVariable String documentId) {
        try {
            File doc = documentService.findDocument(documentId);
            Path path = doc.toPath();
            Resource resource = new PathResource(path);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header("Content-Disposition", "attachment; filename=\"" + doc.getName() + "\"")
                    .body(resource);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (RuntimeException | SystemContextException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/removed")
    public ResponseEntity<?> removedDocument(@RequestBody List<String> documentIds) {
        try {
            var user = authenticationService.getLoggedUser();
            if (documentIds.isEmpty()) {
                return new ResponseEntity<>("Nenhum ID de documento fornecido", HttpStatus.BAD_REQUEST);
            }

            for (String documentId : documentIds) {
                boolean removedDocument = documentService.removedDocument(documentId, user);
                if (!removedDocument) {
                    return new ResponseEntity<>("Falha ao remover o documento com ID: " + documentId, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }

            return new ResponseEntity<>("Documentos removidos com sucesso", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/add")
    public ResponseEntity<?> addDocument(@RequestBody List<String> documentIds) {
        try {
            var user = authenticationService.getLoggedUser();
            if (documentIds.isEmpty()) {
                return new ResponseEntity<>("Nenhum ID de documento fornecido", HttpStatus.BAD_REQUEST);
            }

            for (String documentId : documentIds) {
                boolean removedDocument = documentService.addDocument(documentId, user);
                if (!removedDocument) {
                    return new ResponseEntity<>("Falha ao adicionar o documento com ID: " + documentId, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }

            ResponseModelDTO response = new ResponseModelDTO("Documentos adicionados com sucesso");
            return ResponseEntity.ok().body(response);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/download/{documentId}")
    public ResponseEntity<?> downloadDocument(@PathVariable String documentId) {
        try {
            Documents doc = documentService.getFile(documentId);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header("Content-Disposition", "attachment; filename=\"" + doc.getFileName() + "\"")
                    .body(doc.getFileBytes());
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
