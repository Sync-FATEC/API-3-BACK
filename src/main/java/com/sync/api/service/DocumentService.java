package com.sync.api.service;

import com.sync.api.dto.DocumentUploadDto;
import com.sync.api.model.Documents;
import com.sync.api.model.Project;
import com.sync.api.operation.uploads.UploadsDocuments;
import com.sync.api.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

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
            document.setFileUrl(fileUrl);
            document.setUploadedAt(LocalDate.now());
            document.setProject(project);

            return documentRepository.save(document);

        } catch (IOException e) {
            throw new RuntimeException("Erro ao processar o arquivo: " + e.getMessage(), e);
        }
    }
}