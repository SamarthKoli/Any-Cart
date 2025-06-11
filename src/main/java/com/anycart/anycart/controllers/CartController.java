package com.anycart.anycart.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anycart.anycart.dto.CartItemDTO;
import com.anycart.anycart.services.CartService;

@RestController
@RequestMapping("api/v1/cartItems")
public class CartController {



    @Autowired CartService cartService;


    @GetMapping("/viewAll/{userId}")
    public ResponseEntity<List<CartItemDTO>> getAllCartItems(@PathVariable Long userId){
        List<CartItemDTO> cartItems=cartService.getAllCartItems(userId);
        return ResponseEntity.ok(cartItems);
        
    }
}
