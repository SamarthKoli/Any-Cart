package com.anycart.anycart.dtomapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anycart.anycart.dto.OrderDetailsDTO;
import com.anycart.anycart.dto.OrderSummaryDTO;
import com.anycart.anycart.dto.PlaceOrderDTO;
import com.anycart.anycart.entities.Order;
import com.anycart.anycart.entities.OrderStatus;

@Component
public class OrderMapper {

    @Autowired
    private OrderItemMapper orderItemMapper;

    public OrderSummaryDTO toOrderSummaryDTO(Order order) {
        return new OrderSummaryDTO(
            order.getId(),
            order.getTotalAmount(),
            order.getStatus().name(),
            order.getCreatedAt()
        );
    }

    public List<OrderSummaryDTO> toOrderSummaryDTOs(List<Order> orders) {
        return orders.stream().map(this::toOrderSummaryDTO).collect(Collectors.toList());
    }

    public OrderDetailsDTO toOrderDetailsDTO(Order order) {
        return new OrderDetailsDTO(
            order.getId(),
            order.getTotalAmount(),
            order.getStatus().name(),
            order.getShippingAddress(),
            order.getCreatedAt(),
            orderItemMapper.toOrderItemDTOs(order.getOrderItems())
        );
    }

    public Order toOrder(PlaceOrderDTO dto) {
        Order order = new Order();
        order.setShippingAddress(dto.getShippingAddress());
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());
        return order;
    }
}

