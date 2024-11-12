package com.sync.api.infra.repository;

import com.sync.api.domain.model.DraftEditProject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DraftEditProjectRepository extends JpaRepository<DraftEditProject, String> {
}
