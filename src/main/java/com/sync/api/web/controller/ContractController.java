package com.sync.api.web.controller;

import com.sync.api.application.service.ContractService;
import com.sync.api.web.dto.web.ResponseModelDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contract")
public class ContractController {

    @Autowired
    private ContractService contractService;

    @GetMapping("")
    public ResponseEntity<ResponseModelDTO> getContracts() {
        try {
            return ResponseEntity.ok(new ResponseModelDTO(contractService.listContracts()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseModelDTO(HttpStatus.BAD_REQUEST, e.getMessage()));
        }
    }

    @GetMapping("download/{id}")
    public ResponseEntity<ResponseModelDTO> downloadContract(String id) {
        try {
            return ResponseEntity.ok(new ResponseModelDTO(contractService.getContract(id)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseModelDTO(HttpStatus.BAD_REQUEST, e.getMessage()));
        }
    }

    @PostMapping("generate")
    public ResponseEntity<ResponseModelDTO> generateContract(@RequestBody String projectId) {
        try {
            return ResponseEntity.ok(new ResponseModelDTO(contractService.generateFPAGContract(projectId)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseModelDTO(HttpStatus.BAD_REQUEST, e.getMessage()));
        }
    }
}
