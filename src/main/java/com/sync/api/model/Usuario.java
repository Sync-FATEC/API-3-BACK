package com.sync.api.model;

import com.sync.api.enums.PapeisUsuario;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public String id;
    public String email;
    private String login;
    public String senha;
    @Getter
    @Enumerated(EnumType.STRING)
    public PapeisUsuario role;

    public Usuario(String email, String senha) {
        this.email = email;
        this.login = email;
        this.senha = senha;
        this.role = PapeisUsuario.USER;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(this.role == PapeisUsuario.ADMIN)
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
        else
            return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return login;
    }

    public String getPayload() {
        String name = getUsername();
        String role = getRole().toString();
        return String.format("{'name': '%s', 'role': '%s'}", name, role);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
