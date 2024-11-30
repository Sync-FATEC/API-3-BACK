package com.sync.api.application.service;

import com.sync.api.application.operation.exporter.GenerateFAPGContract;
import com.sync.api.domain.model.Documents;
import com.sync.api.infra.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContractService
{
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private GenerateFAPGContract contractGenerator;

    public void generateFPAGContract(String projectId) {

        var project = projectRepository.findById(projectId).orElseThrow();

        var contract = contractGenerator.generateContract(project);

        var documentToSave = Documents.CreateContract(project, contract);

        documentRepository.save(documentToSave);
    }
}
