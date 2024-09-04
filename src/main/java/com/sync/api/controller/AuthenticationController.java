package com.sync.api.controller;

import com.sync.api.repository.UsuarioRepository;
import com.sync.api.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(String email, String password) {
        return ResponseEntity.ok(authenticationService.authenticateUser(email, password));
    }

    @PostMapping("/register/usuario")
    public ResponseEntity<?> registerUser(String email, String password) {
        return ResponseEntity.ok(authenticationService.registrarUsuario(email, password));
    }
}
