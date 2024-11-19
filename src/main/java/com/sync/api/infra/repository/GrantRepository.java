package com.sync.api.infra.repository;

import com.sync.api.domain.model.Grant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GrantRepository extends JpaRepository<Grant, String> {
}
