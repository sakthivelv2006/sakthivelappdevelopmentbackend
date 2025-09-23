package com.examly.springapp.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

@ExceptionHandler(ValidationException.class)
public ResponseEntity<Map<String, String>> handleValidationException(ValidationException ex) {
Map<String, String> response = new HashMap<>();
response.put("message", ex.getMessage());
return ResponseEntity.badRequest().body(response);
}
}
