package com.sync.api.domain.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "company")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String corporateName;
    @Column(unique = true)
    private String cnpj;
    @Column(unique = true)
    private String phone;
    private boolean privateCompany;

    @OneToOne
    @JoinColumn(name = "address_id")
    private Address address;
}
