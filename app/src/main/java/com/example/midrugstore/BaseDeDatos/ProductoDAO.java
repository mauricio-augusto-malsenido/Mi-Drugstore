package com.example.midrugstore.BaseDeDatos;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.example.midrugstore.Entidades.Producto;

import java.util.ArrayList;
import java.util.List;

public class ProductoDAO extends DrugstoreOpenHelper{

    private SQLiteDatabase db;

    public ProductoDAO(@Nullable Context context) {
        super(context);
    }

    private String insertarProducto = "INSERT INTO Producto (idProducto,categoria,descripcion,precioVenta,costoCompra,stock,estado,idProveedor) VALUES (?,?,?,?,?,?,?,?)";
    private String eliminarProducto = "DELETE FROM Producto WHERE idProducto = ?";
    private String actualizarProducto = "UPDATE Producto SET categoria = ?, descripcion = ?, precioVenta = ?, costoCompra = ?, stock = ?, estado = ?, idProveedor = ? WHERE idProducto = ?";
    private String actualizarEstadoProducto = "UPDATE Producto SET estado = ? WHERE idProducto = ?";
    private String obtenerProductoPorId = "SELECT * FROM Producto WHERE idProducto = ?";
    private String obtenerProductoPorDescripcion = "SELECT * FROM Producto WHERE descripcion = ?";
    private String obtenerProductosPorCategoria = "SELECT * FROM Producto WHERE categoria = ?";
    private String obtenerProductosPorProveedor = "SELECT * FROM Producto WHERE idProveedor = ?";
    private String obtenerProductos = "SELECT * FROM Producto";

    public void crearProducto(Producto producto)
    {
        db = this.getWritableDatabase();
        String[] params = new String[] {String.valueOf(producto.getIdProducto()),producto.getCategoria(),producto.getDescripcion(),String.valueOf(producto.getPrecioVenta()),String.valueOf(producto.getCostoCompra()),String.valueOf(producto.getStock()),producto.getEstado(),String.valueOf(producto.getIdProveedor())};
        db.execSQL(insertarProducto,params);
        db.close();
    }

    public void eliminarProducto(int idProducto)
    {
        db = this.getWritableDatabase();
        String[] params = new String[]{String.valueOf(idProducto)};
        db.execSQL(eliminarProducto,params);
        db.close();
    }

    public void modificarProducto(Producto producto)
    {
        db = this.getWritableDatabase();
        String[] params = new String[]{producto.getCategoria(),producto.getDescripcion(),String.valueOf(producto.getPrecioVenta()),String.valueOf(producto.getCostoCompra()),String.valueOf(producto.getStock()),producto.getEstado(),String.valueOf(producto.getIdProveedor()),String.valueOf(producto.getIdProducto())};
        db.execSQL(actualizarProducto,params);
        db.close();
    }

    public void modificarEstadoProducto(String estado, int idProducto)
    {
        db = this.getWritableDatabase();
        String[] params = new String[]{estado,String.valueOf(idProducto)};
        db.execSQL(actualizarEstadoProducto,params);
        db.close();
    }

    @SuppressLint("Recycle")
    public Producto obtenerProductoPorId(int idP)
    {
        db = this.getReadableDatabase();
        Producto producto = null;
        String[] params = new String[]{String.valueOf(idP)};

        Cursor c = db.rawQuery(obtenerProductoPorId,params);
        if (c.moveToFirst())
        {
            int idProducto = c.getInt(c.getColumnIndex("idProducto"));
            String categoria = c.getString(c.getColumnIndex("categoria"));
            String descripcion = c.getString(c.getColumnIndex("descripcion"));
            float precioVenta = c.getFloat(c.getColumnIndex("precioVenta"));
            float costoCompra = c.getFloat(c.getColumnIndex("costoCompra"));
            int stock = c.getInt(c.getColumnIndex("stock"));
            String estado = c.getString(c.getColumnIndex("estado"));
            int idProveedor = c.getInt(c.getColumnIndex("idProveedor"));
            producto = new Producto(idProducto,categoria,descripcion,precioVenta,costoCompra,stock,estado,idProveedor);
        }
        db.close();
        return producto;
    }

