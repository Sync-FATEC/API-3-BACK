package com.sync.api.dto;

import com.sync.api.enums.TiposAnexos;
import com.sync.api.model.Project;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record DocumentUploadDto(
         MultipartFile file,
         TiposAnexos typeFile
        ) {
}
