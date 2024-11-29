package com.sync.api.infra.repository;

import com.sync.api.domain.model.WorkPlanCompleteData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkPlanCompleteDataRepository extends JpaRepository<WorkPlanCompleteData, String> {
}
