package com.sync.api.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
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

    @ManyToOne
    @JoinColumn(name = "grant_id")
    private ScholarGrant grant;

    public ScholarShipHolder(String name, String email, String cpf, String rg, String nationality, Address address, ScholarGrant grant) {
        this.name = name;
        this.email = email;
        this.cpf = cpf;
        this.rg = rg;
        this.nationality = nationality;
        this.address = address;
        this.grant = grant;
    }
}
