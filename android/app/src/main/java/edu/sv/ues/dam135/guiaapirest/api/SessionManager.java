package edu.sv.ues.dam135.guiaapirest.api;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Guarda el token JWT de la sesión activa.
 */
public class SessionManager {
    private static final String ARCHIVO = "sesion";
    private static final String CLAVE_TOKEN = "token";

    private final SharedPreferences preferencias;

    public SessionManager(Context contexto) {
        this.preferencias = contexto.getApplicationContext()
                .getSharedPreferences(ARCHIVO, Context.MODE_PRIVATE);
    }

    public void guardarToken(String token) {
        preferencias.edit().putString(CLAVE_TOKEN, token).apply();
    }

    public String obtenerToken() {
        return preferencias.getString(CLAVE_TOKEN, null);
    }

    public void cerrarSesion() {
        preferencias.edit().remove(CLAVE_TOKEN).apply();
    }
}
