package com.sync.api.service;

import com.sync.api.exception.SystemContextException;
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

    public String authenticateUser(String email, String password) throws SystemContextException {
        var bcrypt = new BCryptPasswordEncoder();

        var usuario = this.usuarioRepository.findByLogin(email);

        var senhaValida = bcrypt.matches(password, usuario.getPassword());

        if(senhaValida) {
            return tokenService.generateToken((Usuario) usuario);
        } else {
            throw new SystemContextException("Usuário ou senha inválidos");
        }
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