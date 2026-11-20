package tn.esprit.ms1.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String ressource, String id) {
        super(ressource + " introuvable : " + id);
    }
}
