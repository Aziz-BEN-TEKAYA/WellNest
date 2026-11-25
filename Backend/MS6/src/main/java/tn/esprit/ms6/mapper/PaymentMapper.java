package tn.esprit.ms6.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.ms6.dto.PaymentResponse;
import tn.esprit.ms6.entity.Payment;

@Component
public class PaymentMapper {

    public PaymentResponse toResponse(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getOrder().getId(),
                payment.getAmount(),
                payment.getCurrency(),
                payment.getMethod(),
                payment.getStatus(),
                payment.getPaymentDate()
        );
    }
}