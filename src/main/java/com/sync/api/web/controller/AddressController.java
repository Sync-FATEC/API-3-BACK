package com.sync.api.web.controller;

import com.sync.api.application.service.AddressService;
import com.sync.api.domain.model.Address;
import com.sync.api.web.dto.web.ResponseModelDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    public ResponseEntity<ResponseModelDTO> getAddress() {
        try {
            List<Address> addresses = addressService.getAddress();
            return ResponseEntity.ok(new ResponseModelDTO(addresses));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseModelDTO(HttpStatus.BAD_REQUEST ,e.getMessage()));
        }
    }
}
