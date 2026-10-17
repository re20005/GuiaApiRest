package edu.sv.ues.dam135.guiaapirest.models;

/**
 * Producto devuelto por la API. Coincide con ProductsDTO.
 */
public class Producto {
    private Integer code;
    private String name;
    private boolean status;

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public boolean isStatus() {
        return status;
    }
}
