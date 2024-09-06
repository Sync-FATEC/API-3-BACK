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
    public Double valorDoProjeto;
    public String dataInicio;
    public String dataTermino;
    @Enumerated(EnumType.STRING)
    public SituacaoProjetos situacao;
    @Enumerated(EnumType.STRING)
    public ClassificacaoProjetos classificacao;

    @ManyToOne
    public Coordenadores coordenador;

    @OneToMany(mappedBy = "projeto", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Anexos> anexos;
}
