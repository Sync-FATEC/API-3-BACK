package com.sync.api.controller;

import com.sync.api.dto.projetcs.CoordinatorDTO;
import com.sync.api.dto.projetcs.ProjectsDTO;
import com.sync.api.dto.web.ResponseModelDTO;
import com.sync.api.enums.ClassificacaoProjetos;
import com.sync.api.enums.SituacaoProjetos;
import com.sync.api.exception.SystemContextException;
import com.sync.api.model.Projetos;
import com.sync.api.repository.CoordenadorRepository;
import com.sync.api.repository.ProjetosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.sync.api.model.Coordenadores;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/projetos")
public class ProjetosController {

    @Autowired
    private ProjetosRepository projetosRepository;

    @Autowired
    private CoordenadorRepository coordenadorRepository;

    @GetMapping("/listar")
    public ResponseEntity<?> listarProjetos() {
        List<ProjectsDTO> projects = projetosRepository.findAll()
                .stream()
                .map(ProjectsDTO::new)
                .collect(Collectors.toList());
        var response = new ResponseModelDTO(projects);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/listar/{id}")
    public ResponseEntity<?> listarProjetoPorId(@PathVariable("id") String id) throws SystemContextException {
        Optional<Projetos> projeto = projetosRepository.findById(id);
        if (projeto.isPresent()) {
            ProjectsDTO projectsDTO = new ProjectsDTO(projeto.get());
            var response = new ResponseModelDTO(projectsDTO);
            return ResponseEntity.ok().body(response);
        } else {
            throw new SystemContextException("Projeto não encontrado");
        }
    }

    @GetMapping("/listarCoordenadores")
    public ResponseEntity<?> listarCoordenadores() {
        List<CoordinatorDTO> coordenadores = coordenadorRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Coordenadores::getNome))
                .map(coordenador -> new CoordinatorDTO(coordenador.getId(), coordenador.getNome(), coordenador.getUsuario()))
                .collect(Collectors.toList());
        var response = new ResponseModelDTO(coordenadores);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrarProjeto(@RequestBody ProjectsDTO projectsDTO) throws SystemContextException {
        if (projectsDTO == null) {
            throw new SystemContextException("Projeto inválido");
        }

        Projetos projeto = new Projetos();
        projeto.setId(projectsDTO.id);
        projeto.setReferenciaDoProjeto(projectsDTO.referenciaDoProjeto);
        projeto.setEmpresa(projectsDTO.empresa);
        projeto.setObjetivo(projectsDTO.objetivo);
        projeto.setDescricao(projectsDTO.descricao);
        projeto.setValorDoProjeto(projectsDTO.valorDoProjeto);
        projeto.setDataInicio(projectsDTO.dataInicio);
        projeto.setDataTermino(projectsDTO.dataTermino);
        projeto.setSituacao(SituacaoProjetos.valueOf(projectsDTO.situacao));
        projeto.setClassificacao(ClassificacaoProjetos.valueOf(projectsDTO.classificacao));
        projeto.setAnexos(projectsDTO.anexos);

        if (projectsDTO.coordenador == null || projectsDTO.coordenador.getId() == null) {
            coordenadorRepository.findByNome("SEM COORDENADOR").ifPresent(projeto::setCoordenador);
        } else {
            coordenadorRepository.findById(projectsDTO.coordenador.getId()).ifPresent(projeto::setCoordenador);
        }

        Projetos projetoSalvo = projetosRepository.save(projeto);
        var response = new ResponseModelDTO(projetoSalvo);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<?> deletarProjeto(@PathVariable("id") String id) {
        projetosRepository.deleteById(id);
        var response = new ResponseModelDTO("Projeto deletado com sucesso");
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/atualizar")
    public ResponseEntity<?> atualizarProjeto(@RequestBody ProjectsDTO projectsDTO) throws SystemContextException {
        if (projectsDTO == null || projectsDTO.id == null) {
            throw new SystemContextException("Projeto inválido");
        }

        Optional<Projetos> projeto = projetosRepository.findById(projectsDTO.id);
        if (projeto.isPresent()) {
            projeto.get().setReferenciaDoProjeto(projectsDTO.referenciaDoProjeto);
            projeto.get().setEmpresa(projectsDTO.empresa);
            projeto.get().setObjetivo(projectsDTO.objetivo);
            projeto.get().setDescricao(projectsDTO.descricao);
            projeto.get().setValorDoProjeto(projectsDTO.valorDoProjeto);
            projeto.get().setDataInicio(projectsDTO.dataInicio);
            projeto.get().setDataTermino(projectsDTO.dataTermino);
            projeto.get().setSituacao(SituacaoProjetos.valueOf(projectsDTO.situacao));
            projeto.get().setClassificacao(ClassificacaoProjetos.valueOf(projectsDTO.classificacao));
            projeto.get().setAnexos(projectsDTO.anexos);

            if (projectsDTO.coordenador == null || projectsDTO.coordenador.getId() == null) {
                coordenadorRepository.findByNome("SEM COORDENADOR").ifPresent(projeto.get()::setCoordenador);
            } else {
                coordenadorRepository.findById(projectsDTO.id).ifPresent(projeto.get()::setCoordenador);
            };

            Projetos projetoSalvo = projetosRepository.save(projeto.get());
            var response = new ResponseModelDTO(projetoSalvo);
            return ResponseEntity.ok().body(response);
        } else {
            throw new SystemContextException("Projeto não encontrado");
        }
    }
}