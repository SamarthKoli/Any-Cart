package com.anycart.anycart.dtomapper;

import com.anycart.anycart.dto.AddToCartDTO;
import com.anycart.anycart.dto.CartDTO;
import com.anycart.anycart.dto.CartItemDTO;
import com.anycart.anycart.entities.Cart;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CartMapper {

    public CartItemDTO toCartItemDTO(Cart cart) {
        return new CartItemDTO(
            cart.getProduct().getId(),
            cart.getProduct().getName(),
            cart.getProduct().getPrice(),
            cart.getQuantity(),
            cart.getProduct().getImageUrl()
        );
    }

    public List<CartItemDTO> toCartItemDTOs(List<Cart> carts) {
        return carts.stream().map(this::toCartItemDTO).collect(Collectors.toList());
    }

    public CartDTO toCartDTO(List<Cart> carts) {
        if (carts.isEmpty()) {
            return new CartDTO(null, null, List.of());
        }
        CartDTO dto = new CartDTO();
        dto.setId(carts.get(0).getId()); // Use first cart's ID
        dto.setUserId(carts.get(0).getUser().getId());
        dto.setItems(toCartItemDTOs(carts));
        return dto;
    }

    public Cart toCart(AddToCartDTO dto) {
        Cart cart = new Cart();
        cart.setQuantity(dto.getQuantity());
        cart.setAddedAt(LocalDateTime.now());
        return cart;
    }
}