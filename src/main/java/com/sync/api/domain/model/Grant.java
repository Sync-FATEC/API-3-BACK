package com.sync.api.domain.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Duration;
import java.util.List;

@Data
@Entity
public class Grant {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String type;

    private Duration duration;

    private String acting;

    @OneToMany(mappedBy = "grant")
    private List<ScholarShipHolder> scholarShipHolders;
}
