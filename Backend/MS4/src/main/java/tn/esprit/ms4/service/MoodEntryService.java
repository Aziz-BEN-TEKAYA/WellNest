package tn.esprit.ms4.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.ms4.dto.MoodEntryRequest;
import tn.esprit.ms4.dto.MoodEntryResponse;
import tn.esprit.ms4.dto.PageResponse;
import tn.esprit.ms4.entity.MoodEntry;
import tn.esprit.ms4.exception.ResourceNotFoundException;
import tn.esprit.ms4.mapper.MoodEntryMapper;
import tn.esprit.ms4.repository.MoodEntryRepository;
import tn.esprit.ms4.repository.UserDateRangeSpecs;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class MoodEntryService {

    private final MoodEntryRepository repository;
    private final Clock clock;

    public MoodEntryResponse create(Long userId, MoodEntryRequest request) {
        MoodEntry entry = new MoodEntry();
        entry.setUserId(userId);
        MoodEntryMapper.copyRequest(request, entry, LocalDateTime.now(clock));
        return MoodEntryMapper.toResponse(repository.save(entry));
    }

    @Transactional(readOnly = true)
    public MoodEntryResponse getById(Long userId, Long id) {
        return MoodEntryMapper.toResponse(find(userId, id));
    }

    @Transactional(readOnly = true)
    public PageResponse<MoodEntryResponse> list(Long userId, LocalDate from, LocalDate to, int page, int size) {
        SleepRecordService.checkPeriod(from, to);
        var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "recordedAt"));
        var spec = UserDateRangeSpecs.<MoodEntry>forDateTimeField(userId, "recordedAt", from, to);
        return PageResponse.of(repository.findAll(spec, pageable), MoodEntryMapper::toResponse);
    }

    public MoodEntryResponse update(Long userId, Long id, MoodEntryRequest request) {
        MoodEntry entry = find(userId, id);
        MoodEntryMapper.copyRequest(request, entry, LocalDateTime.now(clock));
        return MoodEntryMapper.toResponse(repository.save(entry));
    }

    public void delete(Long userId, Long id) {
        repository.delete(find(userId, id));
    }

    private MoodEntry find(Long userId, Long id) {
        return repository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Humeur", id));
    }
}
