package com.anycart.anycart.dto;

import lombok.*;
import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateReviewDTO {
    @NotNull
    private Long productId;

    @Min(1)
    @Max(5)
    private Integer rating;

    private String comment;
}

