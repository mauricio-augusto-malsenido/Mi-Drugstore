package com.example.midrugstore.BaseDeDatos;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.example.midrugstore.Entidades.LineaPedidoCompra;

import java.util.ArrayList;
import java.util.List;

public class LineaPedidoCompraDAO extends DrugstoreOpenHelper {

    private SQLiteDatabase db;

    public LineaPedidoCompraDAO(@Nullable Context context) {
        super(context);
    }

    private String insertarLineaPedidoCompra = "INSERT INTO LineaPedidoCompra (idLineaPedidoCompra,cantidadPedida,cantidadRecibida,subtotal,idPedidoCompra,idProducto) VALUES (?,?,?,?,?,?)";
    private String actualizarLineaPedidoCompra = "UPDATE LineaPedidoCompra SET cantidadPedida = ?, cantidadRecibida = ?, subtotal = ?, idPedidoCompra = ?, idProducto = ? WHERE idLineaPedidoCompra = ?";
    private String obtenerLineasPedidosComprasPorPedido = "SELECT * FROM LineaPedidoCompra  WHERE idPedidoCompra = ?";
    private String obtenerLineasPedidosComprasPorProducto = "SELECT * FROM LineaPedidoCompra  WHERE idProducto = ?";
    private String obtenerLineasPedidosCompras = "SELECT * FROM LineaPedidoCompra";

    public void crearLineaPedidoCompra(LineaPedidoCompra lineaPedido)
    {
        db = this.getWritableDatabase();
        String[] params = new String[] {String.valueOf(lineaPedido.getIdLineaPedidoCompra()),String.valueOf(lineaPedido.getCantidadPedida()),String.valueOf(lineaPedido.getCantidadRecibida()),String.valueOf(lineaPedido.getSubtotal()),String.valueOf(lineaPedido.getIdPedidoCompra()),String.valueOf(lineaPedido.getIdProducto())};
        db.execSQL(insertarLineaPedidoCompra,params);
        db.close();
    }

    public void modificarLineaPedidoCompra(LineaPedidoCompra lineaPedido)
    {
        db = this.getWritableDatabase();
        String[] params = new String[]{String.valueOf(lineaPedido.getCantidadPedida()),String.valueOf(lineaPedido.getCantidadRecibida()),String.valueOf(lineaPedido.getSubtotal()),String.valueOf(lineaPedido.getIdPedidoCompra()),String.valueOf(lineaPedido.getIdProducto()),String.valueOf(lineaPedido.getIdLineaPedidoCompra())};
        db.execSQL(actualizarLineaPedidoCompra,params);
        db.close();
    }

    @SuppressLint("Recycle")
    public List<LineaPedidoCompra> obtenerTodasLasLineasPedidosComprasPorPedido(int idPC)
    {
        db = this.getReadableDatabase();
        List<LineaPedidoCompra> lineasPedidos = new ArrayList<>();
        String[] params = new String[]{String.valueOf(idPC)};

        Cursor c = db.rawQuery(obtenerLineasPedidosComprasPorPedido,params);
        if (c.moveToFirst())
        {
            do {
                int idLineaPedidoCompra = c.getInt(c.getColumnIndex("idLineaPedidoCompra"));
                int cantidadPedida = c.getInt(c.getColumnIndex("cantidadPedida"));
                int cantidadRecibida = c.getInt(c.getColumnIndex("cantidadRecibida"));
                float subtotal = c.getFloat(c.getColumnIndex("subtotal"));
                int idPedidoCompra = c.getInt(c.getColumnIndex("idPedidoCompra"));
                int idProducto = c.getInt(c.getColumnIndex("idProducto"));
                LineaPedidoCompra lineaPedido = new LineaPedidoCompra(idLineaPedidoCompra,cantidadPedida,cantidadRecibida,subtotal,idPedidoCompra,idProducto);
                lineasPedidos.add(lineaPedido);
            }while (c.moveToNext());
        }
        db.close();
        return lineasPedidos;
    }

    @SuppressLint("Recycle")
    public List<LineaPedidoCompra> obtenerTodasLasLineasPedidosComprasPorProducto(int idP)
    {
        db = this.getReadableDatabase();
        List<LineaPedidoCompra> lineasPedidos = new ArrayList<>();
        String[] params = new String[]{String.valueOf(idP)};

        Cursor c = db.rawQuery(obtenerLineasPedidosComprasPorProducto,params);
        if (c.moveToFirst())
        {
            do {
                int idLineaPedidoCompra = c.getInt(c.getColumnIndex("idLineaPedidoCompra"));
                int cantidadPedida = c.getInt(c.getColumnIndex("cantidadPedida"));
                int cantidadRecibida = c.getInt(c.getColumnIndex("cantidadRecibida"));
                float subtotal = c.getFloat(c.getColumnIndex("subtotal"));
                int idPedidoCompra = c.getInt(c.getColumnIndex("idPedidoCompra"));
                int idProducto = c.getInt(c.getColumnIndex("idProducto"));
                LineaPedidoCompra lineaPedido = new LineaPedidoCompra(idLineaPedidoCompra,cantidadPedida,cantidadRecibida,subtotal,idPedidoCompra,idProducto);
                lineasPedidos.add(lineaPedido);
            }while (c.moveToNext());
        }
        db.close();
        return lineasPedidos;
    }

    @SuppressLint("Recycle")
    public List<LineaPedidoCompra> obtenerTodasLasLineasPedidosCompras()
    {
        db = this.getReadableDatabase();
        List<LineaPedidoCompra> lineasPedidos = new ArrayList<>();

        Cursor c = db.rawQuery(obtenerLineasPedidosCompras,null);
        if (c.moveToFirst())
        {
            do {
                int idLineaPedidoCompra = c.getInt(c.getColumnIndex("idLineaPedidoCompra"));
                int cantidadPedida = c.getInt(c.getColumnIndex("cantidadPedida"));
                int cantidadRecibida = c.getInt(c.getColumnIndex("cantidadRecibida"));
                float subtotal = c.getFloat(c.getColumnIndex("subtotal"));
                int idPedidoCompra = c.getInt(c.getColumnIndex("idPedidoCompra"));
                int idProducto = c.getInt(c.getColumnIndex("idProducto"));
                LineaPedidoCompra lineaPedido = new LineaPedidoCompra(idLineaPedidoCompra,cantidadPedida,cantidadRecibida,subtotal,idPedidoCompra,idProducto);
                lineasPedidos.add(lineaPedido);
            }while (c.moveToNext());
        }
        db.close();
        return lineasPedidos;
    }
}
