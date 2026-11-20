package tn.esprit.ms2.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.ms2.dto.CategoryRequest;
import tn.esprit.ms2.dto.CategoryResponse;
import tn.esprit.ms2.entity.Category;

/** Conversions Category <-> DTO (seule responsabilite : SRP). */
@Component
public class CategoryMapper {

    public Category toEntity(CategoryRequest request) {
        Category category = new Category();
        updateEntity(category, request);
        return category;
    }

    public void updateEntity(Category category, CategoryRequest request) {
        category.setName(request.name().trim());
        category.setDescription(request.description());
        category.setIcon(request.icon());
        category.setDefaultIntensity(request.defaultIntensity());
    }

    public CategoryResponse toResponse(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.getIcon(),
                category.getDefaultIntensity(),
                category.getActivityTypes().size());
    }
}
