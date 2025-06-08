package com.anycart.anycart.dtomapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.anycart.anycart.dto.CreateReviewDTO;
import com.anycart.anycart.dto.ReviewDTO;
import com.anycart.anycart.entities.Review;

@Component
public class ReviewMapper {

    public ReviewDTO toReviewDTO(Review review) {
        String userName = review.getUser().getFirstName() + " " + review.getUser().getLastName();
        return new ReviewDTO(
            review.getId(),
            review.getRating(),
            review.getComment(),
            userName,
            review.getCreatedAt()
        );
    }

    public List<ReviewDTO> toReviewDTOs(List<Review> reviews) {
        return reviews.stream().map(this::toReviewDTO).collect(Collectors.toList());
    }

    public Review toReview(CreateReviewDTO dto) {
        Review review = new Review();
        review.setRating(dto.getRating());
        review.setComment(dto.getComment());
        review.setCreatedAt(LocalDateTime.now());
        return review;
    }
}
