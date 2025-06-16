package com.anycart.anycart.repository;

import com.anycart.anycart.entities.PriceAlert;
import com.anycart.anycart.entities.PriceAlert.PriceAlertStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PriceAlertRepository extends JpaRepository<PriceAlert, Long> {
    List<PriceAlert> findByProductIdAndStatus(Long productId, PriceAlertStatus status);
    List<PriceAlert> findByUserIdAndStatus(Long userId, PriceAlertStatus status);
    boolean existsByUserIdAndProductIdAndStatus(Long userId, Long productId, PriceAlertStatus status);
}
