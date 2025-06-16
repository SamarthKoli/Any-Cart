package com.anycart.anycart.services;


import com.anycart.anycart.dto.PriceAlertDTO;
import com.anycart.anycart.entities.PriceAlert;
import com.anycart.anycart.entities.PriceAlert.PriceAlertStatus;
import com.anycart.anycart.entities.Product;
import com.anycart.anycart.entities.User;
import com.anycart.anycart.repository.PriceAlertRepository;
import com.anycart.anycart.repository.ProductRepository;
import com.anycart.anycart.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PriceAlertService {

    private static final Logger logger = LoggerFactory.getLogger(PriceAlertService.class);

    @Autowired
    private PriceAlertRepository priceAlertRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private EmailService emailService;

    @Transactional
    public PriceAlertDTO createPriceAlert(String email, PriceAlertDTO dto) {
        logger.info("Creating price alert for user: {}, productId: {}", email, dto.getProductId());

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found: " + dto.getProductId()));

        if (dto.getTargetPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Target price must be positive");
        }

        if (priceAlertRepository.existsByUserIdAndProductIdAndStatus(user.getId(), product.getId(), PriceAlertStatus.ACTIVE)) {
            throw new RuntimeException("Price alert already exists for this product");
        }

        PriceAlert alert = new PriceAlert();
        alert.setUser(user);
        alert.setProduct(product);
        alert.setTargetPrice(dto.getTargetPrice());
        alert.setStatus(PriceAlertStatus.ACTIVE);
        alert.setCreatedAt(LocalDateTime.now());

        alert = priceAlertRepository.save(alert);
        logger.info("Price alert created: id={}", alert.getId());

        if (product.getPrice().compareTo(dto.getTargetPrice()) <= 0) {
            triggerPriceAlert(alert);
        }

        return toPriceAlertDTO(alert);
    }

    public List<PriceAlertDTO> getUserPriceAlerts(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));
        return priceAlertRepository.findByUserIdAndStatus(user.getId(), PriceAlertStatus.ACTIVE)
                .stream()
                .map(this::toPriceAlertDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deletePriceAlert(String email, Long alertId) {
        logger.info("Deleting price alert id: {} for user: {}", alertId, email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));
        PriceAlert alert = priceAlertRepository.findById(alertId)
                .orElseThrow(() -> new RuntimeException("Price alert not found: " + alertId));

        if (alert.getUser().getId()!=(user.getId())) {
            throw new RuntimeException("Unauthorized to delete this price alert");
        }

        alert.setStatus(PriceAlertStatus.DELETED);
        priceAlertRepository.save(alert);
        logger.info("Price alert deleted: id={}", alertId);
    }

    @Transactional
    public void checkPriceAlertsForProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found: " + productId));
        List<PriceAlert> alerts = priceAlertRepository.findByProductIdAndStatus(productId, PriceAlertStatus.ACTIVE);

        for (PriceAlert alert : alerts) {
            if (product.getPrice().compareTo(alert.getTargetPrice()) <= 0) {
                triggerPriceAlert(alert);
            }
        }
    }

    private void triggerPriceAlert(PriceAlert alert) {
        alert.setStatus(PriceAlertStatus.TRIGGERED);
        priceAlertRepository.save(alert);
        emailService.sendPriceDropNotification(alert);
        logger.info("Price alert triggered for user: {}, product: {}, targetPrice: {}, currentPrice: {}",
                alert.getUser().getEmail(), alert.getProduct().getName(),
                alert.getTargetPrice(), alert.getProduct().getPrice());
    }

    private PriceAlertDTO toPriceAlertDTO(PriceAlert alert) {
        PriceAlertDTO dto = new PriceAlertDTO();
        dto.setId(alert.getId());
        dto.setProductId(alert.getProduct().getId());
        dto.setProductName(alert.getProduct().getName());
        dto.setTargetPrice(alert.getTargetPrice());
        dto.setStatus(alert.getStatus().name());
        dto.setCreatedAt(alert.getCreatedAt());
        return dto;
    }
}