package tn.esprit.ms2.exception;

/** Regle metier non respectee : traduite en HTTP 422. */
public class BusinessRuleException extends RuntimeException {

    public BusinessRuleException(String message) {
        super(message);
    }
}
