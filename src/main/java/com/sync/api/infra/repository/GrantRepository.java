package com.sync.api.infra.repository;


import com.sync.api.domain.model.ScholarGrant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GrantRepository extends JpaRepository<ScholarGrant, String> {
}
