package com.sync.api.service;

import com.sync.api.infra.security.TokenService;
import com.sync.api.model.Usuario;
import com.sync.api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService
{
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private TokenService tokenService;

    public String authenticateUser(String email, String password) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(email, password);
        var auth = this.authenticationManager.authenticate(usernamePassword);

        return tokenService.generateToken((Usuario) auth.getPrincipal());
    }

    public Usuario registrarUsuario(String email, String senha) {
        if(this.usuarioRepository.findByLogin(email) != null) {
            throw new IllegalArgumentException("Usuário já cadastrado");
        }

        String senhaCriptografada = new BCryptPasswordEncoder().encode(senha);

        var usuario = new Usuario(email, senhaCriptografada);

        return usuarioRepository.save(usuario);
    }
}
