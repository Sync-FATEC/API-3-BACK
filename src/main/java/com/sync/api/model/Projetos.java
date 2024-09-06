package com.sync.api.model;

import com.sync.api.enums.ClassificacaoProjetos;
import com.sync.api.enums.SituacaoProjetos;
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
    public String referenciaDoProjeto;
    public String empresa;

    @Lob
    @Column(columnDefinition = "TEXT")
    public String objetivo;

    @Lob
    @Column(columnDefinition = "TEXT")

    public String descricao;
    public String coordenador;
    public Double valorDoProjeto;
    public String dataInicio;
    public String dataTermino;
    public SituacaoProjetos situacao;
    public ClassificacaoProjetos classificacao;

    @OneToMany(mappedBy = "projeto", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Anexos> propostas;

    @OneToMany(mappedBy = "projeto", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Anexos> anexos;
}
