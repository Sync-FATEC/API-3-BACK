package com.sync.api.repository;

import com.sync.api.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, String>, JpaSpecificationExecutor<Project> {
    List<Project> findAllByOrderByProjectStartDateDesc();
}
