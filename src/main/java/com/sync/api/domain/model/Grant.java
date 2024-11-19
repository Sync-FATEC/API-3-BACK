package com.sync.api.domain.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Period;
import java.util.List;

@Data
@Entity
@Table(name = "grants")
public class Grant {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String type;

    private Period duration;

    private String acting;

    @Column(columnDefinition = "tinyint(1) default 1")
    private boolean active;

    @OneToMany(mappedBy = "grant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ScholarShipHolder> scholarShipHolders;
}
