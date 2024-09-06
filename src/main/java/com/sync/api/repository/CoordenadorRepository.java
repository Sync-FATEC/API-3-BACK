package com.sync.api.repository;

import com.sync.api.model.Coordenadores;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CoordenadorRepository extends JpaRepository<Coordenadores, String> {
    Optional<Coordenadores> findByNome(String nome);
}