package com.anycart.anycart.dtomapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.anycart.anycart.dto.ProductCreationDTO;
import com.anycart.anycart.dto.ProductDetailsDTO;
import com.anycart.anycart.dto.ProductListDTO;
import com.anycart.anycart.entities.Product;

@Component
public class ProductMapper {

    public ProductListDTO toProductListDTO(Product product) {
        return new ProductListDTO(
            product.getId(),
            product.getName(),
            product.getPrice(),
            product.getImageUrl(),
            product.getCategory().getName()
        );
    }

    public List<ProductListDTO> toProductListDTOs(List<Product> products) {
        return products.stream().map(this::toProductListDTO).collect(Collectors.toList());
    }

    public ProductDetailsDTO toProductDetailsDTO(Product product) {
        return new ProductDetailsDTO(
            product.getId(),
            product.getName(),
            product.getDescription(),
            product.getPrice(),
            product.getStock(),
            product.getImageUrl(),
            product.getCategory().getName(),
            null // set reviews later if needed
        );
    }

    public Product toProduct(ProductCreationDTO dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setImageUrl(dto.getImageUrl());
        product.setCreatedAt(LocalDateTime.now());
        return product;
    }
}
