package com.sync.api.dto.web;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ResponseModelDTO {
    public int status;
    public Object model;
    public String error;

    public ResponseModelDTO(Object message) {
        this.status = 200;
        this.model = message;
        this.error = "";
    }
}
