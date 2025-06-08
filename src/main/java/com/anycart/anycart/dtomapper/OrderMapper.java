package com.anycart.anycart.dtomapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.anycart.anycart.dto.OrderDetailsDTO;
import com.anycart.anycart.dto.OrderSummaryDTO;
import com.anycart.anycart.dto.PlaceOrderDTO;
import com.anycart.anycart.entities.Order;

import java.util.List;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class})
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    OrderSummaryDTO toOrderSummaryDTO(Order order);

    List<OrderSummaryDTO> toOrderSummaryDTOs(List<Order> orders);

    @Mapping(source = "orderItems", target = "orderItems")
    OrderDetailsDTO toOrderDetailsDTO(Order order);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "totalAmount", ignore = true)
    @Mapping(target = "status", constant = "PENDING")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", ignore = true)
    Order toOrder(PlaceOrderDTO dto);
}
