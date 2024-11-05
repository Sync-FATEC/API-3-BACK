package com.sync.api.infra.repository;

import com.sync.api.domain.model.HistoryProject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoryProjectRepository extends JpaRepository<HistoryProject, String> {
    List<HistoryProject> findByProject_ProjectIdOrderByChangeDateDesc(String projectId);

}
