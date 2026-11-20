package tn.esprit.ms2.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.ms2.dto.ActivityRestrictionRequest;
import tn.esprit.ms2.dto.ActivityRestrictionResponse;
import tn.esprit.ms2.entity.ActivityRestriction;
import tn.esprit.ms2.entity.Category;
import tn.esprit.ms2.exception.BusinessRuleException;
import tn.esprit.ms2.exception.DuplicateResourceException;
import tn.esprit.ms2.exception.ResourceNotFoundException;
import tn.esprit.ms2.mapper.ActivityRestrictionMapper;
import tn.esprit.ms2.repository.ActivityRestrictionRepository;
import tn.esprit.ms2.repository.CategoryRepository;
import tn.esprit.ms2.service.ActivityRestrictionCommandService;
import tn.esprit.ms2.service.ActivityRestrictionQueryService;
import tn.esprit.ms2.service.RestrictionCategoryAssignmentService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ActivityRestrictionServiceImpl implements ActivityRestrictionQueryService,
        ActivityRestrictionCommandService, RestrictionCategoryAssignmentService {

    private final ActivityRestrictionRepository restrictionRepository;
    private final CategoryRepository categoryRepository;
    private final ActivityRestrictionMapper restrictionMapper;

    @Override
    public List<ActivityRestrictionResponse> findByUser(Long userId) {
        return restrictionRepository.findByUserId(userId).stream()
                .map(restrictionMapper::toResponse)
                .toList();
    }

    @Override
    public ActivityRestrictionResponse findById(Long id) {
        return restrictionMapper.toResponse(getOrThrow(id));
    }

    @Override
    @Transactional
    public ActivityRestrictionResponse create(ActivityRestrictionRequest request) {
        Set<Category> excluded = resolveCategories(request.excludedCategoryIds());
        ActivityRestriction saved = restrictionRepository.save(restrictionMapper.toEntity(request, excluded));
        return restrictionMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public ActivityRestrictionResponse update(Long id, ActivityRestrictionRequest request) {
        ActivityRestriction restriction = getOrThrow(id);
        restrictionMapper.updateEntity(restriction, request, resolveCategories(request.excludedCategoryIds()));
        return restrictionMapper.toResponse(restriction);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        restrictionRepository.delete(getOrThrow(id));
    }

    @Override
    @Transactional
    public ActivityRestrictionResponse assignExcludedCategory(Long restrictionId, Long categoryId) {
        ActivityRestriction restriction = getOrThrow(restrictionId);
        Category category = getCategoryOrThrow(categoryId);
        if (!restriction.excludeCategory(category)) {
            throw new DuplicateResourceException(
                    "La categorie '" + category.getName() + "' est deja exclue par cette consigne");
        }
        return restrictionMapper.toResponse(restriction);
    }

    @Override
    @Transactional
    public ActivityRestrictionResponse unassignExcludedCategory(Long restrictionId, Long categoryId) {
        ActivityRestriction restriction = getOrThrow(restrictionId);
        Category category = getCategoryOrThrow(categoryId);
        if (!restriction.removeExcludedCategory(category)) {
            throw new BusinessRuleException(
                    "La categorie '" + category.getName() + "' n'est pas exclue par cette consigne");
        }
        return restrictionMapper.toResponse(restriction);
    }

    private Category getCategoryOrThrow(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categorie", id));
    }

    /** Charge les categories exclues ; une seule inconnue suffit a refuser la consigne (404). */
    private Set<Category> resolveCategories(Set<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Set.of();
        }
        Set<Category> categories = new HashSet<>(categoryRepository.findAllById(ids));
        if (categories.size() != ids.size()) {
            Set<Long> found = new HashSet<>();
            categories.forEach(category -> found.add(category.getId()));
            Long missing = ids.stream().filter(id -> !found.contains(id)).findFirst().orElseThrow();
            throw new ResourceNotFoundException("Categorie", missing);
        }
        return categories;
    }

    private ActivityRestriction getOrThrow(Long id) {
        return restrictionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consigne d'activite", id));
    }
}
