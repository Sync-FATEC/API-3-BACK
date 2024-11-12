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

    @OneToOne(mappedBy = "scholarShipHolder")
    private Address address;

    @ManyToOne
    @JoinColumn(name = "grant_id")
    private Grant grant;
}
