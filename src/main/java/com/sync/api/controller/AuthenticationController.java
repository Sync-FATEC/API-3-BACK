package com.sync.api.controller;

import com.sync.api.dto.auth.LoginDTO;
import com.sync.api.dto.auth.RegisterModelDTO;
import com.sync.api.dto.auth.TokenDTO;
import com.sync.api.dto.web.ResponseModelDTO;
import com.sync.api.exception.SystemContextException;
import com.sync.api.infra.security.TokenService;
import com.sync.api.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginDTO data) throws SystemContextException {
        var token = authenticationService.authenticateUser(data.email, data.password);

        var response = new ResponseModelDTO(new TokenDTO(token));

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterModelDTO data) throws SystemContextException {

        var usuario = authenticationService.registrarUsuario(data.email, data.password);

        return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
    }

}
