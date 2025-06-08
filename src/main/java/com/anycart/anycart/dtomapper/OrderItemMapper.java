package com.anycart.anycart.dtomapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.anycart.anycart.dto.OrderItemCreationDTO;
import com.anycart.anycart.dto.OrderItemDTO;
import com.anycart.anycart.entities.OrderItem;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    OrderItemMapper INSTANCE = Mappers.getMapper(OrderItemMapper.class);

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    OrderItemDTO toOrderItemDTO(OrderItem orderItem);

    List<OrderItemDTO> toOrderItemDTOs(List<OrderItem> orderItems);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "product", source = "productId")
    @Mapping(target = "unitPrice", ignore = true)
    OrderItem toOrderItem(OrderItemCreationDTO dto);
}
