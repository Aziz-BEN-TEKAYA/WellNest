package tn.esprit.ms4.entity.enums;

/**
 * Humeur declaree par le retraite (ecran "Comment vous sentez-vous aujourd'hui ?").
 * Le score (1 a 5) est derive cote backend, jamais saisi par l'utilisateur.
 */
public enum MoodLevel {
    VERY_GOOD(5),   // Tres bien
    GOOD(4),        // Bien
    NEUTRAL(3),     // Moyen
    NOT_GREAT(2),   // Pas tres bien
    BAD(1);         // Mal

    private final int score;

    MoodLevel(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }
}
