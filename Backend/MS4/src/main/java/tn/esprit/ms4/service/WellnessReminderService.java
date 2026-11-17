package tn.esprit.ms4.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.ms4.dto.WellnessReminderRequest;
import tn.esprit.ms4.dto.WellnessReminderResponse;
import tn.esprit.ms4.entity.WellnessReminder;
import tn.esprit.ms4.exception.DuplicateEntryException;
import tn.esprit.ms4.exception.ResourceNotFoundException;
import tn.esprit.ms4.mapper.WellnessReminderMapper;
import tn.esprit.ms4.repository.WellnessReminderRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class WellnessReminderService {

    private final WellnessReminderRepository repository;

    public WellnessReminderResponse create(Long userId, WellnessReminderRequest request) {
        WellnessReminder reminder = new WellnessReminder();
        reminder.setUserId(userId);
        WellnessReminderMapper.copyRequest(request, reminder);
        if (repository.existsByUserIdAndReminderTypeAndReminderTime(userId, reminder.getReminderType(), reminder.getReminderTime())) {
            throw new DuplicateEntryException("Ce rappel existe deja a cette heure");
        }
        return WellnessReminderMapper.toResponse(repository.save(reminder));
    }

    @Transactional(readOnly = true)
    public WellnessReminderResponse getById(Long userId, Long id) {
        return WellnessReminderMapper.toResponse(find(userId, id));
    }

    @Transactional(readOnly = true)
    public List<WellnessReminderResponse> list(Long userId) {
        return repository.findByUserIdOrderByReminderTimeAsc(userId).stream()
                .map(WellnessReminderMapper::toResponse)
                .toList();
    }

    public WellnessReminderResponse update(Long userId, Long id, WellnessReminderRequest request) {
        WellnessReminder reminder = find(userId, id);
        WellnessReminderMapper.copyRequest(request, reminder);
        if (repository.existsByUserIdAndReminderTypeAndReminderTimeAndIdNot(userId, reminder.getReminderType(),
                reminder.getReminderTime(), id)) {
            throw new DuplicateEntryException("Ce rappel existe deja a cette heure");
        }
        return WellnessReminderMapper.toResponse(repository.save(reminder));
    }

    public void delete(Long userId, Long id) {
        repository.delete(find(userId, id));
    }

    private WellnessReminder find(Long userId, Long id) {
        return repository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Rappel", id));
    }
}
