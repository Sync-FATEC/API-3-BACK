package com.sync.api.dto.projetcs;

import com.sync.api.model.Usuario;

public class CoordinatorDTO {
    public String id;
    public String name;
    public Usuario user;

    public CoordinatorDTO(String id, String name, Usuario user) {
        this.id = id;
        this.name = name;
        this.user = user;
    }
}
