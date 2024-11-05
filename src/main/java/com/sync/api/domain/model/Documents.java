package com.sync.api.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sync.api.domain.enums.FileType;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;


import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Table(name = "documents")
public class Documents extends RepresentationModel<Documents> {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public String documents_id;
    public String fileName;
    @Enumerated(EnumType.STRING)
    public FileType fileType;
    public String fileUrl;
    @Column(nullable = true)
    public String filePath;
    public LocalDate uploadedAt;
    @Column(columnDefinition = "boolean default false")
    public boolean removed;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "project_id")
    public Project project;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id", nullable = true)
    public User user;

    @OneToMany(mappedBy = "documents", cascade = CascadeType.REMOVE)
    public List<HistoryProject> historyProjectList;


    public Documents CreateBaseProject(String fileName, FileType fileType, LocalDate uploadedAt, Project project, User user) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.uploadedAt = uploadedAt;
        this.project = project;
        return this;
    }

    public String GenereateUrl() {
        this.fileUrl = this.documents_id  + "-" + this.fileName;
        return this.fileUrl;
    }
}
