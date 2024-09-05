package com.sync.api.dto.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Data
public class ReponseModelDTO {
    public int status;
    public Object message;
    public String error;
}
