package com.example.xerox.userservice.service;

import java.util.List;
import org.springframework.stereotype.Service;

import com.example.xerox.userservice.dto.CreateUserRequest;
import com.example.xerox.userservice.entity.User;
import com.example.xerox.userservice.exceptions.InvalidRoleException;
import com.example.xerox.userservice.exceptions.PasswordMismatchException;
import com.example.xerox.userservice.exceptions.UsernameAlreadyExistsException;
import com.example.xerox.userservice.repository.UserRepository;
import com.example.xerox.userservice.entity.Role;


@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public List<User> getAllUsers() {
        return repository.findAll();
    }

    public User saveUser(User user) {
        return repository.save(user);
    }

    public void createUser(CreateUserRequest userRequest){
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
        user.setPassword(userRequest.getPassword());
        user.setEmail(userRequest.getEmail());
        user.setRole(userRequest.getRole());

        repository.save(user);
    }
}
