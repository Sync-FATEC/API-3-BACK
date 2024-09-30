package com.sync.api.repository;

import com.sync.api.enums.ProjectClassification;
import com.sync.api.enums.ProjectStatus;
import com.sync.api.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, String> {
    List<Project> findAllByOrderByProjectStartDateDesc();
    @Query("SELECT p FROM Project p WHERE " +
            "(:projectReference IS NULL OR p.projectReference LIKE %:projectReference%) AND " +
            "(:projectCompany IS NULL OR p.projectCompany LIKE %:projectCompany%) AND " +
            "(:nameCoordinator IS NULL OR p.nameCoordinator LIKE %:nameCoordinator%) AND " +
            "(:projectClassification IS NULL OR p.projectClassification = :projectClassification) AND " +
            "(:projectStatus IS NULL OR p.projectStatus = :projectStatus) AND " +
            "(:projectStartDate IS NULL OR p.projectStartDate >= :projectStartDate) AND " +
            "(:projectEndDate IS NULL OR p.projectEndDate <= :projectEndDate)")
    List<Project> filterProjects(
            @Param("projectReference") String projectReference,
            @Param("projectCompany") String projectCompany,
            @Param("nameCoordinator") String nameCoordinator,
            @Param("projectClassification") ProjectClassification projectClassification,
            @Param("projectStatus") ProjectStatus projectStatus,
            @Param("projectStartDate") LocalDate projectStartDate,
            @Param("projectEndDate") LocalDate projectEndDate
    );
}
