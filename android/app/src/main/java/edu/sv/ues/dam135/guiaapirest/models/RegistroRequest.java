package edu.sv.ues.dam135.guiaapirest.models;

/**
 * Cuerpo de la petición de registro. Coincide con UserRegisterDTO de la API.
 */
public class RegistroRequest {
    private final String name;
    private final String lastname;
    private final String email;
    private final String password;

    public RegistroRequest(String name, String lastname, String email, String password) {
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
