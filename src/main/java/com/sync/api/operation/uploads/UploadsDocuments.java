package com.sync.api.operation.uploads;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class UploadsDocuments {

    @Value("${file.upload-dir:/uploads}")
    private String uploadDir;

    public String uploadFile(MultipartFile file) throws  IOException{
            Path directoryPath = Paths.get(uploadDir);
            System.out.println("Diretório de upload: " + directoryPath.toAbsolutePath());
            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }

        String uniqueId = UUID.randomUUID().toString();
        String fileExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String uniqueFileName = uniqueId + fileExtension;
        Path filePath = directoryPath.resolve(uniqueFileName).normalize();
        System.out.println("Caminho do arquivo: " + filePath);

        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        return filePath.toString().replace("\\", "/");
    }

    public File getDocumento(String filePath) {
        File file = new File(filePath);
        if (file.exists() && file.isFile()) {
            return file;
        } else {
            throw new RuntimeException("Arquivo não encontrado: " + filePath);
        }
    }
}
