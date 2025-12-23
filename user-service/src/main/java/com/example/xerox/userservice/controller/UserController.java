package com.example.xerox.userservice.controller;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import com.example.xerox.userservice.entity.User;
import com.example.xerox.userservice.service.UserService;

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
    @PostMapping
    public User createUser(@RequestBody User user) {
        return service.saveUser(user);
    }
    
}
