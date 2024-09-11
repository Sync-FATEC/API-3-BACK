package com.sync.api.dto.projetcs;

import com.sync.api.model.Anexos;
import com.sync.api.model.Coordenadores;
import com.sync.api.model.Projetos;

import java.util.List;

public class ProjectsDTO {
    public String id;
    public String referenciaDoProjeto;
    public String empresa;
    public String objetivo;
    public String descricao;
    public Double valorDoProjeto;
    public String dataInicio;
    public String dataTermino;
    public String situacao;
    public String classificacao;
    public Coordenadores coordenador;
    public List<Anexos> anexos;

    public ProjectsDTO(Projetos projeto) {
        this.id = projeto.getId();
        this.referenciaDoProjeto = projeto.getReferenciaDoProjeto();
        this.empresa = projeto.getEmpresa();
        this.objetivo = projeto.getObjetivo();
        this.descricao = projeto.getDescricao();
        this.valorDoProjeto = projeto.getValorDoProjeto();
        this.dataInicio = projeto.getDataInicio();
        this.dataTermino = projeto.getDataTermino();
        this.situacao = projeto.getSituacao().name();
        this.classificacao = projeto.getClassificacao().name();
        this.coordenador = projeto.getCoordenador();
        this.anexos = projeto.getAnexos();
    }
}
