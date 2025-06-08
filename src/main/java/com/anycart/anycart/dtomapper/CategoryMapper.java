package com.anycart.anycart.dtomapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.anycart.anycart.dto.CategoryCreationDTO;
import com.anycart.anycart.dto.CategoryListDTO;
import com.anycart.anycart.entities.Category;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    @Mapping(source = "parentCategory.name", target = "parentCategoryName")
    CategoryListDTO toCategoryListDTO(Category category);

    List<CategoryListDTO> toCategoryListDTOs(List<Category> categories);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "parentCategory", source = "parentCategoryId")
    Category toCategory(CategoryCreationDTO dto);
}