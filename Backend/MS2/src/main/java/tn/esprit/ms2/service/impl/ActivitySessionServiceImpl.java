package tn.esprit.ms2.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.ms2.dto.ActivitySessionRequest;
import tn.esprit.ms2.dto.ActivitySessionResponse;
import tn.esprit.ms2.entity.ActivitySession;
import tn.esprit.ms2.entity.ActivityType;
import tn.esprit.ms2.exception.ResourceNotFoundException;
import tn.esprit.ms2.mapper.ActivitySessionMapper;
import tn.esprit.ms2.repository.ActivitySessionRepository;
import tn.esprit.ms2.repository.ActivityTypeRepository;
import tn.esprit.ms2.service.ActivitySessionAssignmentService;
import tn.esprit.ms2.service.ActivitySessionCommandService;
import tn.esprit.ms2.service.ActivitySessionQueryService;
import tn.esprit.ms2.service.support.Periods;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ActivitySessionServiceImpl
        implements ActivitySessionQueryService, ActivitySessionCommandService, ActivitySessionAssignmentService {

    private final ActivitySessionRepository sessionRepository;
    private final ActivityTypeRepository activityTypeRepository;
    private final ActivitySessionMapper sessionMapper;

    @Override
    public List<ActivitySessionResponse> findByUser(Long userId, LocalDate from, LocalDate to) {
        Periods.requireOrderedFilter(from, to);
        return sessionRepository.findByUserIdAndStartTimeBetweenOrderByStartTimeAsc(
                        userId, Periods.startOfDay(from), Periods.endOfDay(to)).stream()
                .map(sessionMapper::toResponse)
                .toList();
    }

    @Override
    public ActivitySessionResponse findById(Long id) {
        return sessionMapper.toResponse(getOrThrow(id));
    }

    @Override
    @Transactional
    public ActivitySessionResponse create(ActivitySessionRequest request) {
        ActivityType type = getTypeOrThrow(request.activityTypeId());
        int duration = Periods.durationMinutes(request.startTime(), request.endTime(), request.durationMinutes());
        ActivitySession saved = sessionRepository.save(sessionMapper.toEntity(request, type, duration));
        return sessionMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public ActivitySessionResponse update(Long id, ActivitySessionRequest request) {
        ActivitySession session = getOrThrow(id);
        ActivityType type = getTypeOrThrow(request.activityTypeId());
        int duration = Periods.durationMinutes(request.startTime(), request.endTime(), request.durationMinutes());
        sessionMapper.updateEntity(session, request, type, duration);
        return sessionMapper.toResponse(session);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        ActivitySession session = getOrThrow(id);
        session.detachFromActivityType();
        sessionRepository.delete(session);
    }

    @Override
    @Transactional
    public ActivitySessionResponse assignToActivityType(Long sessionId, Long activityTypeId) {
        ActivitySession session = getOrThrow(sessionId);
        session.assignToActivityType(getTypeOrThrow(activityTypeId));
        return sessionMapper.toResponse(session);
    }

    private ActivitySession getOrThrow(Long id) {
        return sessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Seance", id));
    }

    private ActivityType getTypeOrThrow(Long id) {
        return activityTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Type d'activite", id));
    }
}
