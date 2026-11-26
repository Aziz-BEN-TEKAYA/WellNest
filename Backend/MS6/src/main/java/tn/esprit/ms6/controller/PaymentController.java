package tn.esprit.ms6.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.ms6.dto.PaymentRequest;
import tn.esprit.ms6.dto.PaymentResponse;
import tn.esprit.ms6.service.PaymentService;

@RestController
@RequestMapping("/api/v1/store/orders/{orderId}/payment")
@RequiredArgsConstructor
@Tag(name = "Paiements", description = "Paiement simulé d'une commande (aucune donnée de carte)")
public class PaymentController {

    private final PaymentService paymentService;

    /** 201 même si le paiement échoue : le paiement est créé, son résultat est dans « status ». */
    @PostMapping
    public ResponseEntity<PaymentResponse> pay(@PathVariable Long orderId,
                                               @Valid @RequestBody PaymentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.pay(orderId, request));
    }

    @GetMapping
    public PaymentResponse findByOrderId(@PathVariable Long orderId) {
        return paymentService.findByOrderId(orderId);
    }
}