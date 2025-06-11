package com.anycart.anycart.dto;

import com.anycart.anycart.entities.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDTO {
    @NotNull
    private Long id;
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    private String firstName;
    private String lastName;
    private Role role;
    private String address;
}
