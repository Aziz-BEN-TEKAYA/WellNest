package tn.esprit.ms6.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DeliveryRequest(
        @NotBlank(message = "L'adresse de livraison est obligatoire")
        @Size(max = 300, message = "L'adresse ne doit pas dépasser 300 caractères")
        String address
) {
}