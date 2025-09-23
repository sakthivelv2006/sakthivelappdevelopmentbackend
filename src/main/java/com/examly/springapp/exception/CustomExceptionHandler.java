package com.examly.springapp.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class CustomExceptionHandler {

@ExceptionHandler(ResourceNotFoundException.class)
public ResponseEntity<?> handleResourceNotFound(ResourceNotFoundException ex) {
Map<String, String> error = new HashMap<>();
error.put("message", ex.getMessage());
return ResponseEntity.status(404).body(error);
}

@ExceptionHandler(MethodArgumentNotValidException.class)
public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex) {
Map<String, String> error = new HashMap<>();
error.put("message", ex.getBindingResult().getFieldError().getDefaultMessage());
return ResponseEntity.badRequest().body(error);
}
}
