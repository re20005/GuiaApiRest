package edu.sv.ues.dam135.apirest.services;

import edu.sv.ues.dam135.apirest.dtos.ProductsDTO;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public interface ProductServices {
    public List<ProductsDTO> getAllProducts();
;    Optional<ProductsDTO> getProductByCode(Integer code);
    ProductsDTO createProduct(ProductsDTO product);
    Optional<ProductsDTO> updateProduct(Integer code, ProductsDTO product);
    boolean deleteProduct(Integer code);
}
