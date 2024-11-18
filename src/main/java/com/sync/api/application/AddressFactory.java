package com.sync.api.application;

import com.sync.api.domain.model.Address;
import com.sync.api.domain.model.Company;
import com.sync.api.infra.repository.AddressRepository;
import com.sync.api.infra.repository.CompanyRepository;
import com.sync.api.web.dto.address.CreateAddressDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AddressFactory {

    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private AddressRepository addressRepository;

    public Company CreateCompanyAddress(CreateAddressDTO data, Company company){

        var address =  new Address(
                data.getStreet(),
                data.getNumber(),
                data.getNeighborhood(),
                data.getCity(),
                data.getZipCode(),
                data.getState()
        );
        addressRepository.save(address);

        company.setAddress(address);

        companyRepository.save(company);

        return company;
    };

    public Company UpdateCompanyAddress(CreateAddressDTO data, Company company){

        var address =  new Address(
                data.getStreet(),
                data.getNumber(),
                data.getNeighborhood(),
                data.getCity(),
                data.getZipCode(),
                data.getState()
        );
        addressRepository.save(address);

        company.setAddress(address);

        companyRepository.save(company);

        return company;
    };


}
