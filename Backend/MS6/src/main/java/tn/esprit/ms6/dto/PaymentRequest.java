package tn.esprit.ms6.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record PaymentRequest(
        @NotBlank(message = "Le mode de paiement est obligatoire")
        @Pattern(regexp = "CARD_SIMULATED|CASH_ON_DELIVERY|BANK_TRANSFER",
                message = "Mode de paiement invalide (CARD_SIMULATED, CASH_ON_DELIVERY ou BANK_TRANSFER)")
        String method,

        /** Sert à tester l'échec du paiement. Absent = false (le paiement réussit). */
        Boolean simulateFailure
) {
}