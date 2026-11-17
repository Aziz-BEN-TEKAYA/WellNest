package tn.esprit.ms4.entity.enums;

/**
 * Rappels de saisie : la saisie etant manuelle (pas de montre connectee),
 * les rappels aident le retraite a ne pas oublier de noter ses donnees.
 */
public enum ReminderType {
    SLEEP_LOG,   // "Comment avez-vous dormi ?" (le matin)
    MOOD_LOG,    // Noter son humeur
    STRESS_LOG,  // Noter son niveau de stress
    RELAXATION   // Moment de detente (le soir)
}
