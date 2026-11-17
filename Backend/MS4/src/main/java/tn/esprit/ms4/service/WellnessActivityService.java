package tn.esprit.ms4.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.ms4.dto.PageResponse;
import tn.esprit.ms4.dto.WellnessActivityRequest;
import tn.esprit.ms4.dto.WellnessActivityResponse;
import tn.esprit.ms4.entity.WellnessActivity;
import tn.esprit.ms4.exception.ResourceNotFoundException;
import tn.esprit.ms4.mapper.WellnessActivityMapper;
import tn.esprit.ms4.repository.UserDateRangeSpecs;
import tn.esprit.ms4.repository.WellnessActivityRepository;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class WellnessActivityService {

    private final WellnessActivityRepository repository;
    private final Clock clock;

    public WellnessActivityResponse create(Long userId, WellnessActivityRequest request) {
        WellnessActivity activity = new WellnessActivity();
        activity.setUserId(userId);
        WellnessActivityMapper.copyRequest(request, activity, LocalDateTime.now(clock));
        return WellnessActivityMapper.toResponse(repository.save(activity));
    }

    @Transactional(readOnly = true)
    public WellnessActivityResponse getById(Long userId, Long id) {
        return WellnessActivityMapper.toResponse(find(userId, id));
    }

    @Transactional(readOnly = true)
    public PageResponse<WellnessActivityResponse> list(Long userId, LocalDate from, LocalDate to, int page, int size) {
        SleepRecordService.checkPeriod(from, to);
        var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "activityDate"));
        var spec = UserDateRangeSpecs.<WellnessActivity>forDateTimeField(userId, "activityDate", from, to);
        return PageResponse.of(repository.findAll(spec, pageable), WellnessActivityMapper::toResponse);
    }

    public WellnessActivityResponse update(Long userId, Long id, WellnessActivityRequest request) {
        WellnessActivity activity = find(userId, id);
        WellnessActivityMapper.copyRequest(request, activity, LocalDateTime.now(clock));
        return WellnessActivityMapper.toResponse(repository.save(activity));
    }

    public void delete(Long userId, Long id) {
        repository.delete(find(userId, id));
    }

    private WellnessActivity find(Long userId, Long id) {
        return repository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Activite de bien-etre", id));
    }
}
