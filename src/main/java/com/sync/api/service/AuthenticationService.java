package com.sync.api.service;

import com.sync.api.exception.SystemContextException;
import com.sync.api.infra.security.TokenService;
import com.sync.api.model.User;
import com.sync.api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
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
            return tokenService.generateToken((User) usuario);
        } else {
            throw new SystemContextException("Usuário ou senha inválidos");
        }
    }

    public User registrarUsuario(String email, String senha) throws SystemContextException {
        if(this.usuarioRepository.findByLogin(email) != null) {
            throw new SystemContextException("Usuário já cadastrado");
        }

        String senhaCriptografada = new BCryptPasswordEncoder().encode(senha);

        var usuario = new User(email, senhaCriptografada);

        return usuarioRepository.save(usuario);
    }
}