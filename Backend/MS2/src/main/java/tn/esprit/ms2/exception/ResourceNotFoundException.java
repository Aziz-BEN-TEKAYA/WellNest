package tn.esprit.ms2.exception;

/** Ressource introuvable : traduite en HTTP 404. */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resource, Long id) {
        super(resource + " introuvable (id = " + id + ")");
    }
}
