package com.sync.api.infra.repository;

import com.sync.api.domain.model.ScholarShipHolder;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScholarShipHolderRepository extends JpaRepository<ScholarShipHolder, String>{
    @NotNull Optional<ScholarShipHolder> findById(@NotNull String id);
}