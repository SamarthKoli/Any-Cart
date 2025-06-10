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


    @Autowired ProductRepository productRepository;

    @Autowired CategoryRepository categoryRepository;

    @Autowired  ProductMapper productMapper;


    public List<ProductListDTO> getAllProducts(){


        List<Product> products=productRepository.findAll();
        
        return productMapper.toProductListDTOs(products);
    }

    public List<ProductListDTO>getProductsByCategory(Long CategoryId){
        List<Product>products=productRepository.findByCategoryId(CategoryId);
        return productMapper.toProductListDTOs(products);
    }

     public ProductDetailsDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Product not found"));
        return productMapper.toProductDetailsDTO(product);
    }


    public ProductDetailsDTO createProduct(@Valid ProductCreationDTO newpProduct){
        Category category=categoryRepository.findById(newpProduct.getCategoryId()).
        orElseThrow(() -> new RuntimeException("Category not found"));

        Product product=productMapper.toProduct(newpProduct);
        product.setCategory(category);
        productRepository.save(product);

        return productMapper.toProductDetailsDTO(product);

        
    }

    public ProductDetailsDTO updateProduct( Long id, @Valid ProductCreationDTO productCreationDTO){

        Product product=productRepository.findById(id)
        .orElseThrow(()-> new RuntimeException("Product not found"));

        Category category=categoryRepository.findById(productCreationDTO.getCategoryId())
        .orElseThrow(()-> new RuntimeException("Category not found"));


        Product updatedProduct=productMapper.toProduct(productCreationDTO);
        updatedProduct.setId(id);
        updatedProduct.setCategory(category);
        updatedProduct.setCreatedAt(product.getCreatedAt());
        updatedProduct.setUpdatedAt(java.time.LocalDateTime.now());
        updatedProduct = productRepository.save(updatedProduct);
        return productMapper.toProductDetailsDTO(updatedProduct);


    }
    

}
