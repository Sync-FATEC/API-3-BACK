package com.sync.api.infra.repository;

import com.sync.api.domain.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, String> {
    Optional<Company> findByCorporateName(String corporateName);
}
