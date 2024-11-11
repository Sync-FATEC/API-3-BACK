package com.sync.api.infra.repository;

import com.sync.api.domain.model.Coordinators;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoordinatorsRepository extends JpaRepository<Coordinators, Long> {
    Coordinators findByCoordinatorName(String name);
}
