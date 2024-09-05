package com.sync.api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Contrato {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public String id;
    public String pdfPath;

    @ManyToOne
    private Projetos projeto;
}
