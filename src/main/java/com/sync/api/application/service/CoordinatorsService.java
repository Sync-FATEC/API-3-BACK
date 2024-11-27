package com.sync.api.application.service;

import com.sync.api.domain.model.Coordinators;
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

    public Coordinators createCoordinators(RegisterCoordinatorsDTO registerCoordinatorsDTO) {
        try {
            Coordinators coordinators = mapToCoordinators(registerCoordinatorsDTO);
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
            Coordinators coordinators = mapToCoordinators(updateCoordinatorsDTO);
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
        return coordinators;
    }

}
