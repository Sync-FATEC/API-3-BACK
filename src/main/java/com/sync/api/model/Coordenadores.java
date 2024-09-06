package com.sync.api.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(of = "id")
public class Coordenadores {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public String id;
    public String nome;

    @ManyToOne
    public Projetos projeto;

    @OneToOne
    public Usuario usuario;
}
