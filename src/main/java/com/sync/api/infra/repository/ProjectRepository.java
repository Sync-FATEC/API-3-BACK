package com.sync.api.infra.repository;

import com.sync.api.domain.enums.ProjectClassification;
import com.sync.api.domain.enums.ProjectStatus;
import com.sync.api.domain.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, String>, JpaSpecificationExecutor<Project> {
    List<Project> findAllByOrderByProjectStartDateDesc();

    @Query("SELECT p FROM Project p JOIN p.coordinators c WHERE " +
            "(:projectReference IS NULL OR p.projectReference LIKE %:projectReference%) AND " +
            "(:projectCompany IS NULL OR p.projectCompany LIKE %:projectCompany%) AND " +
            "(:nameCoordinator IS NULL OR c.coordinatorName LIKE %:nameCoordinator%) AND " +
            "(:projectClassification IS NULL OR p.projectClassification = :projectClassification) AND " +
            "(:projectStatus IS NULL OR p.projectStatus = :projectStatus) AND " +
            "(:projectStartDate IS NULL OR p.projectStartDate = :projectStartDate) AND " +
            "(:projectEndDate IS NULL OR p.projectEndDate = :projectEndDate)")
    List<Project> filterProjects(
            @Param("projectReference") String projectReference,
            @Param("projectCompany") String projectCompany,
            @Param("nameCoordinator") String nameCoordinator,
            @Param("projectClassification") ProjectClassification projectClassification,
            @Param("projectStatus") ProjectStatus projectStatus,
            @Param("projectStartDate") LocalDate projectStartDate,
            @Param("projectEndDate") LocalDate projectEndDate
    );




    @Query("SELECT p FROM Project p WHERE p.projectEndDate BETWEEN :startOfWeek AND :endOfWeek")
    List<Project> findProjectsEndingThisWeek(@Param("startOfWeek") LocalDate startOfWeek,
                                             @Param("endOfWeek") LocalDate endOfWeek);

    @Query("""
    SELECT
    SUM(CASE WHEN p.projectStatus = 'NAO_INICIADOS' THEN 1 ELSE 0 END) AS naoIniciados,
    SUM(CASE WHEN p.projectStatus = 'EM_ANDAMENTO' THEN 1 ELSE 0 END) AS emAndamento,
    SUM(CASE WHEN p.projectStatus = 'FINALIZADOS' THEN 1 ELSE 0 END) AS finalizados
    FROM Project p
    JOIN p.coordinators c
    WHERE (:nameCoordinator IS NULL OR c.coordinatorName LIKE %:nameCoordinator%) AND
          (:projectStartDate IS NULL OR p.projectStartDate >= :projectStartDate) AND
          (:projectEndDate IS NULL OR p.projectEndDate <= :projectEndDate)
""")
    List<Object[]> countProjectsByStatusCoordinator(
            @Param("nameCoordinator") String nameCoordinator,
            @Param("projectStartDate") LocalDate projectStartDate,
            @Param("projectEndDate") LocalDate projectEndDate);

    @Query("""
    SELECT
    SUM(CASE WHEN p.projectClassification = 'OUTROS' THEN 1 ELSE 0 END) AS outros,
    SUM(CASE WHEN p.projectClassification = 'CONTRATOS' THEN 1 ELSE 0 END) AS contratos,
    SUM(CASE WHEN p.projectClassification = 'CONVENIO' THEN 1 ELSE 0 END) AS convenio,
    SUM(CASE WHEN p.projectClassification = 'PATROCINIO' THEN 1 ELSE 0 END) AS patrocinio,
    SUM(CASE WHEN p.projectClassification = 'TERMO_DE_COOPERACAO' THEN 1 ELSE 0 END) AS termoDeCooperacao,
    SUM(CASE WHEN p.projectClassification = 'TERMO_DE_OUTORGA' THEN 1 ELSE 0 END) AS termoDeOutorga
    FROM Project p
    JOIN p.coordinators c
    WHERE (:nameCoordinator IS NULL OR c.coordinatorName LIKE %:nameCoordinator%) AND
          (:projectStartDate IS NULL OR p.projectStartDate >= :projectStartDate) AND
          (:projectEndDate IS NULL OR p.projectEndDate <= :projectEndDate)
""")
    List<Object[]> countProjectsByClassificationCoordinator(
            @Param("nameCoordinator") String nameCoordinator,
            @Param("projectStartDate") LocalDate projectStartDate,
            @Param("projectEndDate") LocalDate projectEndDate);

    @Query("""
    SELECT FUNCTION('MONTH', p.projectStartDate), COUNT(p)
    FROM Project p
    JOIN p.coordinators c
    WHERE (:nameCoordinator IS NULL OR c.coordinatorName LIKE %:nameCoordinator%) AND
          (:projectStartDate IS NULL OR p.projectStartDate >= :projectStartDate) AND
          (:projectEndDate IS NULL OR p.projectEndDate <= :projectEndDate)
    GROUP BY FUNCTION('MONTH', p.projectStartDate)
""")
    List<Object[]> countProjectsByMonthCoordinator(
            @Param("nameCoordinator") String nameCoordinator,
            @Param("projectStartDate") LocalDate projectStartDate,
            @Param("projectEndDate") LocalDate projectEndDate);

    @Query("""
    SELECT
        SUM(CASE WHEN p.projectStatus = 'NAO_INICIADOS' THEN 1 ELSE 0 END) AS naoIniciados,
        SUM(CASE WHEN p.projectStatus = 'EM_ANDAMENTO' THEN 1 ELSE 0 END) AS emAndamento,
        SUM(CASE WHEN p.projectStatus = 'FINALIZADOS' THEN 1 ELSE 0 END) AS finalizados
    FROM Project p
    WHERE (:projectCompany IS NULL OR p.projectCompany LIKE %:projectCompany%) AND
          (:projectStartDate IS NULL OR p.projectStartDate >= :projectStartDate) AND
          (:projectEndDate IS NULL OR p.projectEndDate <= :projectEndDate)
""")
    List<Object[]> countProjectsByStatusForCompany(
            @Param("projectStartDate") LocalDate projectStartDate,
            @Param("projectEndDate") LocalDate projectEndDate,
            @Param("projectCompany") String projectCompany);

    @Query("""
    SELECT
        SUM(CASE WHEN p.projectClassification = 'OUTROS' THEN 1 ELSE 0 END) AS outros,
        SUM(CASE WHEN p.projectClassification = 'CONTRATOS' THEN 1 ELSE 0 END) AS contratos,
        SUM(CASE WHEN p.projectClassification = 'CONVENIO' THEN 1 ELSE 0 END) AS convenio,
        SUM(CASE WHEN p.projectClassification = 'PATROCINIO' THEN 1 ELSE 0 END) AS patrocinio,
        SUM(CASE WHEN p.projectClassification = 'TERMO_DE_COOPERACAO' THEN 1 ELSE 0 END) AS termoDeCooperacao,
        SUM(CASE WHEN p.projectClassification = 'TERMO_DE_OUTORGA' THEN 1 ELSE 0 END) AS termoDeOutorga
    FROM Project p
    WHERE (:projectCompany IS NULL OR p.projectCompany LIKE %:projectCompany%) AND
          (:projectStartDate IS NULL OR p.projectStartDate >= :projectStartDate) AND
          (:projectEndDate IS NULL OR p.projectEndDate <= :projectEndDate)
    """)
    List<Object[]> countProjectsByClassificationForCompany(
            @Param("projectCompany") String projectCompany,
            @Param("projectStartDate") LocalDate projectStartDate,
            @Param("projectEndDate") LocalDate projectEndDate);


    @Query("""
    SELECT FUNCTION('MONTH', p.projectStartDate), COUNT(p)
    FROM Project p
    WHERE (:projectCompany IS NULL OR p.projectCompany LIKE %:projectCompany%) AND
          (:projectStartDate IS NULL OR p.projectStartDate >= :projectStartDate) AND
          (:projectEndDate IS NULL OR p.projectEndDate <= :projectEndDate)
          AND (p.projectStartDate IS NOT NULL AND p.projectStartDate IS NOT NULL)
    GROUP BY FUNCTION('MONTH', p.projectStartDate)
    """)
    List<Object[]> countProjectsByMonthForCompany(
            @Param("projectCompany") String projectCompany,
            @Param("projectStartDate") LocalDate projectStartDate,
            @Param("projectEndDate") LocalDate projectEndDate);

    @Query("""
    SELECT SUM(p.projectValue) FROM Project p
    WHERE (:projectCompany IS NULL OR p.projectCompany LIKE %:projectCompany%) AND
          (:projectStartDate IS NULL OR p.projectStartDate >= :projectStartDate) AND
          (:projectEndDate IS NULL OR p.projectEndDate <= :projectEndDate)
          AND (p.projectStartDate IS NOT NULL AND p.projectEndDate IS NOT NULL)
    """)
    Long calculateTotalInvestmentByCompany(
            @Param("projectCompany") String projectCompany,
            @Param("projectStartDate") LocalDate projectStartDate,
            @Param("projectEndDate") LocalDate projectEndDate);

    @Query("""
    SELECT p FROM Project p
    JOIN p.coordinators c
    WHERE 
        (:keyword IS NULL OR 
            p.projectReference LIKE %:keyword% OR
            p.projectCompany LIKE %:keyword% OR
            c.coordinatorName LIKE %:keyword%) 
        AND (:projectStartDate IS NULL OR p.projectStartDate >= :projectStartDate)
        AND (:projectEndDate IS NULL OR p.projectEndDate <= :projectEndDate)
        AND (:status IS NULL OR p.projectStatus = :status)
        AND (:classification IS NULL OR p.projectClassification = :classification)
""")
    List<Project> filterProjectsKeyWord(
            @Param("keyword") String keyword,
            @Param("projectStartDate") LocalDate projectStartDate,
            @Param("projectEndDate") LocalDate projectEndDate,
            @Param("status") ProjectStatus status,
            @Param("classification") ProjectClassification classification
    );

}