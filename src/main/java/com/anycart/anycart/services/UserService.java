package com.anycart.anycart.services;

import com.anycart.anycart.dto.LoginDTO;
import com.anycart.anycart.dto.RegistrationDTO;
import com.anycart.anycart.dto.UserProfileDTO;
import com.anycart.anycart.dtomapper.UserMapper;
import com.anycart.anycart.entities.Role;
import com.anycart.anycart.entities.User;
import com.anycart.anycart.repository.UserRepository;
import com.anycart.anycart.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    public UserProfileDTO registerUser(@Valid RegistrationDTO dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }
        User user = userMapper.toUser(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(Role.User);
        user.setCreatedAt(LocalDateTime.now());
        user = userRepository.save(user);
        return userMapper.toUserDTO(user);
    }

    public String loginUser(@Valid LoginDTO dto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));
        return jwtUtil.generateToken(authentication.getName());
    }

    public UserProfileDTO getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toUserDTO(user);
    }

    public UserProfileDTO getUserProfileByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toUserDTO(user);
    }
}