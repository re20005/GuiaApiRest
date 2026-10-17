package edu.sv.ues.dam135.apirest.controllers;

import edu.sv.ues.dam135.apirest.dtos.LoginDTO;
import edu.sv.ues.dam135.apirest.dtos.TokenDTO;
import edu.sv.ues.dam135.apirest.services.AuthServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import edu.sv.ues.dam135.apirest.dtos.UserRegisterDTO;
import org.springframework.web.bind.annotation.RequestHeader;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {
    final private AuthServices authServices;
    private AuthController(AuthServices authServices) {
        this.authServices = authServices;
    }
    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@RequestBody LoginDTO
                                                  authRequest) {
        try {
            System.out.println("DTO enviado : " +
                    authRequest.toString());
            TokenDTO token = authServices.login(authRequest.getUser(),
                    authRequest.getPass());
            if (token == null) {
                return ResponseEntity.status(401).build();
            } else {
                return ResponseEntity.ok(token);
            }
        } catch (Exception e) {
            log.error("{}", e);
        }
        return null;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(
            @RequestBody UserRegisterDTO user) {

        boolean registered = authServices.register(user);

        if (!registered) {
            return ResponseEntity.status(409)
                    .body("Ya existe una cuenta con ese correo");
        }

        return ResponseEntity.status(201)
                .body("Usuario registrado correctamente");
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @RequestHeader("Authorization") String authorization) {

        String token = authorization.startsWith("Bearer ")
                ? authorization.substring(7)
                : authorization;

        authServices.logout(token);
        return ResponseEntity.noContent().build();
    }
}