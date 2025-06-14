package com.anycart.anycart.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anycart.anycart.dto.OrderDetailsDTO;
import com.anycart.anycart.dto.OrderSummaryDTO;
import com.anycart.anycart.dto.PlaceOrderDTO;
import com.anycart.anycart.dtomapper.OrderMapper;
import com.anycart.anycart.entities.Cart;
import com.anycart.anycart.entities.Order;
import com.anycart.anycart.entities.OrderItem;
import com.anycart.anycart.entities.OrderStatus;
import com.anycart.anycart.entities.Product;
import com.anycart.anycart.entities.User;
import com.anycart.anycart.repository.CartRepository;
import com.anycart.anycart.repository.OrderItemRepository;
import com.anycart.anycart.repository.OrderRepository;
import com.anycart.anycart.repository.ProductRepository;
import com.anycart.anycart.repository.UserRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
public class OrderService {



    @Autowired UserRepository userRepository;

    @Autowired OrderRepository orderRepository;

    @Autowired OrderMapper orderMapper;

    @Autowired OrderItemRepository orderItemRepository;


    @Autowired CartRepository cartRepository;

    @Autowired ProductRepository productRepository;


    @Autowired EmailService emailService;



    @Transactional
    public OrderDetailsDTO placeOrder(String email, @Valid PlaceOrderDTO dto){

        User user=userRepository.findByEmail(email).
        orElseThrow(()-> new RuntimeException("User not found"));

        List<Cart> cartItems =cartRepository.findByUserId(user.getId());

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");

        }

        List<Cart> selectedItems=cartItems.stream()
        .filter(cart-> dto.getCartItemIds()
        .contains(cart.getId())).collect(Collectors.toList());


        if (selectedItems.isEmpty()) {
            throw new RuntimeException("No valid cart items are selected");

        }

        for(Cart cart:selectedItems){
            Product product=cart.getProduct();
            if (product.getStock()< cart.getQuantity()) {
                throw new RuntimeException(product.getName()+"is out of stock");
            }

           
            
        }

         Order order=orderMapper.toOrder(dto);
         order.setUser(user);
         order.setTotalAmount(BigDecimal.ZERO);
         order=orderRepository.save(order);

         // Create order items
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (Cart cart : selectedItems) {
            Product product = cart.getProduct();
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(cart.getQuantity());
            orderItem.setUnitPrice(product.getPrice());
            orderItemRepository.save(orderItem);
            order.getOrderItems().add(orderItem);

            // Update total amount
            totalAmount = totalAmount.add(product.getPrice().multiply(new BigDecimal(cart.getQuantity())));

            // Update stock
            product.setStock(product.getStock() - cart.getQuantity());
            product.setUpdatedAt(LocalDateTime.now());
            productRepository.save(product);
        }

        // Update order total
        order.setTotalAmount(totalAmount);
        order = orderRepository.save(order);

        // Clear selected cart items
        cartRepository.deleteAll(selectedItems);

        emailService.sendOrderConfirmationEmail(order);

        return orderMapper.toOrderDetailsDTO(order);


            
    }


    public OrderDetailsDTO getOrderDetails(String email, Long orderId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        if (order.getUser().getId()!=user.getId()) {
            throw new RuntimeException("Unauthorized access to order");
        }
        return orderMapper.toOrderDetailsDTO(order);
    }

     public List<OrderSummaryDTO> getOrderHistory(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Order> orders = orderRepository.findAll().stream()
                .filter(order -> order.getUser().getId()==user.getId())
                .collect(Collectors.toList());
        return orderMapper.toOrderSummaryDTOs(orders);
    }

        public OrderDetailsDTO updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        try {
            order.setStatus(OrderStatus.valueOf(status));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid status: " + status);
        }
        order.setUpdatedAt(LocalDateTime.now());
        order = orderRepository.save(order);
        return orderMapper.toOrderDetailsDTO(order);
    }


     @Transactional
    public OrderDetailsDTO cancelOrder(String email, Long orderId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        if (order.getUser().getId()!=(user.getId())) {
            throw new RuntimeException("Unauthorized access to order");
        }
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new RuntimeException("Only pending orders can be cancelled");
        }
        if (order.getCreatedAt().isBefore(LocalDateTime.now().minusHours(24))) {
            throw new RuntimeException("Orders older than 24 hours cannot be cancelled");
        }

        // Restock products
        for (OrderItem item : order.getOrderItems()) {
            Product product = item.getProduct();
            product.setStock(product.getStock() + item.getQuantity());
            product.setUpdatedAt(LocalDateTime.now());
            productRepository.save(product);
        }

        order.setStatus(OrderStatus.CANCELLED);
        order.setUpdatedAt(LocalDateTime.now());
        order = orderRepository.save(order);
        return orderMapper.toOrderDetailsDTO(order);
    }

     public List<OrderSummaryDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orderMapper.toOrderSummaryDTOs(orders);
    }
    



}
