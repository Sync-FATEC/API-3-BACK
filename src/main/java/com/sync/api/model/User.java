package com.sync.api.model;

import com.google.gson.Gson;
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
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public String userId;
    public String userEmail;
    public String userCpf;
    public Boolean userAdmin;
    private String login;
    public String userPassword;
    @Enumerated(EnumType.STRING)
    public PapeisUsuario role;


    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    public List<Documents> documents;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    public List<Project> projectList;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    public List<HistoryProject> historyProjectList;


    public User(String userEmail, String userPassword) {
        this.userEmail = userEmail;
        this.login = userEmail;
        this.userPassword = userPassword;
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
        return this.userPassword;
    }

    @Override
    public String getUsername() {
        return login;
    }

    public String getPayload() {
        String name = getUsername();
        String role = getRole().toString();
        Object payload = Map.of("name", name, "role", role);
        Gson gson = new Gson();
        return String.format(gson.toJson(payload));
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
