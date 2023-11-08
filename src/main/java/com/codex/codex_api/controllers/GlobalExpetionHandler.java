package com.codex.codex_api.controllers;

import com.codex.codex_api.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExpetionHandler {

    @ExceptionHandler(NotFound.class)
    public ResponseEntity<String> notFound(NotFound ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(NotCreated.class)
    public ResponseEntity<String> notCreated(NotCreated ex) {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(ex.getMessage());
    }

    @ExceptionHandler(ItemAlreadyExists.class)
    public ResponseEntity<String> ItemAlreadyExists(ItemAlreadyExists ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(NoEnoughCB.class)
    public ResponseEntity<String> DontHaveCB(NoEnoughCB ex) {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(ex.getMessage());
    }

    @ExceptionHandler(ErrorApi.class)
    public ResponseEntity<String> DontHaveCB(ErrorApi ex) {
        return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(ex.getMessage());
    }

}