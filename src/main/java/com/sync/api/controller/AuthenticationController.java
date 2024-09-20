package com.sync.api.controller;

import com.sync.api.dto.auth.LoginDTO;
import com.sync.api.dto.auth.RegisterModelDTO;
import com.sync.api.dto.auth.TokenDTO;
import com.sync.api.dto.web.ResponseModelDTO;
import com.sync.api.exception.SystemContextException;
import com.sync.api.infra.security.TokenService;
import com.sync.api.service.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private TokenService tokenService;

    // Method called automatically when the Spring context is initialized
    @EventListener(ContextRefreshedEvent.class)
    public void init() {
        try {
            logger.info("Verificando se usuário administrador existe...");
            authenticationService.registrarAdmin();
            logger.info("Usuário administrador criado com sucesso ou já existente.");
        } catch (SystemContextException e) {
            logger.error("Erro ao criar usuário administrador: {}", e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginDTO data) throws SystemContextException {
        var token = authenticationService.authenticateUser(data.email, data.password);

        var response = new ResponseModelDTO(new TokenDTO(token));

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterModelDTO data) {
        try {
            logger.info("Register request received for email: {}", data.email);
            var usuario = authenticationService.registrarUsuario(data.email, data.password);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
        } catch (SystemContextException e) {
            logger.error("Error during registration: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseModelDTO(e.getMessage()));
        }
    }

}
