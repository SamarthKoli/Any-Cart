package com.anycart.anycart.dto;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryCreationDTO {
    private String name;
    private String description; // Optional
    private Long parentCategoryId; // Optional
}
