package edu.sv.ues.dam135.apirest.repositories;

import edu.sv.ues.dam135.apirest.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Integer>{
    @Query ("SELECT u FROM User u WHERE u.email = :email ")
    User findByEmail(@Param("email")String email);
}