    @SuppressLint("Recycle")
    public Producto obtenerProveedorPorDescripcion(String desc)
    {
        db = this.getReadableDatabase();
        Producto producto = null;
        String[] params = new String[]{desc};

        Cursor c = db.rawQuery(obtenerProductoPorDescripcion,params);
        if (c.moveToFirst())
        {
            int idProducto = c.getInt(c.getColumnIndex("idProducto"));
            String categoria = c.getString(c.getColumnIndex("categoria"));
            String descripcion = c.getString(c.getColumnIndex("descripcion"));
            float precioVenta = c.getFloat(c.getColumnIndex("precioVenta"));
            float costoCompra = c.getFloat(c.getColumnIndex("costoCompra"));
            int stock = c.getInt(c.getColumnIndex("stock"));
            String estado = c.getString(c.getColumnIndex("estado"));
            int idProveedor = c.getInt(c.getColumnIndex("idProveedor"));
            producto = new Producto(idProducto,categoria,descripcion,precioVenta,costoCompra,stock,estado,idProveedor);
        }
        db.close();
        return producto;
    }

    @SuppressLint("Recycle")
    public List<Producto> obtenerTodosLosProductosPorCategoria(String cat)
    {
        db = this.getReadableDatabase();
        List<Producto> productos = new ArrayList<>();
        String[] params = new String[]{cat};

        Cursor c = db.rawQuery(obtenerProductosPorCategoria,params);
        if (c.moveToFirst())
        {
            do {
                int idProducto = c.getInt(c.getColumnIndex("idProducto"));
                String categoria = c.getString(c.getColumnIndex("categoria"));
                String descripcion = c.getString(c.getColumnIndex("descripcion"));
                float precioVenta = c.getFloat(c.getColumnIndex("precioVenta"));
                float costoCompra = c.getFloat(c.getColumnIndex("costoCompra"));
                int stock = c.getInt(c.getColumnIndex("stock"));
                String estado = c.getString(c.getColumnIndex("estado"));
                int idProveedor = c.getInt(c.getColumnIndex("idProveedor"));
                Producto producto = new Producto(idProducto,categoria,descripcion,precioVenta,costoCompra,stock,estado,idProveedor);
                productos.add(producto);
            }while (c.moveToNext());
        }
        db.close();
        return productos;
    }

    @SuppressLint("Recycle")
    public List<Producto> obtenerTodosLosProductosPorProveedor(int idProv)
    {
        db = this.getReadableDatabase();
        List<Producto> productos = new ArrayList<>();
        String[] params = new String[]{String.valueOf(idProv)};

        Cursor c = db.rawQuery(obtenerProductosPorProveedor,params);
        if (c.moveToFirst())
        {
            do {
                int idProducto = c.getInt(c.getColumnIndex("idProducto"));
                String categoria = c.getString(c.getColumnIndex("categoria"));
                String descripcion = c.getString(c.getColumnIndex("descripcion"));
                float precioVenta = c.getFloat(c.getColumnIndex("precioVenta"));
                float costoCompra = c.getFloat(c.getColumnIndex("costoCompra"));
                int stock = c.getInt(c.getColumnIndex("stock"));
                String estado = c.getString(c.getColumnIndex("estado"));
                int idProveedor = c.getInt(c.getColumnIndex("idProveedor"));
                Producto producto = new Producto(idProducto,categoria,descripcion,precioVenta,costoCompra,stock,estado,idProveedor);
                productos.add(producto);
            }while (c.moveToNext());
        }
        db.close();
        return productos;
    }

    @SuppressLint("Recycle")
    public List<Producto> obtenerTodosLosProductos()
    {
        db = this.getReadableDatabase();
        List<Producto> productos = new ArrayList<>();

        Cursor c = db.rawQuery(obtenerProductos,null);
        if (c.moveToFirst())
        {
            do {
                int idProducto = c.getInt(c.getColumnIndex("idProducto"));
                String categoria = c.getString(c.getColumnIndex("categoria"));
                String descripcion = c.getString(c.getColumnIndex("descripcion"));
                float precioVenta = c.getFloat(c.getColumnIndex("precioVenta"));
                float costoCompra = c.getFloat(c.getColumnIndex("costoCompra"));
                int stock = c.getInt(c.getColumnIndex("stock"));
                String estado = c.getString(c.getColumnIndex("estado"));
                int idProveedor = c.getInt(c.getColumnIndex("idProveedor"));
                Producto producto = new Producto(idProducto,categoria,descripcion,precioVenta,costoCompra,stock,estado,idProveedor);
                productos.add(producto);
            }while (c.moveToNext());
        }
        db.close();
        return productos;
    }
}
