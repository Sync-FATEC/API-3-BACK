package com.sync.api.web.controller;

import com.sync.api.application.service.CoordinatorsService;
import com.sync.api.web.dto.coordinators.RegisterCoordinatorsDTO;
import com.sync.api.web.dto.coordinators.UpdateCoordinatorsDTO;
import com.sync.api.web.dto.web.ResponseModelDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/coordinators")
public class CoordinatorsController {
    @Autowired
    private CoordinatorsService coordinatorsService;

    @PostMapping("/create")
    public ResponseEntity<ResponseModelDTO> createCoordinator(@RequestBody RegisterCoordinatorsDTO registerCoordinatorsDTO) {
        try {
            return ResponseEntity.ok(new ResponseModelDTO(coordinatorsService.createCoordinators(registerCoordinatorsDTO)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseModelDTO(HttpStatus.BAD_REQUEST ,e.getMessage()));
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseModelDTO> updateCoordinator(@RequestBody UpdateCoordinatorsDTO updateCoordinatorsDTO) {
        try {
            return ResponseEntity.ok(new ResponseModelDTO(coordinatorsService.updateCoordinator(updateCoordinatorsDTO)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseModelDTO(HttpStatus.BAD_REQUEST, e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseModelDTO> deleteCoordinator(@PathVariable Long id) {
        try {
            coordinatorsService.deleteCoordinator(id);
            return ResponseEntity.ok(new ResponseModelDTO("Coordenador deletado com sucesso."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseModelDTO(HttpStatus.BAD_REQUEST ,e.getMessage()));
        }
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<ResponseModelDTO> getCoordinator(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(new ResponseModelDTO(coordinatorsService.getCoordinator(id)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseModelDTO(HttpStatus.BAD_REQUEST ,e.getMessage()));
        }
    }

    @GetMapping("/buscar")
    public ResponseEntity<ResponseModelDTO> getCoordinators() {
        try {
            return ResponseEntity.ok(new ResponseModelDTO(coordinatorsService.getCoordinators()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseModelDTO(HttpStatus.BAD_REQUEST ,e.getMessage()));
        }
    }
}
