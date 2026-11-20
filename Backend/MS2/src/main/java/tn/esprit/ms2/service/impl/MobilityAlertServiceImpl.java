package tn.esprit.ms2.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.ms2.dto.MobilityAlertRequest;
import tn.esprit.ms2.dto.MobilityAlertResponse;
import tn.esprit.ms2.entity.MobilityAlert;
import tn.esprit.ms2.entity.enums.AlertStatus;
import tn.esprit.ms2.exception.ResourceNotFoundException;
import tn.esprit.ms2.mapper.MobilityAlertMapper;
import tn.esprit.ms2.repository.MobilityAlertRepository;
import tn.esprit.ms2.service.MobilityAlertCommandService;
import tn.esprit.ms2.service.MobilityAlertQueryService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MobilityAlertServiceImpl implements MobilityAlertQueryService, MobilityAlertCommandService {

    private final MobilityAlertRepository alertRepository;
    private final MobilityAlertMapper alertMapper;

    @Override
    public List<MobilityAlertResponse> findByUser(Long userId, AlertStatus status) {
        List<MobilityAlert> alerts = status == null
                ? alertRepository.findByUserIdOrderByDetectedOnDesc(userId)
                : alertRepository.findByUserIdAndStatusOrderByDetectedOnDesc(userId, status);
        return alerts.stream().map(alertMapper::toResponse).toList();
    }

    @Override
    public MobilityAlertResponse findById(Long id) {
        return alertMapper.toResponse(getOrThrow(id));
    }

    @Override
    @Transactional
    public MobilityAlertResponse create(MobilityAlertRequest request) {
        MobilityAlert alert = alertMapper.toEntity(request, deviationPercent(request));
        return alertMapper.toResponse(alertRepository.save(alert));
    }

    @Override
    @Transactional
    public MobilityAlertResponse update(Long id, MobilityAlertRequest request) {
        MobilityAlert alert = getOrThrow(id);
        alertMapper.updateEntity(alert, request, deviationPercent(request));
        return alertMapper.toResponse(alert);
    }

    @Override
    @Transactional
    public MobilityAlertResponse changeStatus(Long id, AlertStatus status) {
        MobilityAlert alert = getOrThrow(id);
        alert.setStatus(status);
        return alertMapper.toResponse(alert);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        alertRepository.delete(getOrThrow(id));
    }

    /** Ecart en % par rapport a la reference, arrondi a 0,1 (negatif = baisse). */
    private double deviationPercent(MobilityAlertRequest request) {
        double baseline = request.baselineValue();
        double raw = (request.observedValue() - baseline) / baseline * 100;
        return Math.round(raw * 10) / 10.0;
    }

    private MobilityAlert getOrThrow(Long id) {
        return alertRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alerte", id));
    }
}
