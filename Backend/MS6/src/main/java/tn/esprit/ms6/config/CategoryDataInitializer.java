package tn.esprit.ms6.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import tn.esprit.ms6.entity.Category;
import tn.esprit.ms6.repository.CategoryRepository;

import java.util.List;

/** Insere les 7 categories de depart au premier lancement (si la table est vide). */
@Component
@RequiredArgsConstructor
public class CategoryDataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    @Override
    public void run(String... args) {
        if (categoryRepository.count() > 0) {
            return;
        }
        categoryRepository.saveAll(List.of(
                category("Aides à la marche",
                        "Cannes, béquilles, déambulateurs et rollators."),
                category("Fauteuils roulants et accessoires",
                        "Fauteuils manuels et électriques, rampes et accessoires."),
                category("Salle de bain et toilettes",
                        "Barres d'appui, sièges de douche, rehausseurs de toilettes, tapis antidérapants."),
                category("Lits, fauteuils releveurs et assises",
                        "Lits médicalisés, fauteuils releveurs, coussins et assises ergonomiques."),
                category("Accès et escaliers",
                        "Rampes d'accès, mains courantes, monte-escaliers."),
                category("Aide à la vie quotidienne",
                        "Pinces de préhension, ustensiles adaptés, enfile-bas, aides à l'habillage."),
                category("Sécurité et prévention",
                        "Éclairage, détecteurs, alarmes et équipements de prévention des chutes.")
        ));
    }

    private Category category(String name, String description) {
        return Category.builder().name(name).description(description).build();
    }
}
