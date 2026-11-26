package tn.esprit.ms6.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.ms6.dto.PaymentRequest;
import tn.esprit.ms6.dto.PaymentResponse;
import tn.esprit.ms6.entity.Order;
import tn.esprit.ms6.entity.Payment;
import tn.esprit.ms6.enums.OrderStatus;
import tn.esprit.ms6.enums.PaymentStatus;
import tn.esprit.ms6.exception.ConflictException;
import tn.esprit.ms6.exception.ResourceNotFoundException;
import tn.esprit.ms6.mapper.PaymentMapper;
import tn.esprit.ms6.repository.PaymentRepository;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderService orderService;
    private final PaymentMapper paymentMapper;

    /** Paiement simulé : réussite ou échec selon simulateFailure. */
    @Transactional
    public PaymentResponse pay(Long orderId, PaymentRequest request) {
        Order order = orderService.getEntity(orderId);
        if (paymentRepository.findByOrderId(orderId).isPresent()) {
            throw new ConflictException("Cette commande a déjà un paiement");
        }
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new ConflictException("Seule une commande en attente (PENDING) peut être payée : "
                    + "statut actuel = " + order.getStatus());
        }

        boolean failure = Boolean.TRUE.equals(request.simulateFailure());
        Payment payment = Payment.builder()
                .amount(order.getTotalAmount())   // le montant vient de la commande, jamais du client
                .currency(Payment.DEFAULT_CURRENCY)
                .method(request.method())
                .status(failure ? PaymentStatus.FAILED : PaymentStatus.COMPLETED)
                .order(order)
                .build();
        paymentRepository.save(payment);

        if (failure) {
            orderService.markPaymentFailed(orderId); // PAYMENT_FAILED + stock rendu
        } else {
            orderService.markPaid(orderId);          // PAID
        }

        // On relit le paiement : après la restitution du stock, la session a été vidée.
        return paymentMapper.toResponse(getEntity(orderId));
    }

    @Transactional(readOnly = true)
    public PaymentResponse findByOrderId(Long orderId) {
        orderService.getEntity(orderId); // 404 si la commande n'existe pas
        return paymentMapper.toResponse(getEntity(orderId));
    }

    private Payment getEntity(Long orderId) {
        return paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Aucun paiement pour la commande " + orderId));
    }
}