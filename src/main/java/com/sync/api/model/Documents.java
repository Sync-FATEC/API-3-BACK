package com.sync.api.model;

import com.sync.api.enums.TiposAnexos;
import jakarta.persistence.*;
import lombok.Data;


import java.time.LocalDate;

@Entity
@Data
@Table(name = "documents")
public class Documents {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public String documentsId;
    public String fileName;
    @Enumerated(EnumType.STRING)
    public TiposAnexos fileType;
    public String fileUrl;
    public LocalDate uploadedAt;

    @ManyToOne
    @JoinColumn(name = "projectId")
    public Project project;

    @ManyToOne
    @JoinColumn(name = "userId")
    public User user;
}
