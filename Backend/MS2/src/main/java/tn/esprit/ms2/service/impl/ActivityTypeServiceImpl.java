package tn.esprit.ms2.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.ms2.dto.ActivityTypeRequest;
import tn.esprit.ms2.dto.ActivityTypeResponse;
import tn.esprit.ms2.entity.ActivityType;
import tn.esprit.ms2.entity.Category;
import tn.esprit.ms2.entity.enums.IntensityLevel;
import tn.esprit.ms2.exception.BusinessRuleException;
import tn.esprit.ms2.exception.DuplicateResourceException;
import tn.esprit.ms2.exception.ResourceNotFoundException;
import tn.esprit.ms2.mapper.ActivityTypeMapper;
import tn.esprit.ms2.repository.ActivitySessionRepository;
import tn.esprit.ms2.repository.ActivityTypeRepository;
import tn.esprit.ms2.repository.CategoryRepository;
import tn.esprit.ms2.service.ActivityTypeAssignmentService;
import tn.esprit.ms2.service.ActivityTypeCommandService;
import tn.esprit.ms2.service.ActivityTypeQueryService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ActivityTypeServiceImpl
        implements ActivityTypeQueryService, ActivityTypeCommandService, ActivityTypeAssignmentService {

    private final ActivityTypeRepository activityTypeRepository;
    private final CategoryRepository categoryRepository;
    private final ActivitySessionRepository sessionRepository;
    private final ActivityTypeMapper activityTypeMapper;

    @Override
    public List<ActivityTypeResponse> search(Long categoryId, IntensityLevel intensity, Boolean suitableForSeniors) {
        return activityTypeRepository.search(categoryId, intensity, suitableForSeniors).stream()
                .map(activityTypeMapper::toResponse)
                .toList();
    }

    @Override
    public List<ActivityTypeResponse> findByCategory(Long categoryId) {
        getCategoryOrThrow(categoryId);
        return activityTypeRepository.findByCategoryIdOrderByNameAsc(categoryId).stream()
                .map(activityTypeMapper::toResponse)
                .toList();
    }

    @Override
    public ActivityTypeResponse findById(Long id) {
        return activityTypeMapper.toResponse(getOrThrow(id));
    }

    @Override
    @Transactional
    public ActivityTypeResponse create(ActivityTypeRequest request) {
        if (activityTypeRepository.existsByNameIgnoreCase(request.name().trim())) {
            throw new DuplicateResourceException("Un type d'activite nomme '" + request.name().trim() + "' existe deja");
        }
        Category category = getCategoryOrThrow(request.categoryId());
        ActivityType saved = activityTypeRepository.save(activityTypeMapper.toEntity(request, category));
        return activityTypeMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public ActivityTypeResponse update(Long id, ActivityTypeRequest request) {
        ActivityType type = getOrThrow(id);
        if (activityTypeRepository.existsByNameIgnoreCaseAndIdNot(request.name().trim(), id)) {
            throw new DuplicateResourceException("Un type d'activite nomme '" + request.name().trim() + "' existe deja");
        }
        Category category = getCategoryOrThrow(request.categoryId());
        activityTypeMapper.updateEntity(type, request, category);
        return activityTypeMapper.toResponse(type);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        ActivityType type = getOrThrow(id);
        if (sessionRepository.existsByActivityTypeId(id)) {
            throw new BusinessRuleException(
                    "Impossible de supprimer ce type d'activite : des seances l'utilisent");
        }
        activityTypeRepository.delete(type);
    }

    @Override
    @Transactional
    public ActivityTypeResponse assignToCategory(Long activityTypeId, Long categoryId) {
        ActivityType type = getOrThrow(activityTypeId);
        type.assignToCategory(getCategoryOrThrow(categoryId));
        return activityTypeMapper.toResponse(type);
    }

    private ActivityType getOrThrow(Long id) {
        return activityTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Type d'activite", id));
    }

    private Category getCategoryOrThrow(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Categorie", categoryId));
    }
}
