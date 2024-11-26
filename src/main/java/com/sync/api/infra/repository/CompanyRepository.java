package com.sync.api.infra.repository;

import com.sync.api.domain.model.Company;
import com.sync.api.domain.model.Coordinators;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, String> {
    Optional<Company> findByCorporateName(String corporateName);

    Optional<Company> findById(String corporateId);

    @Query("SELECT c FROM Company c " +
            "LEFT JOIN c.address a " +
            "WHERE " +
            "LOWER(c.corporateName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.cnpj) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.phone) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(a.street) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(a.city) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(a.state) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(a.zipCode) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "ORDER BY c.corporateName ASC")
    List<Company> findByAnyFieldOrAddress(@Param("keyword") String keyword);


}
