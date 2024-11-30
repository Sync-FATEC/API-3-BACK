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


    @PostMapping("generate")
    public ResponseEntity<?> generateContract(@RequestBody String projectId) {
        try {
            contractService.generateFPAGContract(projectId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseModelDTO(HttpStatus.BAD_REQUEST, e.getMessage()));
        }
    }
}
