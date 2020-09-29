package com.example.midrugstore.BaseDeDatos;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.example.midrugstore.Entidades.Proveedor;

import java.util.ArrayList;
import java.util.List;

public class ProveedorDAO extends DrugstoreOpenHelper {

    private SQLiteDatabase db;

    public ProveedorDAO(@Nullable Context context) {
        super(context);
    }

    private String insertarProveedor = "INSERT INTO Proveedor (idProveedor,cuit,nombre,direccion,telefono,estado) VALUES (?,?,?,?,?,?)";
    private String eliminarProveedor = "DELETE FROM Proveedor WHERE idProveedor = ?";
    private String actualizarProveedor = "UPDATE Proveedor SET cuit = ?, nombre = ?, direccion = ?, telefono = ?, estado = ? WHERE idProveedor = ?";
    private String actualizarEstadoProveedor = "UPDATE Proveedor SET estado = ? WHERE idProveedor = ?";
    private String obtenerProveedorPorId = "SELECT * FROM Proveedor WHERE idProveedor = ?";
    private String obtenerProveedorPorNombre = "SELECT * FROM Proveedor WHERE nombre = ?";
    private String obtenerProveedoresPorEstado = "SELECT * FROM Proveedor WHERE estado = ?";
    private String obtenerProveedores = "SELECT * FROM Proveedor";

    public void crearProveedor(Proveedor proveedor)
    {
        db = this.getWritableDatabase();
        String[] params = new String[] {String.valueOf(proveedor.getIdProveedor()),proveedor.getCuit(),proveedor.getNombre(),proveedor.getDireccion(),proveedor.getTelefono(),proveedor.getEstado()};
        db.execSQL(insertarProveedor,params);
        db.close();
    }

    public void eliminarProveedor(int idProveedor)
    {
        db = this.getWritableDatabase();
        String[] params = new String[]{String.valueOf(idProveedor)};
        db.execSQL(eliminarProveedor,params);
        db.close();
    }

    public void modificarProveedor(Proveedor proveedor)
    {
        db = this.getWritableDatabase();
        String[] params = new String[]{proveedor.getCuit(),proveedor.getNombre(),proveedor.getDireccion(),proveedor.getTelefono(),proveedor.getEstado(),String.valueOf(proveedor.getIdProveedor())};
        db.execSQL(actualizarProveedor,params);
        db.close();
    }

    public void modificarEstadoProveedor(String estado, int idProveedor)
    {
        db = this.getWritableDatabase();
        String[] params = new String[]{estado,String.valueOf(idProveedor)};
        db.execSQL(actualizarEstadoProveedor,params);
        db.close();
    }

    @SuppressLint("Recycle")
    public Proveedor obtenerProveedorPorId(int idP)
    {
        db = this.getReadableDatabase();
        Proveedor proveedor = null;
        String[] params = new String[]{String.valueOf(idP)};

        Cursor c = db.rawQuery(obtenerProveedorPorId,params);
        if (c.moveToFirst())
        {
            int idProveedor = c.getInt(c.getColumnIndex("idProveedor"));
            String cuit = c.getString(c.getColumnIndex("cuit"));
            String nombre = c.getString(c.getColumnIndex("nombre"));
            String direccion = c.getString(c.getColumnIndex("direccion"));
            String telefono = c.getString(c.getColumnIndex("telefono"));
            String estado = c.getString(c.getColumnIndex("estado"));
            proveedor = new Proveedor(idProveedor,cuit,nombre,direccion,telefono,estado);
        }
        db.close();
        return proveedor;
    }

    @SuppressLint("Recycle")
    public Proveedor obtenerProveedorPorNombre(String nombreProveedor)
    {
        db = this.getReadableDatabase();
        Proveedor proveedor = null;
        String[] params = new String[]{nombreProveedor};

        Cursor c = db.rawQuery(obtenerProveedorPorNombre,params);
        if (c.moveToFirst())
        {
            int idProveedor = c.getInt(c.getColumnIndex("idProveedor"));
            String cuit = c.getString(c.getColumnIndex("cuit"));
            String nombre = c.getString(c.getColumnIndex("nombre"));
            String direccion = c.getString(c.getColumnIndex("direccion"));
            String telefono = c.getString(c.getColumnIndex("telefono"));
            String estado = c.getString(c.getColumnIndex("estado"));
            proveedor = new Proveedor(idProveedor,cuit,nombre,direccion,telefono,estado);
        }
        db.close();
        return proveedor;
    }

    @SuppressLint("Recycle")
    public List<Proveedor> obtenerTodosLosProveedoresPorEstado(String est)
    {
        db = this.getReadableDatabase();
        List<Proveedor> proveedores = new ArrayList<>();
        String[] params = new String[]{est};

        Cursor c = db.rawQuery(obtenerProveedoresPorEstado,params);
        if (c.moveToFirst())
        {
            do {
                int idProveedor = c.getInt(c.getColumnIndex("idProveedor"));
                String cuit = c.getString(c.getColumnIndex("cuit"));
                String nombre = c.getString(c.getColumnIndex("nombre"));
                String direccion = c.getString(c.getColumnIndex("direccion"));
                String telefono = c.getString(c.getColumnIndex("telefono"));
                String estado = c.getString(c.getColumnIndex("estado"));
                Proveedor proveedor = new Proveedor(idProveedor,cuit,nombre,direccion,telefono,estado);
                proveedores.add(proveedor);
            }while (c.moveToNext());
        }
        db.close();
        return proveedores;
    }

    @SuppressLint("Recycle")
    public List<Proveedor> obtenerTodosLosProveedores()
    {
        db = this.getReadableDatabase();
        List<Proveedor> proveedores = new ArrayList<>();

        Cursor c = db.rawQuery(obtenerProveedores,null);
        if (c.moveToFirst())
        {
            do {
                int idProveedor = c.getInt(c.getColumnIndex("idProveedor"));
                String cuit = c.getString(c.getColumnIndex("cuit"));
                String nombre = c.getString(c.getColumnIndex("nombre"));
                String direccion = c.getString(c.getColumnIndex("direccion"));
                String telefono = c.getString(c.getColumnIndex("telefono"));
                String estado = c.getString(c.getColumnIndex("estado"));
                Proveedor proveedor = new Proveedor(idProveedor,cuit,nombre,direccion,telefono,estado);
                proveedores.add(proveedor);
            }while (c.moveToNext());
        }
        db.close();
        return proveedores;
    }
}
