package com.anycart.anycart.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryListDTO {
    private Long id;
    private String name;
    private String parentCategoryName; // Optional
}
