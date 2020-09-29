package com.example.midrugstore.Pantallas.Productos;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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
import android.widget.Toast;

import com.example.midrugstore.Adaptadores.AdaptadorProductos;
import com.example.midrugstore.BaseDeDatos.LineaPedidoCompraDAO;
import com.example.midrugstore.BaseDeDatos.LineaRemitoDAO;
import com.example.midrugstore.BaseDeDatos.LineaVentaDAO;
import com.example.midrugstore.BaseDeDatos.ProductoDAO;
import com.example.midrugstore.BaseDeDatos.ProveedorDAO;
import com.example.midrugstore.Entidades.LineaPedidoCompra;
import com.example.midrugstore.Entidades.LineaRemito;
import com.example.midrugstore.Entidades.LineaVenta;
import com.example.midrugstore.Entidades.Producto;
import com.example.midrugstore.Entidades.Proveedor;
import com.example.midrugstore.R;

import java.util.ArrayList;
import java.util.List;

public class PantallaProductos extends AppCompatActivity {

    ListView listaProductos;

    List<Producto> productos;
    List<Producto> productosFiltradosPorEstado;
    List<Producto> productosFiltradosPorCategoria;
    List<Producto> productosFiltradosPorProveedor;
    List<Proveedor> proveedores;
    Producto productoSeleccionado;

    ProveedorDAO proveedorDAO;
    ProductoDAO productoDAO;
    LineaVentaDAO lineaVentaDAO;
    LineaPedidoCompraDAO lineaPedidoCompraDAO;
    LineaRemitoDAO lineaRemitoDAO;

