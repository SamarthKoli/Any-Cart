package com.anycart.anycart.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;

import com.anycart.anycart.entities.Category;

public interface CategoryRepositpry  extends JpaRepository<Category,Long> {
    List<Category> findByParentCategoryId(Long ParentCategoryId);
    
}
