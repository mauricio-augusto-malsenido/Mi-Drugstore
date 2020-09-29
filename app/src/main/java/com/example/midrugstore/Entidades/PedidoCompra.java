package com.example.midrugstore.Entidades;

import java.io.Serializable;

public class PedidoCompra implements Serializable {
    private int idPedidoCompra;
    private String fechaPedido;
    private String horaPedido;
    private String estado;
    private float total;
    private int idProveedor;

    public PedidoCompra(int idPedidoCompra, String fechaPedido, String horaPedido, String estado, float total, int idProveedor) {
        this.idPedidoCompra = idPedidoCompra;
        this.fechaPedido = fechaPedido;
        this.horaPedido = horaPedido;
        this.estado = estado;
        this.total = total;
        this.idProveedor = idProveedor;
    }

    public int getIdPedidoCompra() {
        return idPedidoCompra;
    }

    public void setIdPedidoCompra(int idPedidoCompra) {
        this.idPedidoCompra = idPedidoCompra;
    }

    public String getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(String fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public String getHoraPedido() {
        return horaPedido;
    }

    public void setHoraPedido(String horaPedido) {
        this.horaPedido = horaPedido;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public int getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(int idProveedor) {
        this.idProveedor = idProveedor;
    }
}
