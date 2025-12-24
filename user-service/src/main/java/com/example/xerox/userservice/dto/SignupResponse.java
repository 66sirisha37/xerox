package com.example.xerox.userservice.dto;

import com.example.xerox.userservice.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignupResponse {
    private String username;
    private String message;
    private Role role;
    private String token;
}
