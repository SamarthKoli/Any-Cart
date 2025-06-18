package com.anycart.anycart.dtomapper;

import com.anycart.anycart.dto.ProductCreationDTO;
import com.anycart.anycart.dto.ProductDetailsDTO;
import com.anycart.anycart.dto.ProductListDTO;
import com.anycart.anycart.entities.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    @Autowired
    private ReviewMapper reviewMapper;

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
                reviewMapper.toReviewDTOs(product.getReviews())
        );
    }

    public Product toProduct(ProductCreationDTO dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setCreatedAt(LocalDateTime.now());
        return product;
    }
}