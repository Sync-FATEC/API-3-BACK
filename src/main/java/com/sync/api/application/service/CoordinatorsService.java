package com.sync.api.application.service;

import com.sync.api.domain.model.Address;
import com.sync.api.domain.model.Coordinators;
import com.sync.api.infra.repository.AddressRepository;
import com.sync.api.infra.repository.CoordinatorsRepository;
import com.sync.api.web.dto.coordinators.RegisterCoordinatorsDTO;
import com.sync.api.web.dto.coordinators.UpdateCoordinatorsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class CoordinatorsService {
    @Autowired
    private CoordinatorsRepository coordinatorsRepository;

    @Autowired
    private AddressRepository addressRepository;

    public Coordinators createCoordinators(RegisterCoordinatorsDTO registerCoordinatorsDTO) {
        try {
            Coordinators coordinators = mapToCoordinators(registerCoordinatorsDTO);
            addressRepository.save(coordinators.getAddress());
            coordinatorsRepository.save(coordinators);
            return coordinators;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar coordenador: " + e.getMessage(), e);
        }
    }

    public List<Coordinators> getCoordinators() {
        try {
            List<Coordinators> coordinatorsList = coordinatorsRepository.findAll();
            coordinatorsList.sort(Comparator.comparing(Coordinators::getCoordinatorName));
            return coordinatorsList;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Coordinators getCoordinator(String id) {
        try {
            return coordinatorsRepository.findByCoordinatorId(id)
                    .orElseThrow(() -> new RuntimeException("Erro ao encontrar coordenador."));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Coordinators updateCoordinator(UpdateCoordinatorsDTO updateCoordinatorsDTO) {
        try {
            Coordinators existingCoordinators = coordinatorsRepository.findByCoordinatorId(updateCoordinatorsDTO.getCoordinatorId())
                    .orElseThrow(() -> new RuntimeException("Coordenador não encontrado"));

            Address address;
            if (updateCoordinatorsDTO.getAddressId() != null && !updateCoordinatorsDTO.getAddressId().isEmpty()) {
                address = addressRepository.findById(updateCoordinatorsDTO.getAddressId())
                        .orElseThrow(() -> new RuntimeException("Endereço não encontrado"));

                address.setStreet(updateCoordinatorsDTO.getAddressStreet());
                address.setNumber(updateCoordinatorsDTO.getAddressNumber());
                address.setNeighborhood(updateCoordinatorsDTO.getAddressNeighborhood());
                address.setCity(updateCoordinatorsDTO.getAddressCity());
                address.setState(updateCoordinatorsDTO.getAddressState());
                address.setZipCode(updateCoordinatorsDTO.getAddressZipCode());

                addressRepository.save(address);
            } else {
                address = new Address(updateCoordinatorsDTO.getAddressStreet(),
                        updateCoordinatorsDTO.getAddressNumber(),
                        updateCoordinatorsDTO.getAddressNeighborhood(),
                        updateCoordinatorsDTO.getAddressCity(),
                        updateCoordinatorsDTO.getAddressZipCode(),
                        updateCoordinatorsDTO.getAddressState());

                addressRepository.save(address);
            }

            Coordinators coordinators = mapToCoordinators(updateCoordinatorsDTO);
            coordinators.setAddress(address);

            coordinatorsRepository.save(coordinators);

            return coordinators;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar coordenador: " + e.getMessage(), e);
        }
    }


    public void deleteCoordinator(String id) {
        try {
            Coordinators coordinators = coordinatorsRepository.findByCoordinatorId(id)
                    .orElseThrow(() -> new RuntimeException("Erro ao encontrar coordenador."));
            coordinatorsRepository.delete(coordinators);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao deletar coordenador: " + e.getMessage(), e);
        }
    }

    public List<Coordinators> filterCoordinators(String keyword) {
        try {
            return coordinatorsRepository.findByAnyField(keyword);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar coordenadores: " + e.getMessage(), e);
        }
    }

    private Coordinators mapToCoordinators(UpdateCoordinatorsDTO updateCoordinatorsDTO) {
        Coordinators coordinators = new Coordinators();
        coordinators.setCoordinatorId(updateCoordinatorsDTO.getCoordinatorId());
        coordinators.setCoordinatorName(updateCoordinatorsDTO.getCoordinatorName());
        coordinators.setCoordinatorCPF(updateCoordinatorsDTO.getCoordinatorCPF());
        coordinators.setCoordinatorTelefone(updateCoordinatorsDTO.getCoordinatorTelefone());
        coordinators.setCoordinatorRG(updateCoordinatorsDTO.getCoordinatorRG());
        coordinators.setCoordinatorNacionality(updateCoordinatorsDTO.getCoordinatorNacionality());
        coordinators.setCoordinatorMaritalStatus(updateCoordinatorsDTO.getCoordinatorMaritalStatus());
        coordinators.setCoordinatorEconomicActivity(updateCoordinatorsDTO.getCoordinatorEconomicActivity());

        return coordinators;
    }

    private Coordinators mapToCoordinators(RegisterCoordinatorsDTO registerCoordinatorsDTO) {
        Coordinators coordinators = new Coordinators();
        coordinators.setCoordinatorName(registerCoordinatorsDTO.getCoordinatorName());
        coordinators.setCoordinatorCPF(registerCoordinatorsDTO.getCoordinatorCPF());
        coordinators.setCoordinatorTelefone(registerCoordinatorsDTO.getCoordinatorTelefone());
        coordinators.setCoordinatorRG(registerCoordinatorsDTO.getCoordinatorRG());
        coordinators.setCoordinatorNacionality(registerCoordinatorsDTO.getCoordinatorNacionality());
        coordinators.setCoordinatorMaritalStatus(registerCoordinatorsDTO.getCoordinatorMaritalStatus());
        coordinators.setCoordinatorEconomicActivity(registerCoordinatorsDTO.getCoordinatorEconomicActivity());

        Address address = new Address();
        address.setState(registerCoordinatorsDTO.getAddressState());
        address.setCity(registerCoordinatorsDTO.getAddressCity());
        address.setStreet(registerCoordinatorsDTO.getAddressStreet());
        address.setNeighborhood(registerCoordinatorsDTO.getAddressNeighborhood());
        address.setNumber(registerCoordinatorsDTO.getAddressNumber());
        address.setZipCode(registerCoordinatorsDTO.getAddressZipCode());
        coordinators.setAddress(address);

        return coordinators;
    }

}
