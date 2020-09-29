package com.example.midrugstore.BaseDeDatos;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.example.midrugstore.Entidades.Venta;

import java.util.ArrayList;
import java.util.List;

public class VentaDAO extends DrugstoreOpenHelper {

    private SQLiteDatabase db;

    public VentaDAO(@Nullable Context context) {
        super(context);
    }

    private String insertarVenta = "INSERT INTO Venta (idVenta,fechaVenta,horaVenta,estado,total) VALUES (?,?,?,?,?)";
    private String actualizarEstadoVenta = "UPDATE Venta SET estado = ? WHERE idVenta = ?";
    private String obtenerVentasPorFechas = "SELECT * FROM Venta WHERE fechaVenta BETWEEN ? AND ?";
    private String obtenerVentas = "SELECT * FROM Venta";

    public void crearVenta(Venta venta)
    {
        db = this.getWritableDatabase();
        String[] params = new String[] {String.valueOf(venta.getIdVenta()),venta.getFechaVenta(),venta.getHoraVenta(),venta.getEstado(),String.valueOf(venta.getTotal())};
        db.execSQL(insertarVenta,params);
        db.close();
    }

    public void modificarEstadoVenta(String estado, int idVenta)
    {
        db = this.getWritableDatabase();
        String[] params = new String[]{estado,String.valueOf(idVenta)};
        db.execSQL(actualizarEstadoVenta,params);
        db.close();
    }

    @SuppressLint("Recycle")
    public List<Venta> obtenerTodasLasVentasPorFechas(String fechaDesde, String fechaHasta)
    {
        db = this.getReadableDatabase();
        List<Venta> ventas = new ArrayList<>();
        String[] params = new String[]{fechaDesde,fechaHasta};

        Cursor c = db.rawQuery(obtenerVentasPorFechas,params);
        if (c.moveToFirst())
        {
            do {
                int idVenta = c.getInt(c.getColumnIndex("idVenta"));
                String fechaVenta = c.getString(c.getColumnIndex("fechaVenta"));
                String horaVenta = c.getString(c.getColumnIndex("horaVenta"));
                String estado = c.getString(c.getColumnIndex("estado"));
                float total = c.getFloat(c.getColumnIndex("total"));
                Venta venta = new Venta(idVenta,fechaVenta,horaVenta,estado,total);
                ventas.add(venta);
            }while (c.moveToNext());
        }
        db.close();
        return ventas;
    }

    @SuppressLint("Recycle")
    public List<Venta> obtenerTodasLasVentas()
    {
        db = this.getReadableDatabase();
        List<Venta> ventas = new ArrayList<>();

        Cursor c = db.rawQuery(obtenerVentas,null);
        if (c.moveToFirst())
        {
            do {
                int idVenta = c.getInt(c.getColumnIndex("idVenta"));
                String fechaVenta = c.getString(c.getColumnIndex("fechaVenta"));
                String horaVenta = c.getString(c.getColumnIndex("horaVenta"));
                String estado = c.getString(c.getColumnIndex("estado"));
                float total = c.getFloat(c.getColumnIndex("total"));
                Venta venta = new Venta(idVenta,fechaVenta,horaVenta,estado,total);
                ventas.add(venta);
            }while (c.moveToNext());
        }
        db.close();
        return ventas;
    }
}
