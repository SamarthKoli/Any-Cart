package com.anycart.anycart.services;

import com.anycart.anycart.dto.AddToCartDTO;
import com.anycart.anycart.dto.CartDTO;
import com.anycart.anycart.dto.CartItemDTO;
import com.anycart.anycart.dto.UpdateCartItemDTO;
import com.anycart.anycart.dtomapper.CartMapper;
import com.anycart.anycart.entities.Cart;
import com.anycart.anycart.entities.Product;
import com.anycart.anycart.entities.User;
import com.anycart.anycart.repository.CartRepository;
import com.anycart.anycart.repository.ProductRepository;
import com.anycart.anycart.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartMapper cartMapper;

    public List<CartItemDTO> getAllCartItems(Long userId) {
        List<Cart> cartItems = cartRepository.findByUserId(userId);
        return cartMapper.toCartItemDTOs(cartItems);
    }

    public CartDTO viewCart(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Cart> cartItems = cartRepository.findByUserId(user.getId());
        return cartMapper.toCartDTO(cartItems);
    }

    public CartItemDTO addToCart(String email, @Valid AddToCartDTO dto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getStock() < dto.getQuantity()) {
            throw new RuntimeException("Insufficient stock for product: " + product.getName());
        }

        List<Cart> existingItems = cartRepository.findByUserId(user.getId());
        for (Cart cart : existingItems) {
            if (cart.getProduct().getId().equals(dto.getProductId())) {
                int newQuantity = cart.getQuantity() + dto.getQuantity();
                if (product.getStock() < newQuantity) {
                    throw new RuntimeException("Insufficient stock for product: " + product.getName());
                }
                cart.setQuantity(newQuantity);
                cart.setAddedAt(LocalDateTime.now());
                cartRepository.save(cart);
                return cartMapper.toCartItemDTO(cart);
            }
        }

        Cart cart = cartMapper.toCart(dto);
        cart.setUser(user);
        cart.setProduct(product);
        cart = cartRepository.save(cart);
        return cartMapper.toCartItemDTO(cart);
    }

    public CartItemDTO updateCartItem(String email, Long productId, @Valid UpdateCartItemDTO dto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getStock() < dto.getQuantity()) {
            throw new RuntimeException("Insufficient stock for product: " + product.getName());
        }

        List<Cart> cartItems = cartRepository.findByUserId(user.getId());
        Cart cart = cartItems.stream()
                .filter(c -> c.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Item not found in cart"));

        cart.setQuantity(dto.getQuantity());
        cart.setAddedAt(LocalDateTime.now());
        cart = cartRepository.save(cart);
        return cartMapper.toCartItemDTO(cart);
    }

    public void removeFromCart(String email, Long productId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Cart> cartItems = cartRepository.findByUserId(user.getId());
        Cart cart = cartItems.stream()
                .filter(c -> c.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Item not found in cart"));

        cartRepository.delete(cart);
    }
}