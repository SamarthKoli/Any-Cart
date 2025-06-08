package com.anycart.anycart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anycart.anycart.entities.Review;

public interface ReviewRepository  extends JpaRepository<Review,Long>{

}
