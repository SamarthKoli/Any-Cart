package com.anycart.anycart.dto;

import org.antlr.v4.runtime.misc.NotNull;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCartItemDTO {
    @NotNull
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;
}