package com.sync.api.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class PlanWordFAPG {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToOne
    @JoinColumn(name = "project_id")
    private Project project;

    private String projectReference;

    @Lob
    @Column(name = "plan_word_file", columnDefinition = "LONGBLOB", nullable = false)
    private byte[] planWordFile;

    public PlanWordFAPG(byte[] planWord, String projectReference, Project project) {
        this.planWordFile = planWord;
        this.projectReference = projectReference;
        this.project = project;
    }
}
