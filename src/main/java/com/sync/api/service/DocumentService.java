package com.sync.api.service;

import com.sync.api.dto.documents.DocumentUploadDto;
import com.sync.api.exception.SystemContextException;
import com.sync.api.model.Documents;
import com.sync.api.model.Project;
import com.sync.api.operation.uploads.UploadsDocuments;
import com.sync.api.repository.DocumentRepository;
import com.sync.api.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UploadsDocuments uploadsDocuments;

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

            document.setFileUrl("localhost:8080/documents/" + document.getDocuments_id());

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
}
