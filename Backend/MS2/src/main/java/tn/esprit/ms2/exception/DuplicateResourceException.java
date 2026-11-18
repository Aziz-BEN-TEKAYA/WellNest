package tn.esprit.ms2.exception;

/** Conflit d'unicite (ex. nom deja utilise) : traduit en HTTP 409. */
public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String message) {
        super(message);
    }
}
