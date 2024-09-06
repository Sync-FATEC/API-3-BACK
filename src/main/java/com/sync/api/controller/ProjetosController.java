package com.sync.api.controller;

import com.sync.api.model.Projetos;
import com.sync.api.repository.CoordenadorRepository;
import com.sync.api.repository.ProjetosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/projetos")
public class ProjetosController {

    @Autowired
    private ProjetosRepository projetosRepository;

    @Autowired
    private CoordenadorRepository coordenadorRepository;

    @GetMapping("/listar")
    public ResponseEntity<?> listarProjetos() {
        return ResponseEntity.ok(projetosRepository.findAll());
    }

    @GetMapping("/listar/{id}")
    public ResponseEntity<?> listarProjetoPorId(@PathVariable("id") String id) {
        Optional<Projetos> projeto = projetosRepository.findById(id);
        return projeto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrarProjeto(@RequestBody Projetos projeto) {
        if (projeto == null) {
            return ResponseEntity.badRequest().body("Projeto inválido");
        }

        if (projeto.coordenador == null || projeto.coordenador.id == null) {
            coordenadorRepository.findByNome("SEM COORDENADOR").ifPresent(projeto::setCoordenador);
        }

        if (projeto.coordenador.id != null) {
            coordenadorRepository.findById(projeto.coordenador.id).ifPresent(projeto::setCoordenador);
        }

        Projetos projetoSalvo = projetosRepository.save(projeto);
        return ResponseEntity.status(HttpStatus.CREATED).body(projetoSalvo);
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<?> deletarProjeto(@PathVariable("id") String id) {
        projetosRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/atualizar")
    public ResponseEntity<?> atualizarProjeto(@RequestBody Projetos projeto) {
        if (projeto == null || projeto.id == null) {
            return ResponseEntity.badRequest().body("Projeto inválido");
        }

        if (!projetosRepository.existsById(projeto.id)) {
            return ResponseEntity.notFound().build();
        }

        if (projeto.coordenador == null || projeto.coordenador.id == null) {
            coordenadorRepository.findByNome("SEM COORDENADOR").ifPresent(projeto::setCoordenador);
        }

        if (projeto.coordenador.id != null) {
            coordenadorRepository.findById(projeto.coordenador.id).ifPresent(projeto::setCoordenador);
        }

        Projetos projetoSalvo = projetosRepository.save(projeto);
        return ResponseEntity.ok(projetoSalvo);
    }
}