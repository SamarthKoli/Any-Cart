package com.anycart.anycart.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anycart.anycart.dto.CartItemDTO;
import com.anycart.anycart.dtomapper.CartMapper;
import com.anycart.anycart.entities.Cart;
import com.anycart.anycart.repository.CartRepository;

@Service
public class CartService {


    @Autowired CartRepository cartRepository;

    @Autowired CartMapper cartMapper;

    
    public List<CartItemDTO> getAllCartItems(Long userId){
        List<Cart> cartItems=cartRepository.findByUserId(userId);
        return cartMapper.toCartItemDTOs(cartItems);
    }
    




}
