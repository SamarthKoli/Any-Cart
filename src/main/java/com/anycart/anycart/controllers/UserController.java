package com.anycart.anycart.controllers;

import com.anycart.anycart.dto.LoginDTO;
import com.anycart.anycart.dto.RegistrationDTO;
import com.anycart.anycart.dto.UserProfileDTO;
import com.anycart.anycart.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserProfileDTO> register(@Valid @RequestBody RegistrationDTO dto) {
        UserProfileDTO userDTO = userService.registerUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginDTO dto) {
        String token = userService.loginUser(dto);
        return ResponseEntity.ok(token);
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserProfileDTO> getProfile(Authentication authentication) {
        String email = authentication.getName(); // Email from JWT subject
        UserProfileDTO userDTO = userService.getUserProfileByEmail(email);
        return ResponseEntity.ok(userDTO);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleRuntimeException(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}