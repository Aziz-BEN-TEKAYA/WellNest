package tn.esprit.ms4.repository;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/** Filtres communs : donnees d'un utilisateur, eventuellement limitees a une periode [from, to]. */
public final class UserDateRangeSpecs {

    private UserDateRangeSpecs() {
    }

    /** Pour un champ de type LocalDate (ex. sleepDate). Bornes incluses. */
    public static <T> Specification<T> forDateField(Long userId, String field, LocalDate from, LocalDate to) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("userId"), userId));
            if (from != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get(field), from));
            }
            if (to != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get(field), to));
            }
            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }

    /** Pour un champ de type LocalDateTime (ex. recordedAt). Journees from et to incluses. */
    public static <T> Specification<T> forDateTimeField(Long userId, String field, LocalDate from, LocalDate to) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("userId"), userId));
            if (from != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get(field), from.atStartOfDay()));
            }
            if (to != null) {
                predicates.add(cb.lessThan(root.get(field), to.plusDays(1).atStartOfDay()));
            }
            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }
}
