package com.sync.api.model;

import com.sync.api.enums.TiposAnexos;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Anexos {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public String id;
    public String nome;
    public TiposAnexos tipo;
    public String url;
    public LocalDateTime dateCreated;

    @ManyToOne
    public Projetos projeto;
}
