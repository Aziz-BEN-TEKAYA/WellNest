package tn.esprit.ms4.exception;

/** Donnee syntaxiquement correcte mais incoherente (ex. reveil avant le coucher). */
public class BusinessRuleException extends RuntimeException {
    public BusinessRuleException(String message) {
        super(message);
    }
}
