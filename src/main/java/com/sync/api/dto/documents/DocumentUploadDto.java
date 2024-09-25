package com.sync.api.dto.documents;

import com.sync.api.enums.FileType;
import org.springframework.web.multipart.MultipartFile;

public record DocumentUploadDto(
         MultipartFile file,
         FileType typeFile
        ) {
}
