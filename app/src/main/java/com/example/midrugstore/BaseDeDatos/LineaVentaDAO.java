package com.example.midrugstore.BaseDeDatos;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.example.midrugstore.Entidades.LineaVenta;

import java.util.ArrayList;
import java.util.List;

public class LineaVentaDAO extends DrugstoreOpenHelper {

    private SQLiteDatabase db;

    public LineaVentaDAO(@Nullable Context context) {
        super(context);
    }

    private String insertarLineaVenta = "INSERT INTO LineaVenta (idLineaVenta,cantidad,subtotal,idVenta,idProducto) VALUES (?,?,?,?,?)";
    private String obtenerLineasVentasPorVenta = "SELECT * FROM LineaVenta  WHERE idVenta = ?";
    private String obtenerLineasVentasPorProducto = "SELECT * FROM LineaVenta  WHERE idProducto = ?";
    private String obtenerLineasVentas = "SELECT * FROM LineaVenta";

    public void crearLineaVenta(LineaVenta lineaVenta)
    {
        db = this.getWritableDatabase();
        String[] params = new String[] {String.valueOf(lineaVenta.getIdLineaVenta()),String.valueOf(lineaVenta.getCantidad()),String.valueOf(lineaVenta.getSubtotal()),String.valueOf(lineaVenta.getIdVenta()),String.valueOf(lineaVenta.getIdProducto())};
        db.execSQL(insertarLineaVenta,params);
        db.close();
    }

    @SuppressLint("Recycle")
    public List<LineaVenta> obtenerTodasLasLineasVentasPorVenta(int idV)
    {
        db = this.getReadableDatabase();
        List<LineaVenta> lineasVentas = new ArrayList<>();
        String[] params = new String[]{String.valueOf(idV)};

        Cursor c = db.rawQuery(obtenerLineasVentasPorVenta,params);
        if (c.moveToFirst())
        {
            do {
                int idLineaVenta = c.getInt(c.getColumnIndex("idLineaVenta"));
                int cantidad = c.getInt(c.getColumnIndex("cantidad"));
                float subtotal = c.getFloat(c.getColumnIndex("subtotal"));
                int idVenta = c.getInt(c.getColumnIndex("idVenta"));
                int idProducto = c.getInt(c.getColumnIndex("idProducto"));
                LineaVenta lineaVenta = new LineaVenta(idLineaVenta,cantidad,subtotal,idVenta,idProducto);
                lineasVentas.add(lineaVenta);
            }while (c.moveToNext());
        }
        db.close();
        return lineasVentas;
    }

    @SuppressLint("Recycle")
    public List<LineaVenta> obtenerTodasLasLineasVentasPorProducto(int idP)
    {
        db = this.getReadableDatabase();
        List<LineaVenta> lineasVentas = new ArrayList<>();
        String[] params = new String[]{String.valueOf(idP)};

        Cursor c = db.rawQuery(obtenerLineasVentasPorProducto,params);
        if (c.moveToFirst())
        {
            do {
                int idLineaVenta = c.getInt(c.getColumnIndex("idLineaVenta"));
                int cantidad = c.getInt(c.getColumnIndex("cantidad"));
                float subtotal = c.getFloat(c.getColumnIndex("subtotal"));
                int idVenta = c.getInt(c.getColumnIndex("idVenta"));
                int idProducto = c.getInt(c.getColumnIndex("idProducto"));
                LineaVenta lineaVenta = new LineaVenta(idLineaVenta,cantidad,subtotal,idVenta,idProducto);
                lineasVentas.add(lineaVenta);
            }while (c.moveToNext());
        }
        db.close();
        return lineasVentas;
    }

    @SuppressLint("Recycle")
    public List<LineaVenta> obtenerTodasLasLineasVentas()
    {
        db = this.getReadableDatabase();
        List<LineaVenta> lineasVentas = new ArrayList<>();

        Cursor c = db.rawQuery(obtenerLineasVentas,null);
        if (c.moveToFirst())
        {
            do {
                int idLineaVenta = c.getInt(c.getColumnIndex("idLineaVenta"));
                int cantidad = c.getInt(c.getColumnIndex("cantidad"));
                float subtotal = c.getFloat(c.getColumnIndex("subtotal"));
                int idVenta = c.getInt(c.getColumnIndex("idVenta"));
                int idProducto = c.getInt(c.getColumnIndex("idProducto"));
                LineaVenta lineaVenta = new LineaVenta(idLineaVenta,cantidad,subtotal,idVenta,idProducto);
                lineasVentas.add(lineaVenta);
            }while (c.moveToNext());
        }
        db.close();
        return lineasVentas;
    }
}
