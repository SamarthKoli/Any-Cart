package com.anycart.anycart.dto;
import lombok.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailsDTO {
    @NotNull
    private Long id;

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal totalAmount;

    @NotBlank
    private String status;

    @NotBlank
    private String shippingAddress;

    @NotNull
    private LocalDateTime createdAt;

    private List<OrderItemDTO> orderItems;
}
