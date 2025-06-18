package com.anycart.anycart.dto;


import lombok.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreationDTO {
    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal price;

    @NotNull
    @Min(0)
    private Integer stock;

    private MultipartFile image;

    @NotNull
    private Long categoryId;
}
