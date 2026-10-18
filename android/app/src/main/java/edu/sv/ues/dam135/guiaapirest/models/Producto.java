package edu.sv.ues.dam135.guiaapirest.models;

/**
 * Producto de la API. Coincide con ProductsDTO. Al crear un producto el
 * código es nulo (lo asigna la base de datos) y Gson no lo envía.
 */
public class Producto {
    private Integer code;
    private String name;
    private boolean status;

    public Producto(Integer code, String name, boolean status) {
        this.code = code;
        this.name = name;
        this.status = status;
    }

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
