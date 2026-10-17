package edu.sv.ues.dam135.apirest.services;
import edu.sv.ues.dam135.apirest.dtos.TokenDTO;
import edu.sv.ues.dam135.apirest.dtos.UserRegisterDTO;

public interface AuthServices {
    public TokenDTO login(String user, String pass);
    boolean register(UserRegisterDTO user);
    void logout(String token);

}
