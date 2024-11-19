package com.sync.api.web.controller;

import com.sync.api.application.service.GrantService;
import com.sync.api.domain.model.Grant;
import com.sync.api.web.dto.grant.GrantDto;
import com.sync.api.web.dto.grant.GrantResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.Repository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/grant")
public class GrantController {

    @Autowired
    private GrantService grantService;

    @PostMapping("/create")
    public ResponseEntity<?> createGrant(@RequestBody GrantDto grantDto){
        try {
            System.out.println(grantDto);
            Grant grant = grantService.createGrant(grantDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Bolsa Criada");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Dados inválidos: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado: " + e.getMessage());
        }
    }

    @GetMapping("/get/{id}")
    public  ResponseEntity<?> getGrant(@PathVariable String id){
        try {
            GrantResponseDto grant = grantService.getGrant(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(grant);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Dados inválidos: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado: " + e.getMessage());
        }
    }
    @GetMapping("/getAll")
    public ResponseEntity<?> getAllGrant(){
        try {
            List<GrantResponseDto> grantList = grantService.getAllGrant();
            if(grantList.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Sem bolsas cadastradas");
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body(grantList);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Dados inválidos: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado: " + e.getMessage());
        }
    }

    @PatchMapping("/deactivate/{id}")
    public ResponseEntity<?> deactivateGrant(@PathVariable String id){
        try{
            grantService.deactiveteGrant(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Bolsa desativada");
        }catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Dados inválidos: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado: " + e.getMessage());
        }
    }
}
