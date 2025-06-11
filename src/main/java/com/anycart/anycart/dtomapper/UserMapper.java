package com.anycart.anycart.dtomapper;

import com.anycart.anycart.dto.RegistrationDTO;
import com.anycart.anycart.dto.UserProfileDTO;
import com.anycart.anycart.entities.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserProfileDTO toUserDTO(User user) {
        UserProfileDTO dto = new UserProfileDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setRole(user.getRole());
        dto.setAddress(user.getAddress());
        return dto;
    }

    public User toUser(RegistrationDTO dto) {
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setAddress(dto.getAddress());
        // Password and role set in service
        return user;
    }
}