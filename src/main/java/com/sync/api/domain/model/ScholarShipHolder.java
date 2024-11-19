package com.sync.api.domain.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ScholarShipHolder {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;
    private String email;
    private String cpf;
    private String rg;
    private String nationality;
    private boolean removed;

    @OneToOne
    @JoinColumn(name = "address_id")
    private Address address;

    @ManyToOne(optional = false)
    @JoinColumn(name = "grant_id", nullable = false)
    private Grant grant;
}
