package com.example.xerox.userservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.xerox.userservice.dto.LoginRequest;
import com.example.xerox.userservice.dto.LoginResponse;
import com.example.xerox.userservice.service.UserService;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginuser(
            @RequestBody LoginRequest request) {

        LoginResponse response = userService.loginUser(request);

        Map<String, Object> responseBody = Map.of(
            "username", response.getUsername(),
            "message", response.getMessage(),
            "role", response.getRole()
        );

        return ResponseEntity.ok(responseBody); 
    }
}
