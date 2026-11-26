package tn.esprit.ms4.controller;

public final class ApiConstants {

    public static final String BASE_PATH = "/api/v1/sleep-wellness";

    /**
     * Identifiant du retraite. Temporaire : sera remplace par l'identite lue dans le JWT Keycloak
     * quand la Gateway et Keycloak seront en place.
     */
    public static final String USER_HEADER = "X-User-Id";

    private ApiConstants() {
    }
}
