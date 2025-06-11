package com.anycart.anycart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.anycart.anycart.entities.Cart;

@Repository
public interface CartRepository  extends JpaRepository<Cart,Long>{
  

    public List<Cart> findByUserId(Long userId);
}
