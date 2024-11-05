package com.sync.api.application.service;

import com.sync.api.domain.enums.PapeisUsuario;
import com.sync.api.web.exception.SystemContextException;
import com.sync.api.infra.security.TokenService;
import com.sync.api.domain.model.User;
import com.sync.api.infra.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService
{
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenService tokenService;

    // Method to authenticate a user
    public String authenticateUser(String email, String password) throws SystemContextException {
        var bcrypt = new BCryptPasswordEncoder();

        // Make sure the user exists
        var usuario = this.userRepository.findByLogin(email);
        if (usuario == null) {
            throw new SystemContextException("Usuário ou senha inválidos");
        }

        var senhaValida = bcrypt.matches(password, usuario.getPassword());

        if(senhaValida) {
            return tokenService.generateToken(usuario);
        } else {
            throw new SystemContextException("Usuário ou senha inválidos");
        }
    }

    // Method to register a user
    public User registrarUsuario(String email, String senha) throws SystemContextException {
        if(this.userRepository.findByLogin(email) != null) {
            throw new SystemContextException("Usuário já cadastrado");
        }

        String senhaCriptografada = new BCryptPasswordEncoder().encode(senha);

        var usuario = new User(email, senhaCriptografada);

        return userRepository.save(usuario);
    }

    // Method to register an admin
    public User registrarAdmin() throws SystemContextException {
        String emailAdmin = "admin@admin.com";
        String senhaAdmin = "admin";

        // Check if the admin already exists
        if (this.userRepository.findByLogin(emailAdmin) != null) {
            throw new SystemContextException("Administrador já cadastrado");
        }

        String senhaCriptografada = new BCryptPasswordEncoder().encode(senhaAdmin);

        var admin = new User(emailAdmin, senhaCriptografada);
        admin.setRole(PapeisUsuario.ADMIN);
        admin.setUserAdmin(true);

        return userRepository.save(admin);
    }


    public boolean verifyLoggedIn() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        } else {
            Object principal = authentication.getPrincipal();
            if (principal instanceof User) {
                return authentication.isAuthenticated();
            } else {
                return false;
            }
        }
    }

    public User getLoggedUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null) {
            return null;
        } else {
            return (User) authentication.getPrincipal();
        }
    }

}
