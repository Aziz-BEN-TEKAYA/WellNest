package tn.esprit.ms4.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.ms4.dto.PageResponse;
import tn.esprit.ms4.dto.StressEntryRequest;
import tn.esprit.ms4.dto.StressEntryResponse;
import tn.esprit.ms4.entity.StressEntry;
import tn.esprit.ms4.exception.ResourceNotFoundException;
import tn.esprit.ms4.mapper.StressEntryMapper;
import tn.esprit.ms4.repository.StressEntryRepository;
import tn.esprit.ms4.repository.UserDateRangeSpecs;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class StressEntryService {

    private final StressEntryRepository repository;
    private final Clock clock;

    public StressEntryResponse create(Long userId, StressEntryRequest request) {
        StressEntry entry = new StressEntry();
        entry.setUserId(userId);
        StressEntryMapper.copyRequest(request, entry, LocalDateTime.now(clock));
        return StressEntryMapper.toResponse(repository.save(entry));
    }

    @Transactional(readOnly = true)
    public StressEntryResponse getById(Long userId, Long id) {
        return StressEntryMapper.toResponse(find(userId, id));
    }

    @Transactional(readOnly = true)
    public PageResponse<StressEntryResponse> list(Long userId, LocalDate from, LocalDate to, int page, int size) {
        SleepRecordService.checkPeriod(from, to);
        var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "recordedAt"));
        var spec = UserDateRangeSpecs.<StressEntry>forDateTimeField(userId, "recordedAt", from, to);
        return PageResponse.of(repository.findAll(spec, pageable), StressEntryMapper::toResponse);
    }

    public StressEntryResponse update(Long userId, Long id, StressEntryRequest request) {
        StressEntry entry = find(userId, id);
        StressEntryMapper.copyRequest(request, entry, LocalDateTime.now(clock));
        return StressEntryMapper.toResponse(repository.save(entry));
    }

    public void delete(Long userId, Long id) {
        repository.delete(find(userId, id));
    }

    private StressEntry find(Long userId, Long id) {
        return repository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Niveau de stress", id));
    }
}
