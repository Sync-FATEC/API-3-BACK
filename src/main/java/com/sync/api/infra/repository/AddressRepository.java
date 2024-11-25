package com.sync.api.infra.repository;

import com.sync.api.domain.model.Address;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, String> {
    @NotNull Optional<Address> findById(String id);
}
