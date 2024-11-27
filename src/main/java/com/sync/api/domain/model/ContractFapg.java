package com.sync.api.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;


@Entity
@Data
public class ContractFapg {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    public String projectReference;
    public byte[] contract_file;

    public ContractFapg(byte[] contract, String projectReference) {
        this.contract_file = contract;
        this.projectReference = projectReference;
    }
}
