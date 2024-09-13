package com.sync.api.dto.projetcs;

import com.sync.api.model.User;

public class CoordinatorDTO {
    public String id;
    public String name;
    public User user;

    public CoordinatorDTO(String id, String name, User user) {
        this.id = id;
        this.name = name;
        this.user = user;
    }
}
