package com.anycart.anycart.dtomapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.anycart.anycart.dto.OrderItemCreationDTO;
import com.anycart.anycart.dto.OrderItemDTO;
import com.anycart.anycart.entities.OrderItem;

@Component
public class OrderItemMapper {

    public OrderItemDTO toOrderItemDTO(OrderItem orderItem) {
        return new OrderItemDTO(
            orderItem.getProduct().getId(),
            orderItem.getProduct().getName(),
            orderItem.getQuantity(),
            orderItem.getUnitPrice()
        );
    }

    public List<OrderItemDTO> toOrderItemDTOs(List<OrderItem> items) {
        return items.stream().map(this::toOrderItemDTO).collect(Collectors.toList());
    }

    public OrderItem toOrderItem(OrderItemCreationDTO dto) {
        OrderItem item = new OrderItem();
        item.setQuantity(dto.getQuantity());
        // set product and order externally in service
        return item;
    }
}

