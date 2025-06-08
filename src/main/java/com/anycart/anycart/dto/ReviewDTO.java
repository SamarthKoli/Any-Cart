package com.anycart.anycart.dto;

import lombok.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {
    @NotNull
    private Long id;

    @Min(1)
    @Max(5)
    private Integer rating;

    private String comment;

    @NotBlank
    private String userName;

    @NotNull
    private LocalDateTime createdAt;
}
