package com.anycart.anycart.dtomapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.anycart.anycart.dto.AddToCartDTO;
import com.anycart.anycart.dto.CartItemDTO;
import com.anycart.anycart.entities.Cart;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartMapper {
    CartMapper INSTANCE = Mappers.getMapper(CartMapper.class);

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "product.price", target = "price")
    @Mapping(source = "product.imageUrl", target = "imageUrl")
    CartItemDTO toCartItemDTO(Cart cart);

    List<CartItemDTO> toCartItemDTOs(List<Cart> carts);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "product", source = "productId")
    @Mapping(target = "addedAt", expression = "java(java.time.LocalDateTime.now())")
    Cart toCart(AddToCartDTO dto);
}