    Spinner filtroCategoriaProd;
    Spinner filtroProveedorProd;
    Spinner filtroEstadoProd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_productos);

        listaProductos = findViewById(R.id.listaProductos);
        filtroCategoriaProd = findViewById(R.id.filtroCategoriaProd);
        filtroProveedorProd = findViewById(R.id.filtroProveedorProd);
        filtroEstadoProd = findViewById(R.id.filtroEstadoProd);

        registerForContextMenu(listaProductos);

        proveedorDAO = new ProveedorDAO(getApplicationContext());
        productoDAO = new ProductoDAO(getApplicationContext());
        lineaVentaDAO = new LineaVentaDAO(getApplicationContext());
        lineaPedidoCompraDAO = new LineaPedidoCompraDAO(getApplicationContext());
        lineaRemitoDAO = new LineaRemitoDAO(getApplicationContext());

        cargarComboProveedor();

        productos = productoDAO.obtenerTodosLosProductos();

        filtroCategoriaProd.setOnItemSelectedListener(seleccionarCategoria);
        filtroProveedorProd.setOnItemSelectedListener(seleccionarProveedor);
        filtroEstadoProd.setOnItemSelectedListener(seleccionarEstado);

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
        filtroProveedorProd.setAdapter(adaptadorFiltroProveedor);
    }

    public void filtrarCategoria()
    {
        productosFiltradosPorCategoria = new ArrayList<>();
        String categoriaSeleccionada = filtroCategoriaProd.getSelectedItem().toString();
        if (filtroCategoriaProd.getSelectedItem().toString().contentEquals("Todas"))
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
        String proveedorSeleccionado = filtroProveedorProd.getSelectedItem().toString();
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
        String estadoSeleccionado = filtroEstadoProd.getSelectedItem().toString();
        if (estadoSeleccionado.contentEquals("Habilitado"))
        {
            for (int i = 0; i< productosFiltradosPorProveedor.size(); i++)
            {
                if (productosFiltradosPorProveedor.get(i).getEstado().contentEquals("Habilitado"))
                {
                    productosFiltradosPorEstado.add(productosFiltradosPorProveedor.get(i));
                }
            }
        }
        if (estadoSeleccionado.contentEquals("Deshabilitado"))
        {
            for (int i = 0; i< productosFiltradosPorProveedor.size(); i++)
            {
                if (productosFiltradosPorProveedor.get(i).getEstado().contentEquals("Deshabilitado"))
                {
                    productosFiltradosPorEstado.add(productosFiltradosPorProveedor.get(i));
                }
            }
        }
        AdaptadorProductos adaptador = new AdaptadorProductos(getApplicationContext(), productosFiltradosPorEstado);
        listaProductos.setAdapter(adaptador);
    }

    public void actualizarLista()
    {
        productos.clear();
        productos = productoDAO.obtenerTodosLosProductos();
        filtrarCategoria();
        filtrarProveedor();
        filtrarEstado();
    }

    public boolean sePuedeEliminarProducto(Producto producto)
    {
        List<LineaVenta> lineasVentas = lineaVentaDAO.obtenerTodasLasLineasVentasPorProducto(producto.getIdProducto());
        List<LineaPedidoCompra> lineasPedidos = lineaPedidoCompraDAO.obtenerTodasLasLineasPedidosComprasPorProducto(producto.getIdProducto());
        List<LineaRemito> lineasRemitos = lineaRemitoDAO.obtenerTodasLasLineasRemitosPorProducto(producto.getIdProducto());

        return lineasVentas.isEmpty() && lineasPedidos.isEmpty() && lineasRemitos.isEmpty();
    }

    AdapterView.OnItemClickListener clickCortoEnProducto = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            productoSeleccionado = (Producto) parent.getItemAtPosition(position);
            Intent irPantallaDetalleProducto = new Intent(getApplicationContext(),PantallaDetalleProducto.class);
            irPantallaDetalleProducto.putExtra("productoSeleccionado",productoSeleccionado);
            startActivityForResult(irPantallaDetalleProducto,200);
        }
    };

    AdapterView.OnItemLongClickListener clickLargoEnProducto = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            productoSeleccionado = (Producto) listaProductos.getItemAtPosition(position);
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

    AdapterView.OnItemSelectedListener seleccionarEstado = new AdapterView.OnItemSelectedListener() {
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.opAtras) finish();
        if (item.getItemId() == R.id.opNuevo)
        {
            Intent irPantallaNuevoProducto = new Intent(getApplicationContext(), PantallaNuevoProducto.class);
            startActivityForResult(irPantallaNuevoProducto,100);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        getMenuInflater().inflate(R.menu.menu_contextual_lista,menu);

        if (sePuedeEliminarProducto(productoSeleccionado))
        {
            menu.getItem(1).setVisible(true);
            menu.getItem(2).setVisible(false);
            menu.getItem(3).setVisible(false);
        }
        else
        {
            menu.getItem(1).setVisible(false);
            if (productoSeleccionado.getEstado().contentEquals("Habilitado"))
            {
                menu.getItem(2).setVisible(false);
                menu.getItem(3).setVisible(true);
            }
            if (productoSeleccionado.getEstado().contentEquals("Deshabilitado"))
            {
                menu.getItem(2).setVisible(true);
                menu.getItem(3).setVisible(false);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.opModifcar:
                Intent irPantallaModificarProducto = new Intent(getApplicationContext(),PantallaModificarProducto.class);
                irPantallaModificarProducto.putExtra("productoSeleccionado",productoSeleccionado);
                irPantallaModificarProducto.putExtra("pantallaAnterior","PantallaProductos");
                startActivityForResult(irPantallaModificarProducto,300);
                return true;
            case R.id.opEliminar:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("¿Desea eliminar el producto seleccionado?\n\nAclaración: Esta acción es irreversible.")
                        .setTitle("Confirmación")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                productoDAO.eliminarProducto(productoSeleccionado.getIdProducto());
                                Toast msj = Toast.makeText(getApplicationContext(),"Producto eliminado", Toast.LENGTH_SHORT);
                                msj.show();
                                actualizarLista();
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                builder.create();
                builder.show();
                return true;
            case R.id.opHabilitar:
                productoDAO.modificarEstadoProducto("Habilitado",productoSeleccionado.getIdProducto());
                Toast msj2 = Toast.makeText(getApplicationContext(),"Producto habilitado", Toast.LENGTH_SHORT);
                msj2.show();
                actualizarLista();
                return true;
            case R.id.opDeshabilitar:
                productoDAO.modificarEstadoProducto("Deshabilitado",productoSeleccionado.getIdProducto());
                Toast msj3 = Toast.makeText(getApplicationContext(),"Producto deshabilitado", Toast.LENGTH_SHORT);
                msj3.show();
                actualizarLista();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && (requestCode == 200 || requestCode == 300)) actualizarLista();
        if (resultCode == RESULT_OK && requestCode == 100)
        {
            filtroCategoriaProd.setSelection(0);
            filtroProveedorProd.setSelection(0);
            filtroEstadoProd.setSelection(0);
            actualizarLista();
        }
    }
}
