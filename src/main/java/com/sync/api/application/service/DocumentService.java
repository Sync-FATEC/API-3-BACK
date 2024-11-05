package com.sync.api.application.service;

import com.sync.api.web.dto.project.HistoryProjectDto;
import com.sync.api.web.dto.documents.DocumentUploadDto;
import com.sync.api.web.exception.SystemContextException;
import com.sync.api.domain.model.Documents;
import com.sync.api.domain.model.Project;
import com.sync.api.domain.model.User;
import com.sync.api.application.operation.RegisterHistoryProject;
import com.sync.api.application.operation.uploads.UploadsDocuments;
import com.sync.api.infra.repository.DocumentRepository;
import com.sync.api.infra.repository.ProjectRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UploadsDocuments uploadsDocuments;

    @Autowired
    private RegisterHistoryProject registerHistoryProject;

    public Documents createDocument(DocumentUploadDto documentUploadDto, Project project) {
        try {
            String fileUrl = uploadsDocuments.uploadFile(documentUploadDto.file());

            if (fileUrl == null) {
                throw new RuntimeException("Falha no upload do arquivo.");
            }

            Documents document = new Documents();
            document.setFileName(documentUploadDto.file().getOriginalFilename());
            document.setFileType(documentUploadDto.typeFile());
            document.setFilePath(fileUrl);
            document.setUploadedAt(LocalDate.now());
            document.setProject(project);
            documentRepository.save(document);

            document.setFileUrl("/documents/get/" + document.getDocuments_id());

            project.getDocuments().add(document);

            projectRepository.save(project);

            return documentRepository.save(document);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao processar o arquivo: " + e.getMessage(), e);
        }
    }

    public File findDocument(String documentId) throws SystemContextException {
        var docOp = documentRepository.findById(documentId);

        if(docOp.isEmpty()){
            throw new SystemContextException("Documento não encontrado.");
        }

        var docDb = docOp.get();

        return uploadsDocuments.getDocumento(docDb.getFilePath());
    }

    public boolean removedDocument(String documentId, User user) {
        Documents document = documentRepository.findById(documentId)
                .orElseThrow(() -> new EntityNotFoundException("Documento não encontrado com o id: " + documentId));

        document.setRemoved(true);
        documentRepository.save(document);
        HistoryProjectDto historyProjectDto = new HistoryProjectDto("removed","true","false", LocalDateTime.now(), document.getProject(),document, user, user.getUserEmail());
        registerHistoryProject.registerLog(historyProjectDto);
        return true;
    }

    public boolean addDocument(String documentId, User user) {
        Documents document = documentRepository.findById(documentId)
                .orElseThrow(() -> new EntityNotFoundException("Documento não encontrado com o id: " + documentId));

        document.setRemoved(false);
        documentRepository.save(document);
        HistoryProjectDto historyProjectDto = new HistoryProjectDto("add","true","false", LocalDateTime.now(), document.getProject(),document, user, user.getUserEmail());
        registerHistoryProject.registerLog(historyProjectDto);
        return true;
    }

}
