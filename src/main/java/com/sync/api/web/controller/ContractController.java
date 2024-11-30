package com.sync.api.web.controller;

import com.sync.api.application.operation.exporter.GenerateFAPGContract;
import com.sync.api.application.service.DocumentService;
import com.sync.api.domain.enums.FileType;
import com.sync.api.domain.model.Project;
import com.sync.api.infra.repository.ProjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.sync.api.domain.model.Documents;

@RestController
@RequestMapping("/contracts")
public class ContractController {

    private static final Logger logger = LoggerFactory.getLogger(ContractController.class);

    private final GenerateFAPGContract generateFAPGContract;
    private final ProjectRepository projectRepository;
    private final DocumentService documentService;

    @Autowired
    public ContractController(GenerateFAPGContract generateFAPGContract, ProjectRepository projectRepository, DocumentService documentService) {
        this.generateFAPGContract = generateFAPGContract;
        this.projectRepository = projectRepository;
	    this.documentService = documentService;
    }

    @GetMapping("/generate/{projectId}")
    public ResponseEntity<byte[]> generateContract(@PathVariable String projectId) {
        logger.info("Received request to generate contract for projectId: {}", projectId);
        try {
            Project project = findProjectById(String.valueOf(projectId));
            byte[] contractBytes = generateFAPGContract.generateContract(project);

            Documents contractDocument = documentService.saveDocument(
                    "Contrato " + project.getProjectReference() + ".docx",
                    FileType.CONTRATO,
                    contractBytes,
                    project
            );

            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=contract.docx");
            headers.setContentType(org.springframework.http.MediaType.APPLICATION_OCTET_STREAM);

            return new ResponseEntity<>(contractBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error generating contract for projectId: {}", projectId, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Project findProjectById(String projectId) {
        return projectRepository.findByProjectId(projectId)
                .orElseThrow(() -> {
                    logger.error("Project not found with id: {}", projectId);
                    return new RuntimeException("Project not found with id: " + projectId);
                });
    }
}