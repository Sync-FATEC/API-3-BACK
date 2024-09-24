package com.sync.api.dto.documents;

import com.sync.api.enums.TiposAnexos;
import org.springframework.web.multipart.MultipartFile;

public record DocumentUploadDto(
         MultipartFile file,
         TiposAnexos typeFile
        ) {
}
