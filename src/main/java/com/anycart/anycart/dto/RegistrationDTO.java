package com.anycart.anycart.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationDTO {

    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String address;

    
}
