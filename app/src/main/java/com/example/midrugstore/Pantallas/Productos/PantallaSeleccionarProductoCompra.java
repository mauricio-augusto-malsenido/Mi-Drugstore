package com.example.midrugstore.Pantallas.Productos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.midrugstore.Adaptadores.AdaptadorProductos;
import com.example.midrugstore.BaseDeDatos.ProductoDAO;
import com.example.midrugstore.BaseDeDatos.ProveedorDAO;
import com.example.midrugstore.Entidades.Producto;
import com.example.midrugstore.Entidades.Proveedor;
import com.example.midrugstore.R;

import java.util.ArrayList;
import java.util.List;

public class PantallaSeleccionarProductoCompra extends AppCompatActivity {

    ListView listaProductos;

    List<Producto> productos;
    List<Producto> productosFiltradosPorEstado;
    Producto productoSeleccionado;

    ProveedorDAO proveedorDAO;
    ProductoDAO productoDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_seleccionar_producto_compra);

        listaProductos = findViewById(R.id.listaProductosCompra);
        registerForContextMenu(listaProductos);

        proveedorDAO = new ProveedorDAO(getApplicationContext());
        productoDAO = new ProductoDAO(getApplicationContext());

        String proveedorSeleccionado = getIntent().getStringExtra("proveedorSeleccionado");
        Proveedor proveedor = proveedorDAO.obtenerProveedorPorNombre(proveedorSeleccionado);

        productos = productoDAO.obtenerTodosLosProductosPorProveedor(proveedor.getIdProveedor());

        productosFiltradosPorEstado = new ArrayList<>();
        for (int i = 0; i< productos.size(); i++)
        {
            if (productos.get(i).getEstado().contentEquals("Habilitado"))
            {
                productosFiltradosPorEstado.add(productos.get(i));
            }
        }

        AdaptadorProductos adaptador = new AdaptadorProductos(getApplicationContext(), productosFiltradosPorEstado);
        listaProductos.setAdapter(adaptador);

        listaProductos.setOnItemClickListener(clickCortoEnProducto);
        listaProductos.setOnItemLongClickListener(clickLargoEnProducto);
    }

    AdapterView.OnItemClickListener clickCortoEnProducto = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            productoSeleccionado = (Producto) parent.getItemAtPosition(position);
            Intent irPantallaNuevaCompra = getIntent();
            irPantallaNuevaCompra.putExtra("productoSeleccionado", productoSeleccionado);
            setResult(RESULT_OK,irPantallaNuevaCompra);
            finish();
        }
    };

    AdapterView.OnItemLongClickListener clickLargoEnProducto = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            productoSeleccionado = (Producto) parent.getItemAtPosition(position);
            return false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pantalla_secundaria,menu);
        menu.getItem(1).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.opAtras) finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        getMenuInflater().inflate(R.menu.menu_contextual_lista_seleccion,menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.opVerDetalles)
        {
            Intent irPantallaDetalleProducto = new Intent(getApplicationContext(), PantallaDetalleProducto.class);
            irPantallaDetalleProducto.putExtra("productoSeleccionado",productoSeleccionado);
            irPantallaDetalleProducto.putExtra("pantallaAnterior","PantallaSeleccionarProducto");
            startActivity(irPantallaDetalleProducto);
        }
        return super.onContextItemSelected(item);
    }
}
