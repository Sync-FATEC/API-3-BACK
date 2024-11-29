package com.sync.api.web.dto.documents;

import com.sync.api.domain.enums.FileType;
import com.sync.api.domain.model.Documents;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;

@AllArgsConstructor
@Data
public class DocumentListDTO extends RepresentationModel<Documents> {

    public String documentId;
    public String FileName;
    public FileType fileType;
    public String fileUrl;
    public LocalDate uploadedAt;
    public byte[] fileBytes;
    public boolean removed;
}

