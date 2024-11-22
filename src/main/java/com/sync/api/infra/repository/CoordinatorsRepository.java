package com.sync.api.infra.repository;

import com.sync.api.domain.model.Coordinators;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CoordinatorsRepository extends JpaRepository<Coordinators, Long> {
    Optional <Coordinators> findByCoordinatorName(String name);
}
