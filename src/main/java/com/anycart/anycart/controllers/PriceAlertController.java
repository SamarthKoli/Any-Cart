package com.anycart.anycart.controllers;

import com.anycart.anycart.dto.PriceAlertDTO;
import com.anycart.anycart.services.PriceAlertService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/price-alerts")
public class PriceAlertController {

    private static final Logger logger = LoggerFactory.getLogger(PriceAlertController.class);

    @Autowired
    private PriceAlertService priceAlertService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> createPriceAlert(@Valid @RequestBody PriceAlertDTO dto, Authentication authentication) {
        try {
            logger.info("Create price alert request for user: {}", authentication.getName());
            PriceAlertDTO createdAlert = priceAlertService.createPriceAlert(authentication.getName(), dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAlert);
        } catch (RuntimeException e) {
            logger.error("Error creating price alert for user {}: {}", authentication.getName(), e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create price alert: " + e.getMessage());
        }
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getPriceAlerts(Authentication authentication) {
        try {
            logger.info("Get price alerts request for user: {}", authentication.getName());
            List<PriceAlertDTO> alerts = priceAlertService.getUserPriceAlerts(authentication.getName());
            return ResponseEntity.ok(alerts);
        } catch (RuntimeException e) {
            logger.error("Error fetching price alerts for user {}: {}", authentication.getName(), e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to fetch price alerts: " + e.getMessage());
        }
    }

    @DeleteMapping("/{alertId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deletePriceAlert(@PathVariable Long alertId, Authentication authentication) {
        try {
            logger.info("Delete price alert request for user: {}, alertId: {}", authentication.getName(), alertId);
            priceAlertService.deletePriceAlert(authentication.getName(), alertId);
            return ResponseEntity.ok("Price alert deleted successfully");
        } catch (RuntimeException e) {
            logger.error("Error deleting price alert {} for user {}: {}", 
                         alertId, authentication.getName(), e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to delete price alert: " + e.getMessage());
        }
    }
}
