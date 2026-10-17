package edu.sv.ues.dam135.apirest.implementations;

import edu.sv.ues.dam135.apirest.config.CustomerDetailServices;
import edu.sv.ues.dam135.apirest.dtos.TokenDTO;
import edu.sv.ues.dam135.apirest.services.AuthServices;
import edu.sv.ues.dam135.apirest.utilities.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import edu.sv.ues.dam135.apirest.dtos.UserRegisterDTO;
import edu.sv.ues.dam135.apirest.entities.User;
import edu.sv.ues.dam135.apirest.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@Service
public class AuthServicesImpl implements AuthServices {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private CustomerDetailServices customerDetailServices;
    @Autowired
    private JwtUtil jwtUtil;
    @Override
    public TokenDTO login(String user, String pass) {
        TokenDTO token = new TokenDTO();
        try {
            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(user, pass)
                    );
            if (authentication.isAuthenticated()) {
                UserDetails usuarioDetail = (UserDetails)
                        authentication.getPrincipal();
                if (customerDetailServices.getUserDetail().getActive()) {
                    token = jwtUtil.generateToken(user, usuarioDetail);
                    return token;
                }
            }
        } catch (BadCredentialsException bad) {
            token.setMsj("Credenciales incorrectas!");
            return null;
        } catch (Exception e) {
            log.error("{}", e);
            token.setMsj("Error innesperado");
            return null;
        }
        return null;
    }
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public boolean register(UserRegisterDTO data) {
        if (userRepository.findByEmail(data.getEmail()) != null) {
            return false; // Ya existe una cuenta con ese correo
        }

        User user = new User();
        user.setName(data.getName());
        user.setLastname(data.getLastname());
        user.setEmail(data.getEmail());
        user.setPassword(passwordEncoder.encode(data.getPassword()));
        user.setActive(true);

        userRepository.save(user);
        return true;
    }

    @Override
    public void logout(String token) {
        jwtUtil.revokeToken(token);
    }
}