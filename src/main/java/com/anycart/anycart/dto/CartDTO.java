package com.anycart.anycart.dto;

import java.util.List;

import org.antlr.v4.runtime.misc.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {
    @NotNull
    private Long id;
    @NotNull
    private Long userId;
    private List<CartItemDTO> items;
}