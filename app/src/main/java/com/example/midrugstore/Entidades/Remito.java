package com.example.midrugstore.Entidades;

import java.io.Serializable;

public class Remito implements Serializable {
    private int idRemito;
    private int nroRemito;
    private String fechaRemito;
    private String fechaRecepcion;
    private int idPedidoCompra;
    private int idProveedor;

    public Remito(int idRemito, int nroRemito, String fechaRemito, String fechaRecepcion, int idPedidoCompra, int idProveedor) {
        this.idRemito = idRemito;
        this.nroRemito = nroRemito;
        this.fechaRemito = fechaRemito;
        this.fechaRecepcion = fechaRecepcion;
        this.idPedidoCompra = idPedidoCompra;
        this.idProveedor = idProveedor;
    }

    public int getIdRemito() {
        return idRemito;
    }

    public void setIdRemito(int idRemito) {
        this.idRemito = idRemito;
    }

    public int getNroRemito() {
        return nroRemito;
    }

    public void setNroRemito(int nroRemito) {
        this.nroRemito = nroRemito;
    }

    public String getFechaRemito() {
        return fechaRemito;
    }

    public void setFechaRemito(String fechaRemito) {
        this.fechaRemito = fechaRemito;
    }

    public String getFechaRecepcion() {
        return fechaRecepcion;
    }

    public void setFechaRecepcion(String fechaRecepcion) {
        this.fechaRecepcion = fechaRecepcion;
    }

    public int getIdPedidoCompra() {
        return idPedidoCompra;
    }

    public void setIdPedidoCompra(int idPedidoCompra) {
        this.idPedidoCompra = idPedidoCompra;
    }

    public int getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(int idProveedor) {
        this.idProveedor = idProveedor;
    }
}
