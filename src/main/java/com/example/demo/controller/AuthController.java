package com.example.demo.controller;

import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.model.UserAccount;
import com.example.demo.service.UserAccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserAccountService userService;

    public AuthController(UserAccountService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest request) {
        UserAccount user = new UserAccount();
        user.setEmail(request.getUsername()); // Mapping username field to email as per common patterns or customize
        user.setUsername(request.getUsername());
        user.setPasswordHash(request.getPassword()); // Should be encoded
        user.setRole(Collections.singleton("EMPLOYEE"));
        
        userService.registerUser(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        UserAccount user = userService.findByEmail(request.getUsername());
        AuthResponse response = new AuthResponse();
        response.setToken("dummy-jwt-token-for-review-1");
        response.setEmail(user.getEmail());
        
        return ResponseEntity.ok(response);
    }
}