package com.anycart.anycart.dto;

import lombok.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderSummaryDTO {
    @NotNull
    private Long id;

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal totalAmount;

    @NotBlank
    private String status;

    @NotNull
    private LocalDateTime createdAt;
}
