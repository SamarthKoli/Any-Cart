package com.anycart.anycart.controllers;

import com.anycart.anycart.dto.CategoryCreationDTO;
import com.anycart.anycart.dto.CategoryListDTO;
import com.anycart.anycart.services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/viewAll")
    public ResponseEntity<List<CategoryListDTO>> getAllCategories() {
        List<CategoryListDTO> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/viewSubcategory/{parentCategoryId}")
    public ResponseEntity<List<CategoryListDTO>> getSubCategories(@PathVariable Long parentCategoryId) {
        List<CategoryListDTO> subCategories = categoryService.getSubCategories(parentCategoryId);
        return ResponseEntity.ok(subCategories);
    }

    @GetMapping("/viewById/{id}")
    public ResponseEntity<CategoryListDTO> getCategoryById(@PathVariable Long id) {
        CategoryListDTO category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<CategoryListDTO> createCategory(@Valid @RequestBody CategoryCreationDTO dto) {
        CategoryListDTO category = categoryService.createCategory(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(category);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<CategoryListDTO> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryCreationDTO dto) {
        CategoryListDTO category = categoryService.updateCategory(id, dto);
        return ResponseEntity.ok(category);
    }

    @DeleteMapping("/remove/{id}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.removeCategory(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler
    public ResponseEntity<String> handleRuntimeException(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}