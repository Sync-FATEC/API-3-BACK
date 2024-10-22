package com.sync.api.repository;

import com.sync.api.model.HistoryProject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoryProjectRepository extends JpaRepository<HistoryProject, String> {
    List<HistoryProject> findByProject_ProjectId(String projectId);
}
