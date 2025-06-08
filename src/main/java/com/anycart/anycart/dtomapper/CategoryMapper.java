package com.anycart.anycart.dtomapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.anycart.anycart.dto.CategoryCreationDTO;
import com.anycart.anycart.dto.CategoryListDTO;
import com.anycart.anycart.entities.Category;

@Component
public class CategoryMapper {

    public CategoryListDTO toCategoryListDTO(Category category) {
        return new CategoryListDTO(
            category.getId(),
            category.getName(),
            category.getParentCategory() != null ? category.getParentCategory().getName() : null
        );
    }

    public List<CategoryListDTO> toCategoryListDTOs(List<Category> categories) {
        return categories.stream().map(this::toCategoryListDTO).collect(Collectors.toList());
    }

    public Category toCategory(CategoryCreationDTO dto) {
        Category category = new Category();
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        // setParentCategory externally in service layer using repo
        return category;
    }
}

