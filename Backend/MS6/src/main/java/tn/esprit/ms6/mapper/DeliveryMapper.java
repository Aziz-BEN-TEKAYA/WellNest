package tn.esprit.ms6.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.ms6.dto.DeliveryResponse;
import tn.esprit.ms6.dto.TrackingResponse;
import tn.esprit.ms6.entity.Delivery;

@Component
public class DeliveryMapper {

    public DeliveryResponse toResponse(Delivery delivery) {
        return new DeliveryResponse(
                delivery.getId(),
                delivery.getOrder().getId(),
                delivery.getAddress(),
                delivery.getStatus(),
                delivery.getTrackingNumber(),
                delivery.getEstimatedDeliveryDate(),
                delivery.getPreparationDate(),
                delivery.getShippingDate(),
                delivery.getDeliveryDate()
        );
    }

    /** Suivi public : volontairement sans adresse. */
    public TrackingResponse toTracking(Delivery delivery) {
        return new TrackingResponse(
                delivery.getTrackingNumber(),
                delivery.getStatus(),
                delivery.getEstimatedDeliveryDate(),
                delivery.getPreparationDate(),
                delivery.getShippingDate(),
                delivery.getDeliveryDate()
        );
    }
}