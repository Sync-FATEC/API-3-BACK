package com.sync.api.application.service;

import com.sync.api.domain.model.Address;
import com.sync.api.infra.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    public List<Address> getAddress() {
        try {
            return addressRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar todos os endereços", e);
        }
    }

    public Address getAddress(String id) {
        try {
            return addressRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Erro ao encontrar endereço."));
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar endereço", e);
        }
    }
}
