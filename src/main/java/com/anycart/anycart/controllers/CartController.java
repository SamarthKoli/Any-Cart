package com.anycart.anycart.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import com.anycart.anycart.dto.AddToCartDTO;
import com.anycart.anycart.dto.CartDTO;
import com.anycart.anycart.dto.CartItemDTO;
import com.anycart.anycart.dto.UpdateCartItemDTO;
import com.anycart.anycart.services.CartService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/cartItems")
public class CartController {



    @Autowired CartService cartService;


    @GetMapping("/viewAll/{userId}")
    public ResponseEntity<List<CartItemDTO>> getAllCartItems(@PathVariable Long userId){
        List<CartItemDTO> cartItems=cartService.getAllCartItems(userId);
        return ResponseEntity.ok(cartItems);
        
    }

    @PostMapping("/add")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CartItemDTO> addCartItems(@Valid @RequestBody AddToCartDTO dto,Authentication authentication){
       CartItemDTO cartItemDTO=cartService.addToCart(authentication.getName(),dto);
       return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDTO);
       
    }

    @GetMapping("/view")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CartDTO> viewCart(Authentication authentication) {
        CartDTO cartDTO = cartService.viewCart(authentication.getName());
        return ResponseEntity.ok(cartDTO);
    }

    @PutMapping("/update/{productId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CartItemDTO> updateCartItem(@PathVariable long productId, @Valid @RequestBody UpdateCartItemDTO dto, Authentication authentication) {
        CartItemDTO cartItem = cartService.updateCartItem(authentication.getName(), productId, dto);
        return ResponseEntity.ok(cartItem);
    }

    @DeleteMapping("/remove/{productId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> removeFromCart(@PathVariable long productId, Authentication authentication) {
        cartService.removeFromCart(authentication.getName(), productId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler
    public ResponseEntity<String> handleRuntimeException(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }





}
