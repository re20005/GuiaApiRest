package edu.sv.ues.dam135.apirest.entities;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table (name="[User]")
public class User {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String lastname;
    private String email;
    private String password;
    private Boolean active;
}