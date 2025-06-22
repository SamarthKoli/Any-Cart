package com.anycart.anycart.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anycart.anycart.dto.CreateReviewDTO;
import com.anycart.anycart.dto.ReviewDTO;
import com.anycart.anycart.dtomapper.ReviewMapper;
import com.anycart.anycart.entities.OrderStatus;
import com.anycart.anycart.entities.Product;
import com.anycart.anycart.entities.Review;
import com.anycart.anycart.entities.User;
import com.anycart.anycart.repository.OrderRepository;
import com.anycart.anycart.repository.ProductRepository;
import com.anycart.anycart.repository.ReviewRepository;
import com.anycart.anycart.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class ReviewService {

      @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ReviewMapper reviewMapper;


    @Transactional
    public ReviewDTO createReview(String email, CreateReviewDTO dto) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found: " + dto.getProductId()));

        // Check if user has purchased the product
        boolean hasPurchased = orderRepository.findAll().stream()
                .filter(order -> order.getUser().getId()==(user.getId()) && order.getStatus() == OrderStatus.COMPLETED)
                .flatMap(order -> order.getOrderItems().stream())
                .anyMatch(item -> item.getProduct().getId().equals(dto.getProductId()));
        if (!hasPurchased) {
            throw new RuntimeException("User has not purchased this product");
        }

        // Check for existing review
        boolean hasReviewed = reviewRepository.findAll().stream()
                .anyMatch(review -> review.getUser().getId()==(user.getId()) && 
                                   review.getProduct().getId().equals(dto.getProductId()));
        if (hasReviewed) {
            throw new RuntimeException("User has already reviewed this product");
        }

        Review review = reviewMapper.toReview(dto);
        review.setUser(user);
        review.setProduct(product);
        review.setCreatedAt(LocalDateTime.now());
        review = reviewRepository.save(review);

        return reviewMapper.toReviewDTO(review);
    }

    public List<ReviewDTO> getReviewsByProduct(Long productId) {
        productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found: " + productId));
        List<Review> reviews = reviewRepository.findAll().stream()
                .filter(review -> review.getProduct().getId().equals(productId))
                .collect(Collectors.toList());
        return reviews.stream()
                .map(reviewMapper::toReviewDTO)
                .collect(Collectors.toList());
    }

     @Transactional
    public void deleteReview(String email, Long reviewId, boolean isAdmin) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found: " + reviewId));

        if (!isAdmin && review.getUser().getId()!=(user.getId())) {
            throw new RuntimeException("Unauthorized to delete this review");
        }

        reviewRepository.delete(review);
    }

}
