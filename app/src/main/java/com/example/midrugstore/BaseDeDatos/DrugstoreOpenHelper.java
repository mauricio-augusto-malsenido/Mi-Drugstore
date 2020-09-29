package com.example.midrugstore.BaseDeDatos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DrugstoreOpenHelper extends SQLiteOpenHelper {

    private static String nombreBD = "Drugstore";
    private static int versionBD = 1;

    // Tabla Proveedor
    private String crearTablaProveedor = "CREATE TABLE Proveedor (idProveedor PRIMARY KEY, cuit TEXT, nombre TEXT, direccion TEXT, telefono TEXT, estado TEXT)";
    private String eliminarTablaProveedor = "DROP TABLE IF EXISTS Proveedor";

    // Tabla Producto
    private String crearTablaProducto = "CREATE TABLE Producto (idProducto PRIMARY KEY, categoria TEXT, descripcion TEXT, precioVenta REAL, costoCompra REAL, stock INTEGER, estado TEXT, idProveedor INTEGER, FOREIGN KEY(idProveedor) REFERENCES Proveedor(idProveedor))";
    private String eliminarTablaProducto = "DROP TABLE IF EXISTS Producto";

    // Tabla Venta
    private String crearTablaVenta = "CREATE TABLE Venta (idVenta PRIMARY KEY, fechaVenta TEXT, horaVenta TEXT, estado TEXT, total REAL)";
    private String eliminarTablaVenta = "DROP TABLE IF EXISTS Venta";

    // Tabla LineaVenta
    private String crearTablaLineaVenta = "CREATE TABLE LineaVenta (idLineaVenta PRIMARY KEY, cantidad INTEGER, subtotal REAL, idVenta INTEGER, idProducto INTEGER, FOREIGN KEY(idVenta) REFERENCES Venta(idVenta), FOREIGN KEY(idProducto) REFERENCES Producto(idProducto))";
    private String eliminarTablaLineaVenta = "DROP TABLE IF EXISTS LineaVenta";

    // Tabla PedidoCompra
    private String crearTablaPedidoCompra = "CREATE TABLE PedidoCompra (idPedidoCompra PRIMARY KEY, fechaPedido TEXT, horaPedido TEXT, estado TEXT, total REAL, idProveedor INTEGER, FOREIGN KEY(idProveedor) REFERENCES Proveedor(idProveedor))";
    private String eliminarTablaPedidoCompra = "DROP TABLE IF EXISTS PedidoCompra";

    // Tabla LineaPedidoCompra
    private String crearTablaLineaPedidoCompra = "CREATE TABLE LineaPedidoCompra (idLineaPedidoCompra PRIMARY KEY, cantidadPedida INTEGER, cantidadRecibida INTEGER, subtotal REAL, idPedidoCompra INTEGER, idProducto INTEGER, FOREIGN KEY(idPedidoCompra) REFERENCES PedidoCompra(idPedidoCompra), FOREIGN KEY(idProducto) REFERENCES Producto(idProducto))";
    private String eliminarTablaLineaPedidoCompra = "DROP TABLE IF EXISTS LineaPedidoCompra";

    // Tabla Remito
    private String crearTablaRemito = "CREATE TABLE Remito (idRemito PRIMARY KEY, nroRemito TEXT, fechaRemito TEXT, fechaRecepcion TEXT, idPedidoCompra INTEGER, idProveedor INTEGER, FOREIGN KEY(idPedidoCompra) REFERENCES PedidoCompra(idPedidoCompra), FOREIGN KEY(idProveedor) REFERENCES Proveedor(idProveedor))";
    private String eliminarTablaRemito = "DROP TABLE IF EXISTS Remito";

    // Tabla LineaRemito
    private String crearTablaLineaRemito = "CREATE TABLE LineaRemito (idLineaRemito PRIMARY KEY, cantidadRemito INTEGER, cantidadRecibida INTEGER, idRemito INTEGER, idProducto INTEGER, FOREIGN KEY(idRemito) REFERENCES Remito(idRemito), FOREIGN KEY(idProducto) REFERENCES Producto(idProducto))";
    private String eliminarTablaLineaRemito = "DROP TABLE IF EXISTS LineaRemito";

    DrugstoreOpenHelper(@Nullable Context context) {
        super(context, nombreBD, null, versionBD);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(crearTablaProveedor);
        db.execSQL(crearTablaProducto);
        db.execSQL(crearTablaVenta);
        db.execSQL(crearTablaLineaVenta);
        db.execSQL(crearTablaPedidoCompra);
        db.execSQL(crearTablaLineaPedidoCompra);
        db.execSQL(crearTablaRemito);
        db.execSQL(crearTablaLineaRemito);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(eliminarTablaLineaRemito);
        db.execSQL(eliminarTablaRemito);
        db.execSQL(eliminarTablaLineaPedidoCompra);
        db.execSQL(eliminarTablaPedidoCompra);
        db.execSQL(eliminarTablaLineaVenta);
        db.execSQL(eliminarTablaVenta);
        db.execSQL(eliminarTablaProducto);
        db.execSQL(eliminarTablaProveedor);

        db.execSQL(crearTablaProveedor);
        db.execSQL(crearTablaProducto);
        db.execSQL(crearTablaVenta);
        db.execSQL(crearTablaLineaVenta);
        db.execSQL(crearTablaPedidoCompra);
        db.execSQL(crearTablaLineaPedidoCompra);
        db.execSQL(crearTablaRemito);
        db.execSQL(crearTablaLineaRemito);
    }
}
