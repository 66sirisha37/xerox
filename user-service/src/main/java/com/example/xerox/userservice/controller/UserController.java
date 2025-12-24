package com.example.xerox.userservice.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.xerox.userservice.dto.CreateUserRequest;
import com.example.xerox.userservice.dto.SignupResponse;
import com.example.xerox.userservice.entity.User;
import com.example.xerox.userservice.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/auth")
public class UserController {
    private final UserService service;
    public UserController(UserService service) {
        this.service = service;
    }
    @GetMapping
    public List<User> getUsers() {
        return service.getAllUsers();
    }

    @PostMapping("/signup")
    public ResponseEntity<Map<String,Object>> createUser(@RequestBody CreateUserRequest request) {
        SignupResponse signupResponse = service.createUser(request);
        
        Map<String, Object> response = Map.of(
            "status", 200,
            "message", signupResponse.getMessage(),
            "username", signupResponse.getUsername(),
            "role", signupResponse.getRole(),
            "token", signupResponse.getToken()
        );
        return ResponseEntity.status(200).body(response);
    }
}
