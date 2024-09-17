package com.sync.api.dto.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatusCode;

@AllArgsConstructor
@Data
public class ResponseModelDTO  {
    public int status;
    public Object model;
    public String error;

    public ResponseModelDTO(Object message) {
        this.status = 200;
        this.model = message;
        this.error = "";
    }

    public ResponseModelDTO(String message) {
        this.status = 200;
        this.model = message;
        this.error = "";
    }

}
