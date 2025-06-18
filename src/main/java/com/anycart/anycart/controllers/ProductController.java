package com.anycart.anycart.controllers;

import com.anycart.anycart.dto.ProductCreationDTO;
import com.anycart.anycart.dto.ProductDetailsDTO;
import com.anycart.anycart.dto.ProductListDTO;
import com.anycart.anycart.services.CloudinaryService;
import com.anycart.anycart.services.ProductService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/v1/products")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @GetMapping("/viewAll")
    public ResponseEntity<List<ProductListDTO>> getAllProducts() {
        logger.info("Fetching all products");
        List<ProductListDTO> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/viewById/{id}")
    public ResponseEntity<ProductDetailsDTO> getProductById(@PathVariable Long id) {
        logger.info("Fetching product with id: {}", id);
        ProductDetailsDTO productDetailsDTO = productService.getProductById(id);
        return ResponseEntity.ok(productDetailsDTO);
    }

    @GetMapping("/viewByCategory/{categoryId}")
    public ResponseEntity<List<ProductListDTO>> getProductsByCategory(@PathVariable Long categoryId) {
        logger.info("Fetching products for category id: {}", categoryId);
        List<ProductListDTO> products = productService.getProductsByCategory(categoryId);
        return ResponseEntity.ok(products);
    }

    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<ProductDetailsDTO> addProduct(
            @Valid @RequestPart("product") ProductCreationDTO productCreationDTO,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        logger.info("Creating product with name: {}", productCreationDTO.getName());
        if (image != null && !image.isEmpty()) {
            logger.info("Image provided: {}", image.getOriginalFilename());
            productCreationDTO.setImage(image);
        } else {
            logger.info("No image provided for product creation");
        }
        ProductDetailsDTO productDetailsDTO = productService.createProduct(productCreationDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(productDetailsDTO);
    }

    @PutMapping(value = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<ProductDetailsDTO> updateProduct(
            @PathVariable Long id,
            @Valid @RequestPart("product") ProductCreationDTO productCreationDTO,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        logger.info("Updating product with id: {}", id);
        if (image != null && !image.isEmpty()) {
            logger.info("New image provided: {}", image.getOriginalFilename());
            productCreationDTO.setImage(image);
        } else {
            logger.info("No new image provided for product update");
        }
        ProductDetailsDTO productDetailsDTO = productService.updateProduct(id, productCreationDTO);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(productDetailsDTO);
    }
}