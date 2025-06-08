package com.anycart.anycart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anycart.anycart.entities.Category;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByParentCategoryId(Long parentCategoryId);
}