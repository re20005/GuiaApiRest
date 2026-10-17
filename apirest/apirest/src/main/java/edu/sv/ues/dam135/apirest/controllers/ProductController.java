package edu.sv.ues.dam135.apirest.controllers;

import edu.sv.ues.dam135.apirest.dtos.ProductsDTO;
import edu.sv.ues.dam135.apirest.services.ProductServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/products")
public class ProductController {
    final private ProductServices productServices;
    private ProductController(ProductServices productServices) {
        this.productServices = productServices;
    }
    @GetMapping
    public ResponseEntity<List<ProductsDTO>> getAllItems() {
        try {
            List<ProductsDTO> items = productServices.getAllProducts();
            if (items.isEmpty()) {
                return ResponseEntity.status(204).build();
            } else {
                return ResponseEntity.ok(items);
            }
        } catch (Exception e) {
            log.error("{}", e);
        }
        return null;
    }

    @GetMapping("/{code}")
    public ResponseEntity<ProductsDTO> getProductByCode(
            @PathVariable Integer code) {

        Optional<ProductsDTO> producto =
                productServices.getProductByCode(code);

        if (producto.isPresent()) {
            return ResponseEntity.ok(producto.get());
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<ProductsDTO> createProduct(
            @RequestBody ProductsDTO product) {
        ProductsDTO created = productServices.createProduct(product);
        return ResponseEntity.status(201).body(created);
    }

    @PutMapping("/{code}")
    public ResponseEntity<ProductsDTO> updateProduct(
            @PathVariable Integer code,
            @RequestBody ProductsDTO product) {

        Optional<ProductsDTO> actualizado =
                productServices.updateProduct(code, product);

        if (actualizado.isPresent()) {
            return ResponseEntity.ok(actualizado.get());
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer code) {
        boolean eliminado = productServices.deleteProduct(code);

        if (!eliminado) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}


