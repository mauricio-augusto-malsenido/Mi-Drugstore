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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.midrugstore.Adaptadores.AdaptadorProductos;
import com.example.midrugstore.BaseDeDatos.ProductoDAO;
import com.example.midrugstore.BaseDeDatos.ProveedorDAO;
import com.example.midrugstore.Entidades.Producto;
import com.example.midrugstore.Entidades.Proveedor;
import com.example.midrugstore.R;

import java.util.ArrayList;
import java.util.List;

public class PantallaSeleccionarProductoVenta extends AppCompatActivity {

    ListView listaProductos;

    List<Producto> productos;
    List<Producto> productosFiltradosPorEstado;
    List<Producto> productosFiltradosPorCategoria;
    List<Producto> productosFiltradosPorProveedor;
    List<Proveedor> proveedores;
    Producto productoSeleccionado;

    ProveedorDAO proveedorDAO;
    ProductoDAO productoDAO;

    Spinner filtroCategoria;
    Spinner filtroProveedor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_seleccionar_producto_venta);

        listaProductos = findViewById(R.id.listaProductosVenta);
        filtroCategoria = findViewById(R.id.filtroCategoriaSProd);
        filtroProveedor = findViewById(R.id.filtroProveedorSProd);

        registerForContextMenu(listaProductos);

        proveedorDAO = new ProveedorDAO(getApplicationContext());
        productoDAO = new ProductoDAO(getApplicationContext());

        cargarComboProveedor();

        productos = productoDAO.obtenerTodosLosProductos();

        filtroCategoria.setOnItemSelectedListener(seleccionarCategoria);
        filtroProveedor.setOnItemSelectedListener(seleccionarProveedor);

        listaProductos.setOnItemClickListener(clickCortoEnProducto);
        listaProductos.setOnItemLongClickListener(clickLargoEnProducto);
    }

    public void cargarComboProveedor()
    {
        proveedores = proveedorDAO.obtenerTodosLosProveedores();
        ArrayList<String> datosFiltroProveedor = new ArrayList<>();
        datosFiltroProveedor.add("Todos");
        if(!proveedores.isEmpty())
        {
            for (int i=0; i<proveedores.size(); i++)
            {
                datosFiltroProveedor.add(proveedores.get(i).getNombre());
            }
        }
        ArrayAdapter<String> adaptadorFiltroProveedor = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_item,datosFiltroProveedor);
        adaptadorFiltroProveedor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filtroProveedor.setAdapter(adaptadorFiltroProveedor);
    }

    public void filtrarCategoria()
    {
        productosFiltradosPorCategoria = new ArrayList<>();
        String categoriaSeleccionada = filtroCategoria.getSelectedItem().toString();
        if (filtroCategoria.getSelectedItem().toString().contentEquals("Todas"))
        {
            productosFiltradosPorCategoria.addAll(productos);
        }
        else
        {
            productosFiltradosPorCategoria.addAll(productoDAO.obtenerTodosLosProductosPorCategoria(categoriaSeleccionada));
        }
    }

    public void filtrarProveedor()
    {
        productosFiltradosPorProveedor = new ArrayList<>();
        String proveedorSeleccionado = filtroProveedor.getSelectedItem().toString();
        if (proveedorSeleccionado.contentEquals("Todos"))
        {
            productosFiltradosPorProveedor.addAll(productosFiltradosPorCategoria);
        }
        else
        {
            Proveedor proveedor = proveedorDAO.obtenerProveedorPorNombre(proveedorSeleccionado);
            for (int i=0; i<productosFiltradosPorCategoria.size();i++)
            {
                if (productosFiltradosPorCategoria.get(i).getIdProveedor() == proveedor.getIdProveedor())
                {
                    productosFiltradosPorProveedor.add(productosFiltradosPorCategoria.get(i));
                }
            }
        }
    }

    public void filtrarEstado()
    {
        productosFiltradosPorEstado = new ArrayList<>();
        for (int i = 0; i< productosFiltradosPorProveedor.size(); i++)
        {
            if (productosFiltradosPorProveedor.get(i).getEstado().contentEquals("Habilitado"))
            {
                productosFiltradosPorEstado.add(productosFiltradosPorProveedor.get(i));
            }
        }
        AdaptadorProductos adaptador = new AdaptadorProductos(getApplicationContext(), productosFiltradosPorEstado);
        listaProductos.setAdapter(adaptador);
    }

    AdapterView.OnItemClickListener clickCortoEnProducto = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            productoSeleccionado = (Producto) parent.getItemAtPosition(position);
            Intent irPantallaNuevaVenta = getIntent();
            irPantallaNuevaVenta.putExtra("productoSeleccionado", productoSeleccionado);
            setResult(RESULT_OK,irPantallaNuevaVenta);
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

    AdapterView.OnItemSelectedListener seleccionarCategoria = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            filtrarCategoria();
            filtrarProveedor();
            filtrarEstado();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    AdapterView.OnItemSelectedListener seleccionarProveedor = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            filtrarCategoria();
            filtrarProveedor();
            filtrarEstado();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

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
