package tn.esprit.ms4.exception;

/** Saisie deja existante (ex. une nuit deja enregistree pour cette date). */
public class DuplicateEntryException extends RuntimeException {
    public DuplicateEntryException(String message) {
        super(message);
    }
}
