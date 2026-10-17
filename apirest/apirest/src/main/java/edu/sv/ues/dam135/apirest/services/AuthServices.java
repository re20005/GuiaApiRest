package edu.sv.ues.dam135.apirest.services;
import edu.sv.ues.dam135.apirest.dtos.TokenDTO;

public interface AuthServices {
    public TokenDTO login(String user, String pass);
}
