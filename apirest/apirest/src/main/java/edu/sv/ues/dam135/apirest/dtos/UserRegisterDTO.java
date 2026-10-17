package edu.sv.ues.dam135.apirest.dtos;

import lombok.Data;

@Data
public class UserRegisterDTO {
    private String name;
    private String lastname;
    private String email;
    private String password;
}