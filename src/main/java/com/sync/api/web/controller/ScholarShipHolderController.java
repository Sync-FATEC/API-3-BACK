package com.sync.api.web.controller;

import com.sync.api.application.service.ScholarShipHolderService;
import com.sync.api.domain.model.ScholarShipHolder;
import com.sync.api.web.dto.ScholarShipHolder.RegisterScholarShipHolderDto;
import com.sync.api.web.dto.ScholarShipHolder.UpdateScholarShipHolderDto;
import com.sync.api.web.dto.web.ResponseModelDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/scholarship-holders")
public class ScholarShipHolderController {
    @Autowired
    private ScholarShipHolderService scholarShipHolderService;

    @PostMapping("/create")
    public ResponseEntity<?> createScholarShipHolder(@RequestBody RegisterScholarShipHolderDto dto){
        try {
            ScholarShipHolder scholarShipHolder = scholarShipHolderService.createScholarShip(dto);
            return ResponseEntity.ok(new ResponseModelDTO(scholarShipHolder));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body( new ResponseModelDTO(HttpStatus.BAD_REQUEST, e.getMessage()));
        }
    }
    @PutMapping("/update")
    public ResponseEntity<?> updateScholarShipHolder(@RequestBody UpdateScholarShipHolderDto dto){
        try {
            System.out.println(dto);
            ScholarShipHolder scholarShipHolder = scholarShipHolderService.updateScholarShipHolder(dto);
            return ResponseEntity.ok(new ResponseModelDTO(scholarShipHolder));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body( new ResponseModelDTO(HttpStatus.BAD_REQUEST, e.getMessage()));
        }
    }
    @GetMapping("/buscar/{id}")
    public ResponseEntity<?> getScholarShipHolder(@PathVariable String id){
        try {
            ScholarShipHolder scholarShipHolder = scholarShipHolderService.getScholarShipHolder(id);
            return ResponseEntity.ok(new ResponseModelDTO(scholarShipHolder));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body( new ResponseModelDTO(HttpStatus.BAD_REQUEST, e.getMessage()));
        }
    }
    @GetMapping("/buscar")
    public ResponseEntity<?> getAllScholarShipHolder(){
        try {
            return ResponseEntity.ok(new ResponseModelDTO(scholarShipHolderService.getAllGrant()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body( new ResponseModelDTO(HttpStatus.BAD_REQUEST, e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteScholarShipHolder(@PathVariable String id){
        try {
            scholarShipHolderService.removeScholarShip(id);
            return ResponseEntity.ok(new ResponseModelDTO("Bolsista deletado com sucesso"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body( new ResponseModelDTO(HttpStatus.BAD_REQUEST, e.getMessage()));
        }
    }
}
