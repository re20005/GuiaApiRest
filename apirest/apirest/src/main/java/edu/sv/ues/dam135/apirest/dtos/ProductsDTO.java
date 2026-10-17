package edu.sv.ues.dam135.apirest.dtos;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductsDTO {
    private Integer code;
    private String name;
    private boolean status;
}
