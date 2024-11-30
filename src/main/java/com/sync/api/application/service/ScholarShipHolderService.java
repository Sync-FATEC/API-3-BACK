package com.sync.api.application.service;

import com.sync.api.application.AddressFactory;
import com.sync.api.domain.model.Address;
import com.sync.api.domain.model.ScholarGrant;
import com.sync.api.domain.model.ScholarShipHolder;
import com.sync.api.infra.repository.AddressRepository;
import com.sync.api.infra.repository.GrantRepository;
import com.sync.api.infra.repository.ScholarShipHolderRepository;
import com.sync.api.web.dto.ScholarShipHolder.RegisterScholarShipHolderDto;
import com.sync.api.web.dto.ScholarShipHolder.UpdateScholarShipHolderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScholarShipHolderService {

    @Autowired
    private ScholarShipHolderRepository scholarShipHolderRepository;

    @Autowired
    private AddressFactory addressFactory;

    @Autowired
    private GrantRepository grantRepository;
    @Autowired
    private AddressRepository addressRepository;


    public ScholarShipHolder createScholarShip(RegisterScholarShipHolderDto dto){
        try{
            // Criar endereco
            Address address = new Address();
            address.setStreet(dto.getAddress().getStreet());
            address.setNumber(dto.getAddress().getNumber());
            address.setNeighborhood(dto.getAddress().getNeighborhood());
            address.setCity(dto.getAddress().getCity());
            address.setState(dto.getAddress().getState());
            address.setZipCode(dto.getAddress().getZipCode());

            addressRepository.save(address);

            ScholarGrant grant = grantRepository.findById(dto.getGrantId())
                    .orElseThrow(()-> new RuntimeException("erro bolsa nao encontrada"));

            ScholarShipHolder scholarShipHolder = new ScholarShipHolder();
            scholarShipHolder.setName(dto.getName());
            scholarShipHolder.setEmail(dto.getEmail());
            scholarShipHolder.setCpf(dto.getCpf());
            scholarShipHolder.setRg(dto.getRg());
            scholarShipHolder.setNationality(dto.getNationality());
            scholarShipHolder.setAddress(address);
            scholarShipHolder.setRemoved(false);
            scholarShipHolder.setGrant(grant);

            return scholarShipHolderRepository.save(scholarShipHolder);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ScholarShipHolder updateScholarShipHolder( UpdateScholarShipHolderDto dto){
        try{
            ScholarShipHolder scholarShipHolder = scholarShipHolderRepository.findById(dto.getId())
                    .orElseThrow(()-> new RuntimeException("erro bolsista nao encontrado"));


            ScholarGrant grant = grantRepository.findById(dto.getGrantId())
                    .orElseThrow(()-> new RuntimeException("erro bolsa nao encontrada"));

            Address address = addressRepository.findById(dto.getAddress().getId())
                    .orElseThrow(()-> new RuntimeException("erro endereco nao encontrado"));

            address.setStreet(dto.getAddress().getStreet());
            address.setNumber(dto.getAddress().getNumber());
            address.setNeighborhood(dto.getAddress().getNeighborhood());
            address.setCity(dto.getAddress().getCity());
            address.setState(dto.getAddress().getState());
            address.setZipCode(dto.getAddress().getZipCode());

            addressRepository.save(address);



            scholarShipHolder.setName(dto.getName());
            scholarShipHolder.setEmail(dto.getEmail());
            scholarShipHolder.setCpf(dto.getCpf());
            scholarShipHolder.setRg(dto.getRg());
            scholarShipHolder.setNationality(dto.getNationality());
            scholarShipHolder.setAddress(address);
            scholarShipHolder.setRemoved(false);
            scholarShipHolder.setGrant(grant);

            scholarShipHolderRepository.save(scholarShipHolder);
            return scholarShipHolder;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public ScholarShipHolder getScholarShipHolder(String id){
        try{
            ScholarShipHolder scholarShipHolder = scholarShipHolderRepository.findById(id)
                    .orElseThrow(()->{
                        throw new RuntimeException();
                    });
            return scholarShipHolder;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<ScholarShipHolder> getAllGrant(){
        try{
            List<ScholarShipHolder> grantList = scholarShipHolderRepository.findAll();
            return grantList;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void deactivateScholarShipHolder(String id) {
        try {
            ScholarShipHolder scholarShipHolder = scholarShipHolderRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Scholarship holder not found with id: " + id));

            scholarShipHolder.setRemoved(true); // Altera o campo
            scholarShipHolderRepository.save(scholarShipHolder); // Persiste a alteração no banco de dados

            System.out.println("Scholarship holder with id " + id + " successfully deactivated.");
        } catch (RuntimeException e) {
            System.err.println("Error deactivating scholarship holder: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("Unexpected error occurred: " + e.getMessage());
            throw new RuntimeException("Failed to deactivate scholarship holder", e);
        }
    }

}
