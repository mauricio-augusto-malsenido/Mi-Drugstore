package com.example.midrugstore.BaseDeDatos;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.example.midrugstore.Entidades.PedidoCompra;

import java.util.ArrayList;
import java.util.List;

public class PedidoCompraDAO extends DrugstoreOpenHelper {

    private SQLiteDatabase db;

    public PedidoCompraDAO(@Nullable Context context) {
        super(context);
    }

    private String insertarPedidoCompra = "INSERT INTO PedidoCompra (idPedidoCompra,fechaPedido,horaPedido,estado,total,idProveedor) VALUES (?,?,?,?,?,?)";
    private String actualizarEstadoPedidoCompra = "UPDATE PedidoCompra SET estado = ? WHERE idPedidoCompra = ?";
    private String obtenerPedidoCompraPorId = "SELECT * FROM PedidoCompra WHERE idPedidoCompra = ?";
    private String obtenerPedidosComprasPorFechas = "SELECT * FROM PedidoCompra WHERE fechaPedido BETWEEN ? AND ?";
    private String obtenerPedidosComprasPorProveedor = "SELECT * FROM PedidoCompra WHERE idProveedor = ?";
    private String obtenerPedidosCompras = "SELECT * FROM PedidoCompra";

    public void crearPedidoCompra(PedidoCompra pedido)
    {
        db = this.getWritableDatabase();
        String[] params = new String[] {String.valueOf(pedido.getIdPedidoCompra()),pedido.getFechaPedido(),pedido.getHoraPedido(),pedido.getEstado(),String.valueOf(pedido.getTotal()),String.valueOf(pedido.getIdProveedor())};
        db.execSQL(insertarPedidoCompra,params);
        db.close();
    }

    public void modificarEstadoPedidoCompra(String estado, int idPedidoCompra)
    {
        db = this.getWritableDatabase();
        String[] params = new String[]{estado,String.valueOf(idPedidoCompra)};
        db.execSQL(actualizarEstadoPedidoCompra,params);
        db.close();
    }

    @SuppressLint("Recycle")
    public PedidoCompra obtenerPedidoCompraPorId(int idPC)
    {
        db = this.getReadableDatabase();
        PedidoCompra pedido = null;
        String[] params = new String[]{String.valueOf(idPC)};

        Cursor c = db.rawQuery(obtenerPedidoCompraPorId,params);
        if (c.moveToFirst())
        {
            int idPedidoCompra = c.getInt(c.getColumnIndex("idPedidoCompra"));
            String fechaPedido = c.getString(c.getColumnIndex("fechaPedido"));
            String horaPedido = c.getString(c.getColumnIndex("horaPedido"));
            String estado = c.getString(c.getColumnIndex("estado"));
            float total = c.getFloat(c.getColumnIndex("total"));
            int idProveedor = c.getInt(c.getColumnIndex("idProveedor"));
            pedido = new PedidoCompra(idPedidoCompra,fechaPedido,horaPedido,estado,total,idProveedor);
        }
        db.close();
        return pedido;
    }

    @SuppressLint("Recycle")
    public List<PedidoCompra> obtenerTodosLosPedidosComprasPorFechas(String fechaDesde, String fechaHasta)
    {
        db = this.getReadableDatabase();
        List<PedidoCompra> pedidos = new ArrayList<>();
        String[] params = new String[]{fechaDesde,fechaHasta};

        Cursor c = db.rawQuery(obtenerPedidosComprasPorFechas,params);
        if (c.moveToFirst())
        {
            do {
                int idPedidoCompra = c.getInt(c.getColumnIndex("idPedidoCompra"));
                String fechaPedido = c.getString(c.getColumnIndex("fechaPedido"));
                String horaPedido = c.getString(c.getColumnIndex("horaPedido"));
                String estado = c.getString(c.getColumnIndex("estado"));
                float total = c.getFloat(c.getColumnIndex("total"));
                int idProveedor = c.getInt(c.getColumnIndex("idProveedor"));
                PedidoCompra pedido = new PedidoCompra(idPedidoCompra,fechaPedido,horaPedido,estado,total,idProveedor);
                pedidos.add(pedido);
            }while (c.moveToNext());
        }
        db.close();
        return pedidos;
    }

    @SuppressLint("Recycle")
    public List<PedidoCompra> obtenerTodosLosPedidosComprasPorProveedor(int idP)
    {
        db = this.getReadableDatabase();
        List<PedidoCompra> pedidos = new ArrayList<>();
        String[] params = new String[]{String.valueOf(idP)};

        Cursor c = db.rawQuery(obtenerPedidosComprasPorProveedor,params);
        if (c.moveToFirst())
        {
            do {
                int idPedidoCompra = c.getInt(c.getColumnIndex("idPedidoCompra"));
                String fechaPedido = c.getString(c.getColumnIndex("fechaPedido"));
                String horaPedido = c.getString(c.getColumnIndex("horaPedido"));
                String estado = c.getString(c.getColumnIndex("estado"));
                float total = c.getFloat(c.getColumnIndex("total"));
                int idProveedor = c.getInt(c.getColumnIndex("idProveedor"));
                PedidoCompra pedido = new PedidoCompra(idPedidoCompra,fechaPedido,horaPedido,estado,total,idProveedor);
                pedidos.add(pedido);
            }while (c.moveToNext());
        }
        db.close();
        return pedidos;
    }

    @SuppressLint("Recycle")
    public List<PedidoCompra> obtenerTodosLosPedidosCompras()
    {
        db = this.getReadableDatabase();
        List<PedidoCompra> pedidos = new ArrayList<>();

        Cursor c = db.rawQuery(obtenerPedidosCompras,null);
        if (c.moveToFirst())
        {
            do {
                int idPedidoCompra = c.getInt(c.getColumnIndex("idPedidoCompra"));
                String fechaPedido = c.getString(c.getColumnIndex("fechaPedido"));
                String horaPedido = c.getString(c.getColumnIndex("horaPedido"));
                String estado = c.getString(c.getColumnIndex("estado"));
                float total = c.getFloat(c.getColumnIndex("total"));
                int idProveedor = c.getInt(c.getColumnIndex("idProveedor"));
                PedidoCompra pedido = new PedidoCompra(idPedidoCompra,fechaPedido,horaPedido,estado,total,idProveedor);
                pedidos.add(pedido);
            }while (c.moveToNext());
        }
        db.close();
        return pedidos;
    }
}
