package edu.sv.ues.dam135.apirest.services;

import edu.sv.ues.dam135.apirest.dtos.ProductsDTO;

import java.util.Collections;
import java.util.List;

public interface ProductServices {
    public List<ProductsDTO> getAllProducts();
}
