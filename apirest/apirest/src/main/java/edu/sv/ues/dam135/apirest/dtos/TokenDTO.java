package edu.sv.ues.dam135.apirest.dtos;
import lombok.Data;

@Data
public class TokenDTO {
    private String token;
    private String expireIn;
    private String msj;
}
