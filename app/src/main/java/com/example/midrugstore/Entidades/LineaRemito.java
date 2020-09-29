package com.example.midrugstore.Entidades;

public class LineaRemito {
    private int idLineaRemito;
    private int cantidadRemito;
    private int cantidadRecibida;
    private int idRemito;
    private int idProducto;

    public LineaRemito(int idLineaRemito, int cantidadRemito, int cantidadRecibida, int idRemito, int idProducto) {
        this.idLineaRemito = idLineaRemito;
        this.cantidadRemito = cantidadRemito;
        this.cantidadRecibida = cantidadRecibida;
        this.idRemito = idRemito;
        this.idProducto = idProducto;
    }

    public int getIdLineaRemito() {
        return idLineaRemito;
    }

    public void setIdLineaRemito(int idLineaRemito) {
        this.idLineaRemito = idLineaRemito;
    }

    public int getCantidadRemito() {
        return cantidadRemito;
    }

    public void setCantidadRemito(int cantidadRemito) {
        this.cantidadRemito = cantidadRemito;
    }

    public int getCantidadRecibida() {
        return cantidadRecibida;
    }

    public void setCantidadRecibida(int cantidadRecibida) {
        this.cantidadRecibida = cantidadRecibida;
    }

    public int getIdRemito() {
        return idRemito;
    }

    public void setIdRemito(int idRemito) {
        this.idRemito = idRemito;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }
}
