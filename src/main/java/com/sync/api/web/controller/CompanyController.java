package com.sync.api.web.controller;

import com.sync.api.application.service.CompanyService;
import com.sync.api.domain.model.Company;
import com.sync.api.web.dto.company.RegisterCompanyDTO;
import com.sync.api.web.dto.company.UpdateCompanyDTO;
import com.sync.api.web.dto.web.ResponseModelDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @PostMapping("/create")
    public ResponseEntity<ResponseModelDTO> createCompany(@RequestBody RegisterCompanyDTO registerCompanyDTO) {
        try {
            return ResponseEntity.ok(new ResponseModelDTO(companyService.createCompany(registerCompanyDTO)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseModelDTO(HttpStatus.BAD_REQUEST, e.getMessage()));
        }
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<ResponseModelDTO> getCompany(@PathVariable String id) {
        try {
            return ResponseEntity.ok(new ResponseModelDTO(companyService.getCompany(id)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseModelDTO(HttpStatus.BAD_REQUEST, e.getMessage()));
        }
    }

    @GetMapping("/buscar")
    public ResponseEntity<ResponseModelDTO> getCompanies() {
        try {
            return ResponseEntity.ok(new ResponseModelDTO(companyService.getCompanies()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseModelDTO(HttpStatus.BAD_REQUEST, e.getMessage()));
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<ResponseModelDTO> getCompaniesByFilter(@RequestParam(value = "keyword", required = false) String keyword) {
        try {
            if (keyword == null) {
                return ResponseEntity.ok(new ResponseModelDTO(companyService.getCompanies()));
            }
            return ResponseEntity.ok(new ResponseModelDTO(companyService.filterCompanies(keyword)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseModelDTO(HttpStatus.BAD_REQUEST, e.getMessage()));
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseModelDTO> updateCompany(@RequestBody UpdateCompanyDTO updateCompanyDTO) {
        try {
            return ResponseEntity.ok(new ResponseModelDTO(companyService.updateCompany(updateCompanyDTO)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseModelDTO(HttpStatus.BAD_REQUEST, e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseModelDTO> deleteCompany(@PathVariable String id) {
        try {
            companyService.deleteCompany(id);
            return ResponseEntity.ok(new ResponseModelDTO("Empresa deletada com sucesso."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseModelDTO(HttpStatus.BAD_REQUEST, e.getMessage()));
        }
    }
}
