package com.sync.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sync.api.enums.TiposAnexos;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;


import java.time.LocalDate;

@Entity
@Data
@Table(name = "documents")
public class Documents extends RepresentationModel<Documents> {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public String documents_id;
    public String fileName;
    @Enumerated(EnumType.STRING)
    public TiposAnexos fileType;
    public String fileUrl;
    public LocalDate uploadedAt;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "project_id")
    public Project project;

    @ManyToOne
    @JoinColumn(name = "user_id")
    public User user;

    public Documents CreateBaseProject(String fileName, TiposAnexos fileType, LocalDate uploadedAt, Project project, User user) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.uploadedAt = uploadedAt;
        this.project = project;
        this.user = user;
        return this;
    }

    public String GenereateUrl() {
        this.fileUrl = this.documents_id  + "-" + this.fileName;
        return this.fileUrl;
    }
}
