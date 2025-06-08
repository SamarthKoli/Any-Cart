package com.anycart.anycart.dtomapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.anycart.anycart.dto.CreateReviewDTO;
import com.anycart.anycart.dto.ReviewDTO;
import com.anycart.anycart.entities.Review;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    ReviewMapper INSTANCE = Mappers.getMapper(ReviewMapper.class);

    @Mapping(target = "userName", expression = "java(review.getUser().getFirstName() + \" \" + review.getUser().getLastName())")
    ReviewDTO toReviewDTO(Review review);

    List<ReviewDTO> toReviewDTOs(List<Review> reviews);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "product", source = "productId")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    Review toReview(CreateReviewDTO dto);
}