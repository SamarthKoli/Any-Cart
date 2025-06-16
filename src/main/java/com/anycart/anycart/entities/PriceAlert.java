package com.anycart.anycart.entities;



import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "price_alerts")
public class PriceAlert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "target_price", nullable = false)
    private BigDecimal targetPrice;

    @Enumerated(EnumType.STRING)
    private PriceAlertStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public enum PriceAlertStatus {
        ACTIVE, TRIGGERED, DELETED
    }
}