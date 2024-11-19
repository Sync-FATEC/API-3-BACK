package com.sync.api.domain.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Period;
import java.util.List;

@Data
@Entity
public class ScholarGrant {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String type;

    private Period duration;

    private String acting;

    @Column(columnDefinition = "tinyint(1) default 1")
    private boolean active;

    @OneToMany(mappedBy = "scholarGrant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ScholarShipHolder> scholarShipHolders;
}
