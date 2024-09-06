package com.sync.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TesteController
{

    @GetMapping("/teste")
    ResponseEntity<?> teste(){
        return ResponseEntity.ok("vc esta autenticado");
    }
}
