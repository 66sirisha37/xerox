package com.example.xerox.userservice.controller;

import java.util.List;
import java.util.Map;

import org.aspectj.apache.bcel.generic.RET;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.xerox.userservice.dto.CreateUserRequest;
import com.example.xerox.userservice.entity.User;
import com.example.xerox.userservice.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService service;
    public UserController(UserService service) {
        this.service = service;
    }
    @GetMapping
    public List<User> getUsers() {
        return service.getAllUsers();
    }

    @PostMapping("/createuser")
    public ResponseEntity<Map<String,Object>> createUser(@RequestBody CreateUserRequest request) {
        service.createUser(request);
        // return ResponseEntity.ok("User created successfully");
        Map<String, Object> response = Map.of(
            "status", 201,
            "message", "User created successfully",
            "username", request.getUsername()
        );
        return ResponseEntity.status(201).body(response);
    }
}
