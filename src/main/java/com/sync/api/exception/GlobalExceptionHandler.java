package com.sync.api.exception;

import com.sync.api.dto.web.ResponseModelDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SystemContextException.class)
    public ResponseEntity<?> handleSystemContextException(SystemContextException ex, WebRequest request) {
        var response = new ResponseModelDTO(
                HttpStatus.BAD_REQUEST.value(),
                null,
                ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
