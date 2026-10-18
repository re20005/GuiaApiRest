package edu.sv.ues.dam135.guiaapirest.models;

/**
 * Respuesta del inicio de sesión. Coincide con TokenDTO de la API.
 */
public class TokenResponse {
    private String token;
    private String expireIn;
    private String msj;

    public String getToken() {
        return token;
    }

    public String getExpireIn() {
        return expireIn;
    }

    public String getMsj() {
        return msj;
    }
}
