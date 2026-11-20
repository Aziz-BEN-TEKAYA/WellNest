package tn.esprit.ms2.service.support;

import tn.esprit.ms2.exception.BusinessRuleException;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

/** Outils de periode partages par les services (bornes de filtrage, coherence debut/fin, duree). */
public final class Periods {

    private static final LocalDate MIN_DATE = LocalDate.of(1900, 1, 1);
    private static final LocalDate MAX_DATE = LocalDate.of(9999, 12, 31);

    private Periods() {
    }

    /** Borne basse d'un filtre : sans date, aucune limite. */
    public static LocalDate fromOrMin(LocalDate from) {
        return from != null ? from : MIN_DATE;
    }

    /** Borne haute d'un filtre : sans date, aucune limite. */
    public static LocalDate toOrMax(LocalDate to) {
        return to != null ? to : MAX_DATE;
    }

    public static LocalDateTime startOfDay(LocalDate from) {
        return fromOrMin(from).atStartOfDay();
    }

    public static LocalDateTime endOfDay(LocalDate to) {
        return toOrMax(to).atTime(23, 59, 59);
    }

    public static void requireOrderedFilter(LocalDate from, LocalDate to) {
        if (from != null && to != null && to.isBefore(from)) {
            throw new BusinessRuleException("La date 'to' doit etre posterieure ou egale a 'from'");
        }
    }

    public static void requireOrdered(LocalDate start, LocalDate end, String message) {
        if (end != null && end.isBefore(start)) {
            throw new BusinessRuleException(message);
        }
    }

    /**
     * Duree en minutes : calculee a partir de l'heure de fin si elle est connue,
     * sinon la duree fournie est obligatoire.
     */
    public static int durationMinutes(LocalDateTime start, LocalDateTime end, Integer provided) {
        if (end != null) {
            if (!end.isAfter(start)) {
                throw new BusinessRuleException("L'heure de fin doit etre posterieure a l'heure de debut");
            }
            return (int) Duration.between(start, end).toMinutes();
        }
        if (provided == null) {
            throw new BusinessRuleException("Indiquez l'heure de fin ou la duree en minutes");
        }
        return provided;
    }
}
