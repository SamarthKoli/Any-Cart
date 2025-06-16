package com.anycart.anycart.services;

import com.anycart.anycart.services.PriceAlertService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.anycart.anycart.entities.Product;
import com.anycart.anycart.repository.ProductRepository;

@Component
public class PriceAlertScheduler {

    private static final Logger logger = LoggerFactory.getLogger(PriceAlertScheduler.class);

    @Autowired
    private PriceAlertService priceAlertService;

    @Autowired
    private ProductRepository productRepository;

    @Scheduled(fixedRate = 6000)
    public void checkAllPriceAlerts() {
        logger.info("Starting scheduled price alert check");
        for (Product product : productRepository.findAll()) {
            priceAlertService.checkPriceAlertsForProduct(product.getId());
        }
        logger.info("Completed scheduled price alert check");
    }
}
