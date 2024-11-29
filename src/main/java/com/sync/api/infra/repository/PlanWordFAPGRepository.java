package com.sync.api.infra.repository;

import com.sync.api.domain.model.PlanWordFAPG;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlanWordFAPGRepository extends JpaRepository<PlanWordFAPG, String> {
    Optional<PlanWordFAPG> findByProject_ProjectId(String projectId);
}

