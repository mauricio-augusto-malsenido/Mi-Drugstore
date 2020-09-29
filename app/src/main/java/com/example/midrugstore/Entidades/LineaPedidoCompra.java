package com.example.midrugstore.Entidades;

public class LineaPedidoCompra {
    private int idLineaPedidoCompra;
    private int cantidadPedida;
    private int cantidadRecibida;
    private float subtotal;
    private int idPedidoCompra;
    private int idProducto;

    public LineaPedidoCompra(int idLineaPedidoCompra, int cantidadPedida, int cantidadRecibida, float subtotal, int idPedidoCompra, int idProducto) {
        this.idLineaPedidoCompra = idLineaPedidoCompra;
        this.cantidadPedida = cantidadPedida;
        this.cantidadRecibida = cantidadRecibida;
        this.subtotal = subtotal;
        this.idPedidoCompra = idPedidoCompra;
        this.idProducto = idProducto;
    }

    public int getIdLineaPedidoCompra() {
        return idLineaPedidoCompra;
    }

    public void setIdLineaPedidoCompra(int idLineaPedidoCompra) {
        this.idLineaPedidoCompra = idLineaPedidoCompra;
    }

    public int getCantidadPedida() {
        return cantidadPedida;
    }

    public void setCantidadPedida(int cantidadPedida) {
        this.cantidadPedida = cantidadPedida;
    }

    public int getCantidadRecibida() {
        return cantidadRecibida;
    }

    public void setCantidadRecibida(int cantidadRecibida) {
        this.cantidadRecibida = cantidadRecibida;
    }

    public float getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(float subtotal) {
        this.subtotal = subtotal;
    }

    public int getIdPedidoCompra() {
        return idPedidoCompra;
    }

    public void setIdPedidoCompra(int idPedidoCompra) {
        this.idPedidoCompra = idPedidoCompra;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }
}
