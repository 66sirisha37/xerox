package com.example.xerox.userservice.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.xerox.userservice.dto.CreateUserRequest;
import com.example.xerox.userservice.dto.LoginRequest;
import com.example.xerox.userservice.dto.LoginResponse;
import com.example.xerox.userservice.dto.SignupResponse;
import com.example.xerox.userservice.entity.User;
import com.example.xerox.userservice.exceptions.BlankFieldException;
import com.example.xerox.userservice.exceptions.InvalidCredentialsException;
import com.example.xerox.userservice.exceptions.InvalidRoleException;
import com.example.xerox.userservice.exceptions.PasswordMismatchException;
import com.example.xerox.userservice.exceptions.UsernameAlreadyExistsException;
import com.example.xerox.userservice.repository.UserRepository;
import com.example.xerox.userservice.util.JwtUtil;


@Service
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public List<User> getAllUsers() {
        return repository.findAll();
    }

    public User saveUser(User user) {
        if (user != null) {
            return repository.save(user);
        }
        return null;
    }

    public SignupResponse createUser(CreateUserRequest userRequest){
        if ((userRequest.getUsername().isBlank() || userRequest.getPassword().isBlank() ||
            userRequest.getConfirmPassword().isBlank() || userRequest.getEmail().isBlank())){
            throw new BlankFieldException("All fields are required");
        }
        
        if (!userRequest.getPassword().equals(userRequest.getConfirmPassword())){
            throw new PasswordMismatchException("Passwords do not match");
        }

        if (repository.findByUsername(userRequest.getUsername()).isPresent()){
            throw new UsernameAlreadyExistsException("Username already exists");
        }

        if (userRequest.getRole() == null){
            throw new InvalidRoleException("Role must be specified");
        }
        
        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setEmail(userRequest.getEmail());
        user.setRole(userRequest.getRole());

        User savedUser = repository.save(user);
        
        // Generate JWT token for new user
        String token = jwtUtil.generateToken(savedUser.getUsername(), savedUser.getRole().toString());
        
        return new SignupResponse(
            savedUser.getUsername(),
            "User created successfully",
            savedUser.getRole(),
            token
        );
    }


  public LoginResponse loginUser(LoginRequest request) {

    User user = repository.findByUsername(request.getUsername())
        .orElseThrow(() ->
            new InvalidCredentialsException("Invalid username"));

    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
        throw new InvalidCredentialsException("Invalid username or password");
    }

    String token = jwtUtil.generateToken(user.getUsername(), user.getRole().toString());

    LoginResponse response = new LoginResponse(
        "User logged in successfully",
        user.getUsername(),
        user.getRole()
    );
    response.setToken(token);
    
    return response;
}
}
