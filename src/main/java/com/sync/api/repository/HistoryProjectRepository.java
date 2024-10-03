package com.sync.api.repository;

import com.sync.api.model.HistoryProject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryProjectRepository extends JpaRepository<HistoryProject, String> {
}
