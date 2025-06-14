package com.anycart.anycart.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anycart.anycart.dto.CategoryCreationDTO;
import com.anycart.anycart.dto.CategoryListDTO;
import com.anycart.anycart.dtomapper.CategoryMapper;
import com.anycart.anycart.entities.Category;
import com.anycart.anycart.repository.CategoryRepository;

import jakarta.validation.Valid;

@Service
public class CategoryService {

    @Autowired CategoryRepository categoryRepository;

    @Autowired CategoryMapper categoryMapper;


    public List<CategoryListDTO> getAllCategories(){
       List<Category> categories=categoryRepository.findAll();
        return categoryMapper.toCategoryListDTOs(categories);

    }

     public List<CategoryListDTO> getSubCategories(Long parentCategoryId) {
        List<Category> categories = categoryRepository.findByParentCategoryId(parentCategoryId);
        return categoryMapper.toCategoryListDTOs(categories);
    }

    public CategoryListDTO getCategoryById(Long CategoryId){
        Category category=categoryRepository.findById(CategoryId).
        orElseThrow(()-> new RuntimeException("Category not found"));
        return categoryMapper.toCategoryListDTO(category);
    }

    public CategoryListDTO createCategory(@Valid CategoryCreationDTO categoryCreationDTO){
        Category parentCategory=null;

        if (categoryCreationDTO.getParentCategoryId()!=null) {
            parentCategory=categoryRepository.findById(categoryCreationDTO.getParentCategoryId())
            .orElseThrow(()-> new RuntimeException("Parent Category not found"));
            }
             Category category=categoryMapper.toCategory(categoryCreationDTO);
            category.setParentCategory(parentCategory);
            category=categoryRepository.save(category);
            return categoryMapper.toCategoryListDTO(category);
    }

 

     public CategoryListDTO updateCategory(Long id, @Valid CategoryCreationDTO dto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        Category parentCategory = null;
        if (dto.getParentCategoryId() != null) {
            parentCategory = categoryRepository.findById(dto.getParentCategoryId())
                    .orElseThrow(() -> new RuntimeException("Parent category not found"));
        }
        Category updatedCategory = categoryMapper.toCategory(dto);
        updatedCategory.setId(id);
        updatedCategory.setParentCategory(parentCategory);
        updatedCategory = categoryRepository.save(updatedCategory);
        return categoryMapper.toCategoryListDTO(updatedCategory);
    }


    public void removeCategory(Long id){
        Category category=categoryRepository.findById(id)
        .orElseThrow(()-> new RuntimeException("Category not found"));
         List<Category> subCategories = categoryRepository.findByParentCategoryId(id);
        if (!subCategories.isEmpty()) {
            throw new RuntimeException("Cannot delete category with subcategories");
        }
        categoryRepository.deleteById(id);
    }






    
}
