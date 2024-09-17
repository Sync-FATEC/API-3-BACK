package com.sync.api.service;

import com.sync.api.enums.TiposAnexos;
import com.sync.api.exception.SystemContextException;
import com.sync.api.infra.security.SecurityUtils;
import com.sync.api.model.Documents;
import com.sync.api.model.User;
import com.sync.api.repository.DocumentRepository;
import com.sync.api.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

@Component
public class DocumentService {

    private static final String UPLOAD_DIR = "uploads/";

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private ProjectRepository projectRepository;

    public void upload(MultipartFile file, String projectId, TiposAnexos fileType) throws Exception {

        User userContext = SecurityUtils.getAuthenticatedUser();

        var project = projectRepository.findById(projectId).orElseThrow(() -> new SystemContextException("Project not found"));

        Documents document = new Documents();

        document.CreateBaseProject(
                file.getOriginalFilename(),
                fileType,
                LocalDate.now(),
                project,
                userContext);

        documentRepository.save(document);

        var url = document.GenereateUrl();

        saveFile(file, url);

        documentRepository.save(document);
    }

    private void saveFile(MultipartFile file, String url) throws Exception {

    }
}
