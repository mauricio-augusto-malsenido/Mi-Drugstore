package com.example.midrugstore.BaseDeDatos;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.example.midrugstore.Entidades.Remito;

import java.util.ArrayList;
import java.util.List;

public class RemitoDAO extends DrugstoreOpenHelper {

    private SQLiteDatabase db;

    public RemitoDAO(@Nullable Context context) {
        super(context);
    }

    private String insertarRemito = "INSERT INTO Remito (idRemito,nroRemito,fechaRemito,fechaRecepcion,idPedidoCompra,idProveedor) VALUES (?,?,?,?,?,?)";
    private String eliminarRemito = "DELETE FROM Remito WHERE idRemito = ?";
    private String actualizarRemito = "UPDATE Remito SET nroRemito = ?, fechaRemito = ?, fechaRecepcion = ?, idPedidoCompra = ?, idProveedor = ? WHERE idRemito = ?";
    private String obtenerRemitosPorPedidoCompra = "SELECT * FROM Remito WHERE idPedidoCompra = ?";
    private String obtenerRemitosPorProveedor = "SELECT * FROM Remito WHERE idProveedor = ?";
    private String obtenerRemitos = "SELECT * FROM Remito";

    public void crearRemito(Remito remito)
    {
        db = this.getWritableDatabase();
        String[] params = new String[] {String.valueOf(remito.getIdRemito()),String.valueOf(remito.getNroRemito()),remito.getFechaRemito(),remito.getFechaRecepcion(),String.valueOf(remito.getIdPedidoCompra()),String.valueOf(remito.getIdProveedor())};
        db.execSQL(insertarRemito,params);
        db.close();
    }

    public void eliminarRemito(int idRemito)
    {
        db = this.getWritableDatabase();
        String[] params = new String[]{String.valueOf(idRemito)};
        db.execSQL(eliminarRemito,params);
        db.close();
    }

    public void modificarRemito(Remito remito)
    {
        db = this.getWritableDatabase();
        String[] params = new String[]{String.valueOf(remito.getNroRemito()),remito.getFechaRemito(),remito.getFechaRecepcion(),String.valueOf(remito.getIdPedidoCompra()),String.valueOf(remito.getIdProveedor()),String.valueOf(remito.getIdRemito())};
        db.execSQL(actualizarRemito,params);
        db.close();
    }

    @SuppressLint("Recycle")
    public List<Remito> obtenerTodosLosRemitosPorPedidoCompra(int idPC)
    {
        db = this.getReadableDatabase();
        List<Remito> remitos = new ArrayList<>();
        String[] params = new String[]{String.valueOf(idPC)};

        Cursor c = db.rawQuery(obtenerRemitosPorPedidoCompra,params);
        if (c.moveToFirst())
        {
            do {
                int idRemito = c.getInt(c.getColumnIndex("idRemito"));
                int nroRemito = c.getInt(c.getColumnIndex("nroRemito"));
                String fechaRemito = c.getString(c.getColumnIndex("fechaRemito"));
                String fechaRecepcion = c.getString(c.getColumnIndex("fechaRecepcion"));
                int idPedidoCompra = c.getInt(c.getColumnIndex("idPedidoCompra"));
                int idProveedor = c.getInt(c.getColumnIndex("idProveedor"));
                Remito remito = new Remito(idRemito,nroRemito,fechaRemito,fechaRecepcion,idPedidoCompra,idProveedor);
                remitos.add(remito);
            }while (c.moveToNext());
        }
        db.close();
        return remitos;
    }

    @SuppressLint("Recycle")
    public List<Remito> obtenerTodosLosRemitosPorProveedor(int idP)
    {
        db = this.getReadableDatabase();
        List<Remito> remitos = new ArrayList<>();
        String[] params = new String[]{String.valueOf(idP)};

        Cursor c = db.rawQuery(obtenerRemitosPorProveedor,params);
        if (c.moveToFirst())
        {
            do {
                int idRemito = c.getInt(c.getColumnIndex("idRemito"));
                int nroRemito = c.getInt(c.getColumnIndex("nroRemito"));
                String fechaRemito = c.getString(c.getColumnIndex("fechaRemito"));
                String fechaRecepcion = c.getString(c.getColumnIndex("fechaRecepcion"));
                int idPedidoCompra = c.getInt(c.getColumnIndex("idPedidoCompra"));
                int idProveedor = c.getInt(c.getColumnIndex("idProveedor"));
                Remito remito = new Remito(idRemito,nroRemito,fechaRemito,fechaRecepcion,idPedidoCompra,idProveedor);
                remitos.add(remito);
            }while (c.moveToNext());
        }
        db.close();
        return remitos;
    }

    @SuppressLint("Recycle")
    public List<Remito> obtenerTodosLosRemitos()
    {
        db = this.getReadableDatabase();
        List<Remito> remitos = new ArrayList<>();

        Cursor c = db.rawQuery(obtenerRemitos,null);
        if (c.moveToFirst())
        {
            do {
                int idRemito = c.getInt(c.getColumnIndex("idRemito"));
                int nroRemito = c.getInt(c.getColumnIndex("nroRemito"));
                String fechaRemito = c.getString(c.getColumnIndex("fechaRemito"));
                String fechaRecepcion = c.getString(c.getColumnIndex("fechaRecepcion"));
                int idPedidoCompra = c.getInt(c.getColumnIndex("idPedidoCompra"));
                int idProveedor = c.getInt(c.getColumnIndex("idProveedor"));
                Remito remito = new Remito(idRemito,nroRemito,fechaRemito,fechaRecepcion,idPedidoCompra,idProveedor);
                remitos.add(remito);
            }while (c.moveToNext());
        }
        db.close();
        return remitos;
    }
}
