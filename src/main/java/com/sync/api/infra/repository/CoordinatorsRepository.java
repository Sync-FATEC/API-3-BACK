package com.sync.api.infra.repository;

import com.sync.api.domain.model.Coordinators;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CoordinatorsRepository extends JpaRepository<Coordinators, Long> {

    Optional<Coordinators> findByCoordinatorName(String name);

    Optional<Coordinators> findByCoordinatorId(String id);

    void deleteByCoordinatorId(String id);

    @Query("SELECT c FROM Coordinators c WHERE " +
            "LOWER(c.coordinatorName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.coordinatorCPF) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.coordinatorTelefone) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.coordinatorEconomicActivity) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Coordinators> findByAnyField(@Param("keyword") String keyword);
}
