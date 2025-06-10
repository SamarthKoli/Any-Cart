package com.anycart.anycart.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anycart.anycart.dto.CategoryCreationDTO;
import com.anycart.anycart.dto.CategoryListDTO;
import com.anycart.anycart.services.CategoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/categories")
public class CategoryController {

    @Autowired CategoryService categoryService;


    @GetMapping("/viewAll")
    public ResponseEntity<List<CategoryListDTO>> getAllCategories(){
      
           List<CategoryListDTO> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/viewSubcategory/{parentCategoryId}")
    public ResponseEntity<List<CategoryListDTO>> getSubCategories(@PathVariable Long parentCategoryId){
         List<CategoryListDTO> subCategories = categoryService.getSubCategories(parentCategoryId);
        return ResponseEntity.ok(subCategories);
    }

    @GetMapping("/viewById/{id}")
    public ResponseEntity<CategoryListDTO> getCategoryById(@PathVariable Long id) {
        CategoryListDTO category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }

    @PostMapping("/add")
    public ResponseEntity<CategoryListDTO> createCategory(@Valid @RequestBody CategoryCreationDTO dto) {
        CategoryListDTO category = categoryService.createCategory(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(category);
    }


    @PutMapping("/update/{id}")
     public ResponseEntity<CategoryListDTO> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryCreationDTO dto) {
        CategoryListDTO category = categoryService.updateCategory(id, dto);
        return ResponseEntity.ok(category);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.removeCategory(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler
    public ResponseEntity<String> handleRuntimeException(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }













}
