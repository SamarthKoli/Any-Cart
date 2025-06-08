package com.anycart.anycart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.anycart.anycart.entities.Review;

@Repository
public interface ReviewRepository  extends JpaRepository<Review,Long>{

}
