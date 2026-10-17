package edu.sv.ues.dam135.apirest.repositories;
import edu.sv.ues.dam135.apirest.entities.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {

}
