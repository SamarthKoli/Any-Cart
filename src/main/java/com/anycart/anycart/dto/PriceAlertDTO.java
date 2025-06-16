package com.anycart.anycart.dto;



import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PriceAlertDTO {
    private Long id;
    private Long productId;
    private String productName;
    private BigDecimal targetPrice;
    private String status;
    private LocalDateTime createdAt;
}