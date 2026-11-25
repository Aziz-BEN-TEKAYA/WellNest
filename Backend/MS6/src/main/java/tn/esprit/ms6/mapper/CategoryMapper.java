package tn.esprit.ms6.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.ms6.dto.CategoryRequest;
import tn.esprit.ms6.dto.CategoryResponse;
import tn.esprit.ms6.entity.Category;

@Component
public class CategoryMapper {

    public Category toEntity(CategoryRequest request) {
        return Category.builder()
                .name(request.name().trim())
                .description(request.description())
                .build();
    }

    public void updateEntity(Category category, CategoryRequest request) {
        category.setName(request.name().trim());
        category.setDescription(request.description());
    }

    public CategoryResponse toResponse(Category category) {
        return new CategoryResponse(category.getId(), category.getName(), category.getDescription());
    }
}