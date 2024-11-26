package com.sync.api.infra.repository;

import com.sync.api.domain.model.DraftEditProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface DraftEditProjectRepository extends JpaRepository<DraftEditProject, String> {
    @Query("SELECT d FROM DraftEditProject d WHERE d.project.projectId = :id")
    Optional<DraftEditProject> findByProjectId(String id);
}
