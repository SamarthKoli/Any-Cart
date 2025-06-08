package com.anycart.anycart.dto;

import com.anycart.anycart.entities.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDTO {

    private String email;
    private String firstName;
    private String lastName;
    private Role role;
    private String address;
}
