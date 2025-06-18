package com.anycart.anycart.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anycart.anycart.dto.ProductCreationDTO;
import com.anycart.anycart.dto.ProductDetailsDTO;
import com.anycart.anycart.dto.ProductListDTO;
import com.anycart.anycart.dtomapper.ProductMapper;
import com.anycart.anycart.entities.Category;
import com.anycart.anycart.entities.Product;
import com.anycart.anycart.repository.CategoryRepository;
import com.anycart.anycart.repository.ProductRepository;

import jakarta.validation.Valid;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CloudinaryService cloudinaryService;

    public List<ProductListDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return productMapper.toProductListDTOs(products);
    }

    public List<ProductListDTO> getProductsByCategory(Long categoryId) {
        List<Product> products = productRepository.findByCategoryId(categoryId);
        return productMapper.toProductListDTOs(products);
    }

    public ProductDetailsDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return productMapper.toProductDetailsDTO(product);
    }

    public ProductDetailsDTO createProduct(@Valid ProductCreationDTO newProduct) {
        Category category = categoryRepository.findById(newProduct.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Product product = productMapper.toProduct(newProduct);
        if (newProduct.getImage() != null && !newProduct.getImage().isEmpty()) {
            try {
                String imageUrl = cloudinaryService.uploadImage(newProduct.getImage());
                product.setImageUrl(imageUrl);
            } catch (Exception e) {
                throw new RuntimeException("Failed to upload image: " + e.getMessage());
            }
        }
        product.setCategory(category);
        product = productRepository.save(product);

        return productMapper.toProductDetailsDTO(product);
    }

    public ProductDetailsDTO updateProduct(Long id, @Valid ProductCreationDTO productCreationDTO) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Category category = categoryRepository.findById(productCreationDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Product updatedProduct = productMapper.toProduct(productCreationDTO);
        if (productCreationDTO.getImage() != null && !productCreationDTO.getImage().isEmpty()) {
            try {
                String imageUrl = cloudinaryService.uploadImage(productCreationDTO.getImage());
                updatedProduct.setImageUrl(imageUrl);
            } catch (Exception e) {
                throw new RuntimeException("Failed to upload image: " + e.getMessage());
            }
        } else {
            updatedProduct.setImageUrl(product.getImageUrl()); // Retain existing image URL if no new image
        }
        updatedProduct.setId(id);
        updatedProduct.setCategory(category);
        updatedProduct.setCreatedAt(product.getCreatedAt());
        updatedProduct.setUpdatedAt(java.time.LocalDateTime.now());
        updatedProduct = productRepository.save(updatedProduct);
        return productMapper.toProductDetailsDTO(updatedProduct);
    }
}