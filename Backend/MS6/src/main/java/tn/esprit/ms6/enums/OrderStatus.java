package tn.esprit.ms6.enums;

/**
 * Cycle de vie d'une commande.
 * PENDING -> PAID -> SHIPPED -> DELIVERED
 * PENDING -> PAYMENT_FAILED
 * PENDING ou PAID -> CANCELLED (tant que la commande n'est pas expediee)
 */
public enum OrderStatus {
    PENDING,
    PAYMENT_FAILED,
    PAID,
    SHIPPED,
    DELIVERED,
    CANCELLED
}
