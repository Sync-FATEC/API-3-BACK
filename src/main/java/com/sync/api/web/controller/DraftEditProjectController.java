package com.sync.api.web.controller;

import com.sync.api.application.service.DraftEditProjectService;
import com.sync.api.web.dto.web.ResponseModelDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/draft")
public class DraftEditProjectController {

    @Autowired
    private DraftEditProjectService draftEditProjectService;

    @GetMapping("/get")
    public ResponseEntity<ResponseModelDTO> getDrafts() {
        return ResponseEntity.ok(new ResponseModelDTO(draftEditProjectService.getAll()));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ResponseModelDTO> getDraft(@PathVariable String id) {
        return ResponseEntity.ok(new ResponseModelDTO(draftEditProjectService.getById(id)));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseModelDTO> deleteDraft(@PathVariable String id) {
        draftEditProjectService.delete(id);
        return ResponseEntity.ok(new ResponseModelDTO("Draft deletado com sucesso"));
    }
}
