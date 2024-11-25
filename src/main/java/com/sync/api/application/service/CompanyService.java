package com.sync.api.application.service;

import com.sync.api.domain.model.Address;
import com.sync.api.domain.model.Company;
import com.sync.api.infra.repository.AddressRepository;
import com.sync.api.infra.repository.CompanyRepository;
import com.sync.api.web.dto.company.RegisterCompanyDTO;
import com.sync.api.web.dto.company.UpdateCompanyDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private AddressRepository addressRepository;

    public Company createCompany(RegisterCompanyDTO registerCompanyDTO) {
        try {
            Address address = new Address(registerCompanyDTO.getAddressStreet(),
                    registerCompanyDTO.getAddressNumber(),
                    registerCompanyDTO.getAddressNeighborhood(),
                    registerCompanyDTO.getAddressCity(),
                    registerCompanyDTO.getAddressZipCode(),
                    registerCompanyDTO.getAddressState());

            addressRepository.save(address);

            Company company = mapToCompany(registerCompanyDTO, address);

            companyRepository.save(company);

            return company;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar empresa: " + e.getMessage(), e);
        }
    }



    public Company getCompany(String id) {
        try {
            return companyRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Erro ao encontrar empresa."));
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar empresa", e);
        }
    }

    public List<Company> getCompanies() {
        try {
            return companyRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar todas as empresas", e);
        }
    }

    public List<Company> filterCompanies(String keyword) {
        try {
            return companyRepository.findByAnyFieldOrAddress(keyword);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar empresas", e);
        }
    }

    public Company updateCompany(UpdateCompanyDTO updateCompanyDTO) {
        try {
            Company existingCompany = companyRepository.findById(updateCompanyDTO.getId())
                    .orElseThrow(() -> new RuntimeException("Empresa não encontrada"));

            Address address;
            if (updateCompanyDTO.getAddressId() != null && !updateCompanyDTO.getAddressId().isEmpty()) {
                address = addressRepository.findById(updateCompanyDTO.getAddressId())
                        .orElseThrow(() -> new RuntimeException("Endereço não encontrado"));

                // Update the address details
                address.setStreet(updateCompanyDTO.getAddressStreet());
                address.setNumber(updateCompanyDTO.getAddressNumber());
                address.setNeighborhood(updateCompanyDTO.getAddressNeighborhood());
                address.setCity(updateCompanyDTO.getAddressCity());
                address.setState(updateCompanyDTO.getAddressState());
                address.setZipCode(updateCompanyDTO.getAddressZipCode());

                addressRepository.save(address);
            } else {
                address = new Address(updateCompanyDTO.getAddressStreet(),
                        updateCompanyDTO.getAddressNumber(),
                        updateCompanyDTO.getAddressNeighborhood(),
                        updateCompanyDTO.getAddressCity(),
                        updateCompanyDTO.getAddressZipCode(),
                        updateCompanyDTO.getAddressState());

                addressRepository.save(address);
            }

            Company company = mapToCompany(updateCompanyDTO, address);

            companyRepository.save(company);

            return existingCompany;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar empresa: " + e.getMessage(), e);
        }
    }



    public void deleteCompany(String id) {
        try {
            Company company = companyRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Erro ao encontrar empresa."));
            companyRepository.delete(company);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao deletar empresa: " + e.getMessage(), e);
        }
    }

    private Company mapToCompany(RegisterCompanyDTO registerCompanyDTO, Address address) {
        Company company = new Company();
        company.setCorporateName(registerCompanyDTO.getCorporateName());
        company.setCnpj(registerCompanyDTO.getCnpj());
        company.setPhone(registerCompanyDTO.getPhone());
        company.setPrivateCompany(registerCompanyDTO.privateCompany);
        company.setAddress(address);
        return company;
    }

    private Company mapToCompany(UpdateCompanyDTO updateCompanyDTO, Address address) {
        Company company = new Company();
        company.setId(updateCompanyDTO.getId());
        company.setCorporateName(updateCompanyDTO.getCorporateName());
        company.setCnpj(updateCompanyDTO.getCnpj());
        company.setPhone(updateCompanyDTO.getPhone());
        company.setPrivateCompany(updateCompanyDTO.privateCompany);
        company.setAddress(address);
        return company;
    }
}
