package com.example.xerox.userservice.dto;

import com.example.xerox.userservice.entity.Role;
import lombok.Data;

@Data
public class SignupRequest {
    private String username;
    private String password;
    private String confirmPassword;
    private String email;
    private Role role;
}
