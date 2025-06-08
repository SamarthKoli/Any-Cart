package com.anycart.anycart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anycart.anycart.entities.Cart;

public interface CartRepository  extends JpaRepository<Cart,Long>{
  
}
