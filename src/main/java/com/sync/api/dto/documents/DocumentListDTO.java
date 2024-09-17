package com.sync.api.dto.documents;

import com.sync.api.enums.TiposAnexos;
import com.sync.api.model.Documents;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;

public class DocumentListDTO extends RepresentationModel<Documents> {

    public String documentId;
    public String FileName;
    public TiposAnexos fileType;
    public String fileUrl;
    public LocalDate uploadedAt;
}
