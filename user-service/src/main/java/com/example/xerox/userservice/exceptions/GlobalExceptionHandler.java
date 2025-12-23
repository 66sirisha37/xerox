package com.example.xerox.userservice.exceptions;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<Map<String,Object>> handlePasswordMismatch(PasswordMismatchException ex) {
      return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

     
    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<Map<String,Object>> handleUserNotFound(UsernameAlreadyExistsException ex) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }


    @ExceptionHandler(InvalidRoleException.class)
    public ResponseEntity<Map<String,Object>> handleEmailAlreadyExists(InvalidRoleException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(BlankFieldException.class)
    public ResponseEntity<Map<String,Object>> handleBlankField(BlankFieldException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    private ResponseEntity<Map<String,Object>> handleInvalidCredentials(InvalidCredentialsException ex) {
        return buildResponse(HttpStatus.NO_CONTENT, ex.getMessage());
    }

    private ResponseEntity<Map<String,Object>> buildResponse(HttpStatus status, String message) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        body.put("timestamp", LocalDateTime.now());
        return ResponseEntity.status(status).body(body);
    }
}
