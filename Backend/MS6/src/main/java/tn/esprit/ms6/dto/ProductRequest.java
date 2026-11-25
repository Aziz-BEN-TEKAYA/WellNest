package tn.esprit.ms6.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import tn.esprit.ms6.enums.AutonomyNeed;
import tn.esprit.ms6.enums.Obstacle;
import tn.esprit.ms6.enums.UsageDuration;

import java.math.BigDecimal;
import java.util.Set;

public record ProductRequest(

        @NotBlank(message = "Le nom est obligatoire")
        @Size(max = 150, message = "Le nom ne doit pas dépasser 150 caractères")
        String name,

        @Size(max = 1000, message = "La description ne doit pas dépasser 1000 caractères")
        String description,

        @NotNull(message = "Le prix est obligatoire")
        @DecimalMin(value = "0.001", message = "Le prix doit être supérieur à 0")
        @Digits(integer = 9, fraction = 3, message = "Le prix doit avoir au maximum 3 décimales (millimes)")
        BigDecimal price,

        @NotNull(message = "Le stock est obligatoire")
        @Min(value = 0, message = "Le stock ne peut pas être négatif")
        Integer stock,

        @Size(max = 500, message = "Le lien de l'image ne doit pas dépasser 500 caractères")
        String image,

        @PositiveOrZero(message = "Le poids ne peut pas être négatif")
        @Digits(integer = 4, fraction = 2, message = "Poids invalide (4 chiffres et 2 décimales maximum)")
        BigDecimal weightKg,

        @Positive(message = "La largeur doit être positive")
        @Digits(integer = 5, fraction = 1, message = "Largeur invalide (1 décimale maximum)")
        BigDecimal widthCm,

        @Positive(message = "La hauteur doit être positive")
        @Digits(integer = 5, fraction = 1, message = "Hauteur invalide (1 décimale maximum)")
        BigDecimal heightCm,

        @Positive(message = "La profondeur doit être positive")
        @Digits(integer = 5, fraction = 1, message = "Profondeur invalide (1 décimale maximum)")
        BigDecimal depthCm,

        @Positive(message = "La largeur pliée doit être positive")
        @Digits(integer = 5, fraction = 1, message = "Largeur pliée invalide (1 décimale maximum)")
        BigDecimal foldedWidthCm,

        @Positive(message = "La largeur minimale de passage doit être positive")
        @Digits(integer = 5, fraction = 1, message = "Largeur de passage invalide (1 décimale maximum)")
        BigDecimal minimumDoorWidthCm,

        @Size(max = 100, message = "Le matériau ne doit pas dépasser 100 caractères")
        String material,

        @Min(value = 0, message = "La garantie ne peut pas être négative")
        Integer warrantyMonths,

        @Positive(message = "La charge maximale doit être positive")
        Integer maxLoadKg,

        /** Information seulement : la boutique n'installe pas. Absent = false. */
        Boolean installationRequired,

        /** Absent = BOTH. */
        UsageDuration usageDuration,

        @NotNull(message = "La catégorie est obligatoire")
        Long categoryId,

        /** Absent = aucun obstacle. */
        Set<Obstacle> obstacles,

        /** Absent = aucun besoin. */
        Set<AutonomyNeed> needs
) {
}