package com.anycart.anycart.controllers;

import com.anycart.anycart.dto.CreateReviewDTO;
import com.anycart.anycart.dto.ReviewDTO;
import com.anycart.anycart.services.ReviewService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    private static final Logger logger = LoggerFactory.getLogger(ReviewController.class);

    @Autowired
    private ReviewService reviewService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> createReview(@Valid @RequestBody CreateReviewDTO dto, Authentication authentication) {
        try {
            logger.info("Create review request for user: {}", authentication.getName());
            ReviewDTO reviewDTO = reviewService.createReview(authentication.getName(), dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(reviewDTO);
        } catch (RuntimeException e) {
            logger.error("Error creating review for user {}: {}", authentication.getName(), e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create review: " + e.getMessage());
        }
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<?> getReviewsByProduct(@PathVariable Long productId) {
        try {
            logger.info("Get reviews request for productId: {}", productId);
            List<ReviewDTO> reviews = reviewService.getReviewsByProduct(productId);
            return ResponseEntity.ok(reviews);
        } catch (RuntimeException e) {
            logger.error("Error fetching reviews for productId {}: {}", productId, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to fetch reviews: " + e.getMessage());
        }
    }

    @DeleteMapping("/{reviewId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteReview(@PathVariable Long reviewId, Authentication authentication) {
        try {
            logger.info("Delete review request for reviewId: {}, user: {}", reviewId, authentication.getName());
            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_Admin"));
            reviewService.deleteReview(authentication.getName(), reviewId, isAdmin);
            return ResponseEntity.ok("Review deleted successfully");
        } catch (RuntimeException e) {
            logger.error("Error deleting review {} for user {}: {}", reviewId, authentication.getName(), e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to delete review: " + e.getMessage());
        }
    }
}