package com.sync.api.controller;

import com.sync.api.dto.web.ResponseModelDTO;
import com.sync.api.enums.TiposAnexos;
import com.sync.api.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @GetMapping("/get/{filename}")
    public byte[] getFile(@PathVariable String filename) throws IOException {
        Path filePath = Paths.get("/uploads/" + filename);
        return Files.readAllBytes(filePath);
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadDocument(@RequestParam("file") MultipartFile file, @RequestParam("projectId") String projectId, @RequestParam("fileType") TiposAnexos fileType) throws Exception {


       documentService.upload(file, projectId, fileType);
        return ResponseEntity.ok(new ResponseModelDTO("File uploaded successfully"));
    }
}