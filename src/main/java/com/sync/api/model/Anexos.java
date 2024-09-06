package com.sync.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sync.api.enums.TiposAnexos;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Anexos {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public String id;
    public String nome;
    @Enumerated(EnumType.STRING)
    public TiposAnexos tipo;
    public String url;

    @JsonIgnore
    @ManyToOne
    public Projetos projeto;
}
