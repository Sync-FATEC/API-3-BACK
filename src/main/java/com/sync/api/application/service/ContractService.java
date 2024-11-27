package com.sync.api.application.service;

import com.sync.api.application.operation.exporter.GenerateFAPGContract;
import com.sync.api.domain.model.ContractFapg;
import com.sync.api.infra.repository.CompanyRepository;
import com.sync.api.infra.repository.ContractFapgRepository;
import com.sync.api.infra.repository.CoordinatorsRepository;
import com.sync.api.infra.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContractService
{
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ContractFapgRepository contractFapgRepository;

    @Autowired
    private GenerateFAPGContract contractGenerator;

    public ContractFapg generateFPAGContract(String projectId) {

        var project = projectRepository.findById(projectId).orElseThrow();

        var contract = contractGenerator.generateContract(project);

        var contractEntity = new ContractFapg(contract, project.getProjectReference());

        contractFapgRepository.save(contractEntity);

        return contractEntity;
    }

    public List<ContractFapg> listContracts () {
        return contractFapgRepository.findAll();
    }

    public byte[] getContract(String contractId) {
        return contractFapgRepository.findById(contractId).orElseThrow().getContract_file();
    }
}
