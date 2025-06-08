package com.anycart.anycart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anycart.anycart.entities.Product;

public interface ProductRepository extends JpaRepository<Product,Long> {
    List<Product> findByCategoryId(Long categoryId);


}
