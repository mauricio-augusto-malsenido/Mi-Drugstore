package com.example.midrugstore.Entidades;

import java.io.Serializable;

public class Producto implements Serializable {
    private int idProducto;
    private String categoria;
    private String descripcion;
    private float precioVenta;
    private float costoCompra;
    private int stock;
    private String estado;
    private int idProveedor;

    public Producto(int idProducto, String categoria, String descripcion, float precioVenta, float costoCompra, int stock, String estado, int idProveedor) {
        this.idProducto = idProducto;
        this.categoria = categoria;
        this.descripcion = descripcion;
        this.precioVenta = precioVenta;
        this.costoCompra = costoCompra;
        this.stock = stock;
        this.estado = estado;
        this.idProveedor = idProveedor;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public float getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(float precioVenta) {
        this.precioVenta = precioVenta;
    }

    public float getCostoCompra() {
        return costoCompra;
    }

    public void setCostoCompra(float costoCompra) {
        this.costoCompra = costoCompra;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(int idProveedor) {
        this.idProveedor = idProveedor;
    }
}
