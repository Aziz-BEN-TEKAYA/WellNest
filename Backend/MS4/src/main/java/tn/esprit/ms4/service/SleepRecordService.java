package tn.esprit.ms4.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.ms4.dto.PageResponse;
import tn.esprit.ms4.dto.SleepRecordRequest;
import tn.esprit.ms4.dto.SleepRecordResponse;
import tn.esprit.ms4.entity.SleepRecord;
import tn.esprit.ms4.exception.BusinessRuleException;
import tn.esprit.ms4.exception.DuplicateEntryException;
import tn.esprit.ms4.exception.ResourceNotFoundException;
import tn.esprit.ms4.mapper.SleepRecordMapper;
import tn.esprit.ms4.repository.SleepRecordRepository;
import tn.esprit.ms4.repository.UserDateRangeSpecs;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class SleepRecordService {

    /** Au-dela de 16 h, la saisie est tres probablement une erreur (date du reveil mal choisie...). */
    static final long MAX_SLEEP_MINUTES = 16 * 60;

    private final SleepRecordRepository repository;
    private final Clock clock;

    public SleepRecordResponse create(Long userId, SleepRecordRequest request) {
        SleepRecord record = new SleepRecord();
        record.setUserId(userId);
        SleepRecordMapper.copyRequest(request, record);
        applyRules(record);
        if (repository.existsByUserIdAndSleepDate(userId, record.getSleepDate())) {
            throw new DuplicateEntryException("Une nuit est deja enregistree pour le " + record.getSleepDate()
                    + ". Modifiez-la plutot que d'en creer une nouvelle.");
        }
        return SleepRecordMapper.toResponse(repository.save(record));
    }

    @Transactional(readOnly = true)
    public SleepRecordResponse getById(Long userId, Long id) {
        return SleepRecordMapper.toResponse(find(userId, id));
    }

    @Transactional(readOnly = true)
    public PageResponse<SleepRecordResponse> list(Long userId, LocalDate from, LocalDate to, int page, int size) {
        checkPeriod(from, to);
        var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "sleepDate"));
        var spec = UserDateRangeSpecs.<SleepRecord>forDateField(userId, "sleepDate", from, to);
        return PageResponse.of(repository.findAll(spec, pageable), SleepRecordMapper::toResponse);
    }

    public SleepRecordResponse update(Long userId, Long id, SleepRecordRequest request) {
        SleepRecord record = find(userId, id);
        SleepRecordMapper.copyRequest(request, record);
        applyRules(record);
        if (repository.existsByUserIdAndSleepDateAndIdNot(userId, record.getSleepDate(), id)) {
            throw new DuplicateEntryException("Une autre nuit est deja enregistree pour le " + record.getSleepDate());
        }
        return SleepRecordMapper.toResponse(repository.save(record));
    }

    public void delete(Long userId, Long id) {
        repository.delete(find(userId, id));
    }

    private SleepRecord find(Long userId, Long id) {
        return repository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Nuit", id));
    }

    /** Regles metier de la saisie manuelle : coherence des horaires et calcul de la duree. */
    private void applyRules(SleepRecord record) {
        LocalDateTime bedtime = record.getBedtime();
        LocalDateTime wakeup = record.getWakeupTime();
        if (!wakeup.isAfter(bedtime)) {
            throw new BusinessRuleException("L'heure du reveil doit etre apres l'heure du coucher");
        }
        if (wakeup.isAfter(LocalDateTime.now(clock))) {
            throw new BusinessRuleException("L'heure du reveil ne peut pas etre dans le futur");
        }
        long minutes = Duration.between(bedtime, wakeup).toMinutes();
        if (minutes > MAX_SLEEP_MINUTES) {
            throw new BusinessRuleException("La nuit depasse 16 heures : verifiez les dates du coucher et du reveil");
        }
        record.setDurationMinutes((int) minutes);
        record.setSleepDate(wakeup.toLocalDate());
    }

    static void checkPeriod(LocalDate from, LocalDate to) {
        if (from != null && to != null && from.isAfter(to)) {
            throw new BusinessRuleException("La date 'from' doit etre avant la date 'to'");
        }
    }
}
