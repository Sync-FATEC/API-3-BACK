package com.sync.api.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    @OneToMany(mappedBy = "grant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ScholarShipHolder> scholarShipHolders;
}
