package edu.sv.ues.dam135.guiaapirest.models;

/**
 * Cuerpo de la petición de inicio de sesión. Los nombres de los campos
 * coinciden con LoginDTO de la API (user y pass).
 */
public class LoginRequest {
    private final String user;
    private final String pass;

    public LoginRequest(String user, String pass) {
        this.user = user;
        this.pass = pass;
    }

    public String getUser() {
        return user;
    }

    public String getPass() {
        return pass;
    }
}
