package com.sync.api.web.controller;

import com.sync.api.application.service.GrantService;
import com.sync.api.domain.model.ScholarGrant;
import com.sync.api.web.dto.grant.GrantDto;
import com.sync.api.web.dto.grant.GrantResponseDto;
import com.sync.api.web.dto.grant.UpdateGrantDto;
import com.sync.api.web.dto.web.ResponseModelDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
            ScholarGrant grant = grantService.createGrant(grantDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ResponseModelDTO(grant));
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
                    .body(new ResponseModelDTO(grant));
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

    @PutMapping("/update")
    public ResponseEntity<?> updateGrant(@RequestBody UpdateGrantDto grantDto){
        try {
            ScholarGrant grant = grantService.updateGrant(grantDto);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseModelDTO(grant));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Dados inválidos: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado: " + e.getMessage());
        }
    }
}
