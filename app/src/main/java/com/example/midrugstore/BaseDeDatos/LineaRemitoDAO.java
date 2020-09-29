package com.example.midrugstore.BaseDeDatos;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.example.midrugstore.Entidades.LineaRemito;

import java.util.ArrayList;
import java.util.List;

public class LineaRemitoDAO  extends DrugstoreOpenHelper{

    private SQLiteDatabase db;

    public LineaRemitoDAO(@Nullable Context context) {
        super(context);
    }

    private String insertarLineaRemito = "INSERT INTO LineaRemito (idLineaRemito,cantidadRemito,cantidadRecibida,idRemito,idProducto) VALUES (?,?,?,?,?)";
    private String eliminarLineaRemito = "DELETE FROM LineaRemito WHERE idLineaRemito = ?";
    private String actualizarLineaRemito = "UPDATE LineaRemito SET cantidadRemito = ?, cantidadRecibida = ?, idRemito = ?, idProducto = ? WHERE idLineaRemito = ?";
    private String obtenerLineaRemitoPorId = "SELECT * FROM LineaRemito WHERE idLineaRemito = ?";
    private String obtenerLineasRemitosPorRemito = "SELECT * FROM LineaRemito  WHERE idRemito = ?";
    private String obtenerLineasRemitosPorProducto = "SELECT * FROM LineaRemito  WHERE idProducto = ?";
    private String obtenerLineasRemitos = "SELECT * FROM LineaRemito";

    public void crearLineaRemito(LineaRemito lineaRemito)
    {
        db = this.getWritableDatabase();
        String[] params = new String[] {String.valueOf(lineaRemito.getIdLineaRemito()),String.valueOf(lineaRemito.getCantidadRemito()),String.valueOf(lineaRemito.getCantidadRecibida()),String.valueOf(lineaRemito.getIdRemito()),String.valueOf(lineaRemito.getIdProducto())};
        db.execSQL(insertarLineaRemito,params);
        db.close();
    }

    public void eliminarLineaRemito(int idLineaRemito)
    {
        db = this.getWritableDatabase();
        String[] params = new String[]{String.valueOf(idLineaRemito)};
        db.execSQL(eliminarLineaRemito,params);
        db.close();
    }

    public void modificarLineaPedidoCompra(LineaRemito lineaRemito)
    {
        db = this.getWritableDatabase();
        String[] params = new String[]{String.valueOf(lineaRemito.getCantidadRemito()),String.valueOf(lineaRemito.getCantidadRecibida()),String.valueOf(lineaRemito.getIdRemito()),String.valueOf(lineaRemito.getIdProducto()),String.valueOf(lineaRemito.getIdLineaRemito())};
        db.execSQL(actualizarLineaRemito,params);
        db.close();
    }

    @SuppressLint("Recycle")
    public LineaRemito obtenerLineaRemitoPorId(int idLR)
    {
        db = this.getReadableDatabase();
        LineaRemito lineaRemito = null;
        String[] params = new String[]{String.valueOf(idLR)};

        Cursor c = db.rawQuery(obtenerLineaRemitoPorId,params);
        if (c.moveToFirst())
        {
            int idLineaRemito = c.getInt(c.getColumnIndex("idLineaRemito"));
            int cantidadRemito = c.getInt(c.getColumnIndex("cantidadRemito"));
            int cantidadRecibida = c.getInt(c.getColumnIndex("cantidadRecibida"));
            int idRemito = c.getInt(c.getColumnIndex("idRemito"));
            int idProducto = c.getInt(c.getColumnIndex("idProducto"));
            lineaRemito = new LineaRemito(idLineaRemito,cantidadRemito,cantidadRecibida,idRemito,idProducto);
        }
        db.close();
        return lineaRemito;
    }

    @SuppressLint("Recycle")
    public List<LineaRemito> obtenerTodasLasLineasRemitosPorRemito(int idR)
    {
        db = this.getReadableDatabase();
        List<LineaRemito> lineasRemitos = new ArrayList<>();
        String[] params = new String[]{String.valueOf(idR)};

        Cursor c = db.rawQuery(obtenerLineasRemitosPorRemito,params);
        if (c.moveToFirst())
        {
            do {
                int idLineaRemito = c.getInt(c.getColumnIndex("idLineaRemito"));
                int cantidadRemito = c.getInt(c.getColumnIndex("cantidadRemito"));
                int cantidadRecibida = c.getInt(c.getColumnIndex("cantidadRecibida"));
                int idRemito = c.getInt(c.getColumnIndex("idRemito"));
                int idProducto = c.getInt(c.getColumnIndex("idProducto"));
                LineaRemito lineaRemito = new LineaRemito(idLineaRemito,cantidadRemito,cantidadRecibida,idRemito,idProducto);
                lineasRemitos.add(lineaRemito);
            }while (c.moveToNext());
        }
        db.close();
        return lineasRemitos;
    }

    @SuppressLint("Recycle")
    public List<LineaRemito> obtenerTodasLasLineasRemitosPorProducto(int idP)
    {
        db = this.getReadableDatabase();
        List<LineaRemito> lineasRemitos = new ArrayList<>();
        String[] params = new String[]{String.valueOf(idP)};

        Cursor c = db.rawQuery(obtenerLineasRemitosPorProducto,params);
        if (c.moveToFirst())
        {
            do {
                int idLineaRemito = c.getInt(c.getColumnIndex("idLineaRemito"));
                int cantidadRemito = c.getInt(c.getColumnIndex("cantidadRemito"));
                int cantidadRecibida = c.getInt(c.getColumnIndex("cantidadRecibida"));
                int idRemito = c.getInt(c.getColumnIndex("idRemito"));
                int idProducto = c.getInt(c.getColumnIndex("idProducto"));
                LineaRemito lineaRemito = new LineaRemito(idLineaRemito,cantidadRemito,cantidadRecibida,idRemito,idProducto);
                lineasRemitos.add(lineaRemito);
            }while (c.moveToNext());
        }
        db.close();
        return lineasRemitos;
    }

    @SuppressLint("Recycle")
    public List<LineaRemito> obtenerTodasLasLineasRemitos()
    {
        db = this.getReadableDatabase();
        List<LineaRemito> lineasRemitos = new ArrayList<>();

        Cursor c = db.rawQuery(obtenerLineasRemitos,null);
        if (c.moveToFirst())
        {
            do {
                int idLineaRemito = c.getInt(c.getColumnIndex("idLineaRemito"));
                int cantidadRemito = c.getInt(c.getColumnIndex("cantidadRemito"));
                int cantidadRecibida = c.getInt(c.getColumnIndex("cantidadRecibida"));
                int idRemito = c.getInt(c.getColumnIndex("idRemito"));
                int idProducto = c.getInt(c.getColumnIndex("idProducto"));
                LineaRemito lineaRemito = new LineaRemito(idLineaRemito,cantidadRemito,cantidadRecibida,idRemito,idProducto);
                lineasRemitos.add(lineaRemito);
            }while (c.moveToNext());
        }
        db.close();
        return lineasRemitos;
    }
}
