package com.anycart.anycart.controllers;

import com.anycart.anycart.dto.OrderDetailsDTO;
import com.anycart.anycart.dto.OrderSummaryDTO;
import com.anycart.anycart.dto.PlaceOrderDTO;
import com.anycart.anycart.services.OrderService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @PostMapping("/place")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> placeOrder(@Valid @RequestBody PlaceOrderDTO dto, Authentication authentication) {
        try {
            logger.info("Place order request for user: {}", authentication.getName());
            OrderDetailsDTO orderDTO = orderService.placeOrder(authentication.getName(), dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(orderDTO);
        } catch (RuntimeException e) {
            logger.error("Error placing order for user {}: {}", authentication.getName(), e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to place order: " + e.getMessage());
        }
    }

    @GetMapping("/view/{orderId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getOrderDetails(@PathVariable Long orderId, Authentication authentication) {
        try {
            logger.info("Get order details request for orderId: {}, user: {}", orderId, authentication.getName());
            OrderDetailsDTO orderDTO = orderService.getOrderDetails(authentication.getName(), orderId);
            return ResponseEntity.ok(orderDTO);
        } catch (RuntimeException e) {
            logger.error("Error fetching order details for orderId {} by user {}: {}", 
                         orderId, authentication.getName(), e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to fetch order details: " + e.getMessage());
        }
    }

    @GetMapping("/history")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getOrderHistory(Authentication authentication) {
        try {
            logger.info("Get order history request for user: {}", authentication.getName());
            List<OrderSummaryDTO> orders = orderService.getOrderHistory(authentication.getName());
            return ResponseEntity.ok(orders);
        } catch (RuntimeException e) {
            logger.error("Error fetching order history for user {}: {}", authentication.getName(), e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to fetch order history: " + e.getMessage());
        }
    }

    @PutMapping("/update/{orderId}/status")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long orderId, @RequestParam String status) {
        try {
            logger.info("Update order status request for orderId: {}, status: {}", orderId, status);
            OrderDetailsDTO orderDTO = orderService.updateOrderStatus(orderId, status);
            return ResponseEntity.ok(orderDTO);
        } catch (RuntimeException e) {
            logger.error("Error updating order status for orderId {}: {}", orderId, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update order status: " + e.getMessage());
        }
    }

    @PutMapping("/cancel/{orderId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> cancelOrder(@PathVariable Long orderId, Authentication authentication) {
        try {
            logger.info("Cancel order request for orderId: {}, user: {}", orderId, authentication.getName());
            OrderDetailsDTO orderDTO = orderService.cancelOrder(authentication.getName(), orderId);
            return ResponseEntity.ok(orderDTO);
        } catch (RuntimeException e) {
            logger.error("Error cancelling order {} for user {}: {}", 
                         orderId, authentication.getName(), e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to cancel order: " + e.getMessage());
        }
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<?> getAllOrders() {
        try {
            logger.info("Get all orders request");
            List<OrderSummaryDTO> orders = orderService.getAllOrders();
            return ResponseEntity.ok(orders);
        } catch (RuntimeException e) {
            logger.error("Error fetching all orders: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to fetch all orders: " + e.getMessage());
        }
    }
}