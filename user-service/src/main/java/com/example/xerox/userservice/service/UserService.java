package com.example.xerox.userservice.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.xerox.userservice.dto.CreateUserRequest;
import com.example.xerox.userservice.dto.LoginRequest;
import com.example.xerox.userservice.dto.LoginResponse;
import com.example.xerox.userservice.entity.User;
import com.example.xerox.userservice.exceptions.BlankFieldException;
import com.example.xerox.userservice.exceptions.InvalidCredentialsException;
import com.example.xerox.userservice.exceptions.InvalidRoleException;
import com.example.xerox.userservice.exceptions.PasswordMismatchException;
import com.example.xerox.userservice.exceptions.UsernameAlreadyExistsException;
import com.example.xerox.userservice.repository.UserRepository;


@Service
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers() {
        return repository.findAll();
    }

    public User saveUser(User user) {
        return repository.save(user);
    }

    public void createUser(CreateUserRequest userRequest){
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

        repository.save(user);
    }


  public LoginResponse loginUser(LoginRequest request) {

    User user = repository.findByUsername(request.getUsername())
        .orElseThrow(() ->
            new InvalidCredentialsException("Invalid username or password"));

    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
        throw new InvalidCredentialsException("Invalid username or password");
    }

    return new LoginResponse(
        "User logged in successfully", // ✅ correct order
        user.getUsername(),
        user.getRole()
    );
}


}
