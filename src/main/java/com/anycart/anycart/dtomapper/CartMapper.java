package com.anycart.anycart.dtomapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.anycart.anycart.dto.AddToCartDTO;
import com.anycart.anycart.dto.CartItemDTO;
import com.anycart.anycart.entities.Cart;

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

    public Cart toCart(AddToCartDTO dto) {
        Cart cart = new Cart();
        cart.setQuantity(dto.getQuantity());
        cart.setAddedAt(LocalDateTime.now());
        // You must set user and product separately in service layer
        return cart;
    }
}

