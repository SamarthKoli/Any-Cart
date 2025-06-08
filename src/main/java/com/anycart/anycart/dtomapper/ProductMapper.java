package com.anycart.anycart.dtomapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.anycart.anycart.dto.ProductCreationDTO;
import com.anycart.anycart.dto.ProductDetailsDTO;
import com.anycart.anycart.dto.ProductListDTO;
import com.anycart.anycart.entities.Product;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mapping(source = "category.name", target = "categoryName")
    ProductListDTO toProductListDTO(Product product);

    List<ProductListDTO> toProductListDTOs(List<Product> products);

    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(source = "reviews", target = "reviews")
    ProductDetailsDTO toProductDetailsDTO(Product product);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "category", source = "categoryId")
    Product toProduct(ProductCreationDTO dto);
}