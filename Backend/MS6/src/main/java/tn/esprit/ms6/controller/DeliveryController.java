package tn.esprit.ms6.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.ms6.dto.DeliveryRequest;
import tn.esprit.ms6.dto.DeliveryResponse;
import tn.esprit.ms6.dto.DeliveryUpdateRequest;
import tn.esprit.ms6.dto.TrackingResponse;
import tn.esprit.ms6.service.DeliveryService;

@RestController
@RequestMapping("/api/v1/store")
@RequiredArgsConstructor
@Tag(name = "Livraisons", description = "Livraison d'une commande et suivi par numéro")
public class DeliveryController {

    private final DeliveryService deliveryService;

    @PostMapping("/orders/{orderId}/delivery")
    public ResponseEntity<DeliveryResponse> create(@PathVariable Long orderId,
                                                   @Valid @RequestBody DeliveryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(deliveryService.create(orderId, request));
    }

    @GetMapping("/orders/{orderId}/delivery")
    public DeliveryResponse findByOrderId(@PathVariable Long orderId) {
        return deliveryService.findByOrderId(orderId);
    }

    @PutMapping("/orders/{orderId}/delivery")
    public DeliveryResponse update(@PathVariable Long orderId,
                                   @Valid @RequestBody DeliveryUpdateRequest request) {
        return deliveryService.update(orderId, request);
    }

    @GetMapping("/deliveries/track/{trackingNumber}")
    public TrackingResponse track(@PathVariable String trackingNumber) {
        return deliveryService.track(trackingNumber);
    }
}