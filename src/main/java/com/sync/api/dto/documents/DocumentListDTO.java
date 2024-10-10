package com.sync.api.dto.documents;

import com.sync.api.enums.FileType;
import com.sync.api.model.Documents;
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
    public boolean removed;
}
