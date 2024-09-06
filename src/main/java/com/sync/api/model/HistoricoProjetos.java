package com.sync.api.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@EqualsAndHashCode(of = "id")
public class HistoricoProjetos {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public String id;
    public String CamposAlterados;
    public LocalDateTime dataAlteracao;

    @ManyToOne
    public Projetos projeto;

    @ManyToOne
    public Usuario usuario;
}
