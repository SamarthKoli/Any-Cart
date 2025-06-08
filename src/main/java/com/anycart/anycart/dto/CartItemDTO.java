package com.anycart.anycart.dto;

import lombok.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {
    @NotNull
    private Long productId;

    @NotBlank
    private String productName;

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal price;

    @NotNull
    @Min(1)
    private Integer quantity;

    @NotBlank
    private String imageUrl;
}
