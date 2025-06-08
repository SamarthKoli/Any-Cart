package com.anycart.anycart.dto;
import lombok.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductListDTO {
    @NotNull
    private Long id;

    @NotBlank
    private String name;

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal price;

    @NotBlank
    private String imageUrl;

    @NotBlank
    private String categoryName;
}
