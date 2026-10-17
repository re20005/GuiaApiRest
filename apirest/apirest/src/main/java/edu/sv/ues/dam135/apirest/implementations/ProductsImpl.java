package edu.sv.ues.dam135.apirest.implementations;

import edu.sv.ues.dam135.apirest.dtos.ProductsDTO;
import edu.sv.ues.dam135.apirest.entities.Producto;
import edu.sv.ues.dam135.apirest.repositories.ProductoRepository;
import edu.sv.ues.dam135.apirest.services.ProductServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
@Slf4j
@Service
public class ProductsImpl implements ProductServices {
    private final ProductoRepository productoRepository;
    private ProductsImpl(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }
    @Override
    public List<ProductsDTO> getAllProducts() {
        List<ProductsDTO> result = new ArrayList<>();
        List<Producto> items = this.productoRepository.findAll();
        for (Producto item : items) {
            result.add(new ProductsDTO(item.getCode(), item.getName(),
                    item.isStatus()));
        }
        return result;
    }
}