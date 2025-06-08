package com.anycart.anycart.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anycart.anycart.dto.ProductCreationDTO;
import com.anycart.anycart.dto.ProductDetailsDTO;
import com.anycart.anycart.dto.ProductListDTO;
import com.anycart.anycart.services.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/products")
public class ProductController {

    @Autowired ProductService productService;
    

    @GetMapping("/viewAll")
    public ResponseEntity<?> getAllProducts(){
        List<ProductListDTO> products=productService.getAllProducts();
        return ResponseEntity.ok(products);

    }



    @PostMapping("/add")
    public ResponseEntity<?> addProduct(@RequestBody @Valid ProductCreationDTO productCreationDTO){
            ProductDetailsDTO productDetailsDTO=productService.createProduct(productCreationDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(productDetailsDTO);

    }

    

        
    

}
