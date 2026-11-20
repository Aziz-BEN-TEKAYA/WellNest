package tn.esprit.ms2.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.ms2.dto.DailySummaryRequest;
import tn.esprit.ms2.dto.DailySummaryResponse;
import tn.esprit.ms2.entity.DailyActivitySummary;
import tn.esprit.ms2.exception.BusinessRuleException;
import tn.esprit.ms2.exception.DuplicateResourceException;
import tn.esprit.ms2.exception.ResourceNotFoundException;
import tn.esprit.ms2.mapper.DailySummaryMapper;
import tn.esprit.ms2.repository.DailyActivitySummaryRepository;
import tn.esprit.ms2.service.DailySummaryCommandService;
import tn.esprit.ms2.service.DailySummaryQueryService;
import tn.esprit.ms2.service.support.Periods;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DailySummaryServiceImpl implements DailySummaryQueryService, DailySummaryCommandService {

    private static final int MINUTES_PER_DAY = 1440;

    private final DailyActivitySummaryRepository summaryRepository;
    private final DailySummaryMapper summaryMapper;

    @Override
    public List<DailySummaryResponse> findByUser(Long userId, LocalDate from, LocalDate to) {
        Periods.requireOrderedFilter(from, to);
        return summaryRepository.findByUserIdAndDateBetweenOrderByDateAsc(
                        userId, Periods.fromOrMin(from), Periods.toOrMax(to)).stream()
                .map(summaryMapper::toResponse)
                .toList();
    }

    @Override
    public DailySummaryResponse findById(Long id) {
        return summaryMapper.toResponse(getOrThrow(id));
    }

    @Override
    @Transactional
    public DailySummaryResponse create(DailySummaryRequest request) {
        requireCoherentMinutes(request);
        if (summaryRepository.existsByUserIdAndDate(request.userId(), request.date())) {
            throw duplicate(request);
        }
        return summaryMapper.toResponse(summaryRepository.save(summaryMapper.toEntity(request)));
    }

    @Override
    @Transactional
    public DailySummaryResponse update(Long id, DailySummaryRequest request) {
        DailyActivitySummary summary = getOrThrow(id);
        requireCoherentMinutes(request);
        if (summaryRepository.existsByUserIdAndDateAndIdNot(request.userId(), request.date(), id)) {
            throw duplicate(request);
        }
        summaryMapper.updateEntity(summary, request);
        return summaryMapper.toResponse(summary);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        summaryRepository.delete(getOrThrow(id));
    }

    private void requireCoherentMinutes(DailySummaryRequest request) {
        int active = request.activeMinutes() != null ? request.activeMinutes() : 0;
        int sedentary = request.sedentaryMinutes() != null ? request.sedentaryMinutes() : 0;
        if (active + sedentary > MINUTES_PER_DAY) {
            throw new BusinessRuleException("Minutes actives et sedentaires depassent 1440 minutes (24 h)");
        }
    }

    private DuplicateResourceException duplicate(DailySummaryRequest request) {
        return new DuplicateResourceException("Un resume existe deja pour l'utilisateur "
                + request.userId() + " le " + request.date());
    }

    private DailyActivitySummary getOrThrow(Long id) {
        return summaryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resume quotidien", id));
    }
}
