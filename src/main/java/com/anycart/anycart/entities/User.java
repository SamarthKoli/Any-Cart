package com.anycart.anycart.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity



@Data
@AllArgsConstructor
@NoArgsConstructor

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
     
    @Column(unique = true,nullable = false)
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private Role role;
    private String address;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;




}
