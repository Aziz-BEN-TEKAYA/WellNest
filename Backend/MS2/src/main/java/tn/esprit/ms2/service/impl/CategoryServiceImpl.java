package tn.esprit.ms2.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.ms2.dto.CategoryRequest;
import tn.esprit.ms2.dto.CategoryResponse;
import tn.esprit.ms2.entity.Category;
import tn.esprit.ms2.exception.BusinessRuleException;
import tn.esprit.ms2.exception.DuplicateResourceException;
import tn.esprit.ms2.exception.ResourceNotFoundException;
import tn.esprit.ms2.mapper.CategoryMapper;
import tn.esprit.ms2.repository.ActivityRestrictionRepository;
import tn.esprit.ms2.repository.ActivityTypeRepository;
import tn.esprit.ms2.repository.CategoryRepository;
import tn.esprit.ms2.service.CategoryCommandService;
import tn.esprit.ms2.service.CategoryQueryService;

import java.util.List;

/**
 * Implemente les deux interfaces Category. Les controleurs ne dependent que de
 * celle dont ils ont besoin (ISP) ; les dependances sont injectees par constructeur (DIP).
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryQueryService, CategoryCommandService {

    private static final String RESOURCE = "Categorie";

    private final CategoryRepository categoryRepository;
    private final ActivityTypeRepository activityTypeRepository;
    private final ActivityRestrictionRepository restrictionRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryResponse> findAll() {
        return categoryRepository.findAllByOrderByNameAsc().stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    @Override
    public CategoryResponse findById(Long id) {
        return categoryMapper.toResponse(getOrThrow(id));
    }

    @Override
    @Transactional
    public CategoryResponse create(CategoryRequest request) {
        if (categoryRepository.existsByNameIgnoreCase(request.name().trim())) {
            throw new DuplicateResourceException("Une categorie nommee '" + request.name().trim() + "' existe deja");
        }
        Category saved = categoryRepository.save(categoryMapper.toEntity(request));
        return categoryMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public CategoryResponse update(Long id, CategoryRequest request) {
        Category category = getOrThrow(id);
        if (categoryRepository.existsByNameIgnoreCaseAndIdNot(request.name().trim(), id)) {
            throw new DuplicateResourceException("Une categorie nommee '" + request.name().trim() + "' existe deja");
        }
        categoryMapper.updateEntity(category, request);
        return categoryMapper.toResponse(category);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Category category = getOrThrow(id);
        if (activityTypeRepository.existsByCategoryId(id)) {
            throw new BusinessRuleException(
                    "Impossible de supprimer la categorie : des types d'activite y sont rattaches");
        }
        if (restrictionRepository.existsByExcludedCategoriesId(id)) {
            throw new BusinessRuleException(
                    "Impossible de supprimer la categorie : elle est exclue par une consigne d'activite");
        }
        categoryRepository.delete(category);
    }

    private Category getOrThrow(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE, id));
    }
}
