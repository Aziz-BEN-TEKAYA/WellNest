package tn.esprit.ms2.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.ms2.dto.ActivityGoalRequest;
import tn.esprit.ms2.dto.ActivityGoalResponse;
import tn.esprit.ms2.entity.ActivityGoal;
import tn.esprit.ms2.exception.ResourceNotFoundException;
import tn.esprit.ms2.mapper.ActivityGoalMapper;
import tn.esprit.ms2.repository.ActivityGoalRepository;
import tn.esprit.ms2.service.ActivityGoalCommandService;
import tn.esprit.ms2.service.ActivityGoalQueryService;
import tn.esprit.ms2.service.support.Periods;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ActivityGoalServiceImpl implements ActivityGoalQueryService, ActivityGoalCommandService {

    private static final String END_BEFORE_START = "La date de fin doit etre posterieure ou egale a la date de debut";

    private final ActivityGoalRepository goalRepository;
    private final ActivityGoalMapper goalMapper;

    @Override
    public List<ActivityGoalResponse> findByUser(Long userId, boolean activeOnly) {
        List<ActivityGoal> goals = activeOnly
                ? goalRepository.findByUserIdAndActiveTrue(userId)
                : goalRepository.findByUserId(userId);
        return goals.stream().map(goalMapper::toResponse).toList();
    }

    @Override
    public ActivityGoalResponse findById(Long id) {
        return goalMapper.toResponse(getOrThrow(id));
    }

    @Override
    @Transactional
    public ActivityGoalResponse create(ActivityGoalRequest request) {
        Periods.requireOrdered(request.startDate(), request.endDate(), END_BEFORE_START);
        return goalMapper.toResponse(goalRepository.save(goalMapper.toEntity(request)));
    }

    @Override
    @Transactional
    public ActivityGoalResponse update(Long id, ActivityGoalRequest request) {
        ActivityGoal goal = getOrThrow(id);
        Periods.requireOrdered(request.startDate(), request.endDate(), END_BEFORE_START);
        goalMapper.updateEntity(goal, request);
        return goalMapper.toResponse(goal);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        goalRepository.delete(getOrThrow(id));
    }

    private ActivityGoal getOrThrow(Long id) {
        return goalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Objectif", id));
    }
}
