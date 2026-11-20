package tn.esprit.ms2.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.ms2.dto.SedentaryPeriodRequest;
import tn.esprit.ms2.dto.SedentaryPeriodResponse;
import tn.esprit.ms2.entity.SedentaryPeriod;
import tn.esprit.ms2.exception.ResourceNotFoundException;
import tn.esprit.ms2.mapper.SedentaryPeriodMapper;
import tn.esprit.ms2.repository.SedentaryPeriodRepository;
import tn.esprit.ms2.service.SedentaryPeriodCommandService;
import tn.esprit.ms2.service.SedentaryPeriodQueryService;
import tn.esprit.ms2.service.support.Periods;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SedentaryPeriodServiceImpl implements SedentaryPeriodQueryService, SedentaryPeriodCommandService {

    private final SedentaryPeriodRepository periodRepository;
    private final SedentaryPeriodMapper periodMapper;

    @Override
    public List<SedentaryPeriodResponse> findByUser(Long userId, LocalDate from, LocalDate to) {
        Periods.requireOrderedFilter(from, to);
        return periodRepository.findByUserIdAndStartTimeBetweenOrderByStartTimeAsc(
                        userId, Periods.startOfDay(from), Periods.endOfDay(to)).stream()
                .map(periodMapper::toResponse)
                .toList();
    }

    @Override
    public SedentaryPeriodResponse findById(Long id) {
        return periodMapper.toResponse(getOrThrow(id));
    }

    @Override
    @Transactional
    public SedentaryPeriodResponse create(SedentaryPeriodRequest request) {
        int duration = Periods.durationMinutes(request.startTime(), request.endTime(), request.durationMinutes());
        return periodMapper.toResponse(periodRepository.save(periodMapper.toEntity(request, duration)));
    }

    @Override
    @Transactional
    public SedentaryPeriodResponse update(Long id, SedentaryPeriodRequest request) {
        SedentaryPeriod period = getOrThrow(id);
        int duration = Periods.durationMinutes(request.startTime(), request.endTime(), request.durationMinutes());
        periodMapper.updateEntity(period, request, duration);
        return periodMapper.toResponse(period);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        periodRepository.delete(getOrThrow(id));
    }

    private SedentaryPeriod getOrThrow(Long id) {
        return periodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Periode sedentaire", id));
    }
}
