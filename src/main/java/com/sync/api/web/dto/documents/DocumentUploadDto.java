package com.sync.api.web.dto.documents;

import com.sync.api.domain.enums.FileType;
import org.springframework.web.multipart.MultipartFile;

public record DocumentUploadDto(
         MultipartFile file,
         FileType typeFile
        ) {
}
