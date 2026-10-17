package edu.sv.ues.dam135.apirest.implementations;

import edu.sv.ues.dam135.apirest.dtos.ProductsDTO;
import edu.sv.ues.dam135.apirest.entities.Producto;
import edu.sv.ues.dam135.apirest.repositories.ProductoRepository;
import edu.sv.ues.dam135.apirest.services.ProductServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @Override
    public Optional<ProductsDTO> getProductByCode(Integer code) {
        return productoRepository.findById(code)
                .map(producto -> new ProductsDTO(
                        producto.getCode(),
                        producto.getName(),
                        producto.isStatus()
                ));
    }

    @Override
    public ProductsDTO createProduct(ProductsDTO productDTO) {
        Producto producto = new Producto();
        producto.setName(productDTO.getName());
        producto.setStatus(productDTO.isStatus());

        Producto guardado = productoRepository.save(producto);

        return new ProductsDTO(
                guardado.getCode(),
                guardado.getName(),
                guardado.isStatus()
        );
    }

    @Override
    public Optional<ProductsDTO> updateProduct(
            Integer code, ProductsDTO productDTO) {

        return productoRepository.findById(code).map(producto -> {
            producto.setName(productDTO.getName());
            producto.setStatus(productDTO.isStatus());

            Producto actualizado = productoRepository.save(producto);

            return new ProductsDTO(
                    actualizado.getCode(),
                    actualizado.getName(),
                    actualizado.isStatus()
            );
        });

    }

    @Override
    public boolean deleteProduct(Integer code) {
        if (!productoRepository.existsById(code)) {
            return false;
        }

        productoRepository.deleteById(code);
        return true;
    }
}