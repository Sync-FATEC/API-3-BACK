package com.sync.api.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Entity
@Data
@EqualsAndHashCode(of = "id")
public class Projetos {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public String id;
    private String referenciaDoProjeto;
    private String empresa;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String objetivo;

    @Lob
    @Column(columnDefinition = "TEXT")

    private String descricao;
    private String coordenador;
    private String valorDoProjeto;
    private String dataInicio;
    private String dataTermino;
    @OneToMany(mappedBy = "projeto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Proposta> propostas;
    @OneToMany(mappedBy = "projeto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Contrato> contratos;
    @OneToMany(mappedBy = "projeto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Artigo> artigos;
}
