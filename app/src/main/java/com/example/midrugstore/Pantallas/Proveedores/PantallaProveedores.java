package com.example.midrugstore.Pantallas.Proveedores;

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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.midrugstore.Adaptadores.AdaptadorProveedores;
import com.example.midrugstore.BaseDeDatos.PedidoCompraDAO;
import com.example.midrugstore.BaseDeDatos.ProductoDAO;
import com.example.midrugstore.BaseDeDatos.ProveedorDAO;
import com.example.midrugstore.BaseDeDatos.RemitoDAO;
import com.example.midrugstore.Entidades.PedidoCompra;
import com.example.midrugstore.Entidades.Producto;
import com.example.midrugstore.Entidades.Proveedor;
import com.example.midrugstore.Entidades.Remito;
import com.example.midrugstore.R;

import java.util.List;

public class PantallaProveedores extends AppCompatActivity {

    ListView listaProveedores;

    List<Proveedor> proveedores;
    Proveedor proveedorSeleccionado;

    ProveedorDAO proveedorDAO;
    ProductoDAO productoDAO;
    PedidoCompraDAO pedidoCompraDAO;
    RemitoDAO remitoDAO;

    Spinner filtroEstadoProv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_proveedores);

        listaProveedores = findViewById(R.id.listaProveedores);
        filtroEstadoProv = findViewById(R.id.filtroEstadoProv);

        registerForContextMenu(listaProveedores);

        proveedorDAO = new ProveedorDAO(getApplicationContext());
        productoDAO = new ProductoDAO(getApplicationContext());
        pedidoCompraDAO = new PedidoCompraDAO(getApplicationContext());
        remitoDAO = new RemitoDAO(getApplicationContext());

        proveedores = proveedorDAO.obtenerTodosLosProveedores();

        filtroEstadoProv.setOnItemSelectedListener(seleccionarEstado);
        listaProveedores.setOnItemClickListener(clickCortoEnProveedor);
        listaProveedores.setOnItemLongClickListener(clickLargoEnProveedor);
    }

    public void filtrarEstado()
    {
        String estadoSeleccionado = filtroEstadoProv.getSelectedItem().toString();
        if (estadoSeleccionado.contentEquals("Habilitado"))
        {
            proveedores = proveedorDAO.obtenerTodosLosProveedoresPorEstado("Habilitado");
        }
        if (estadoSeleccionado.contentEquals("Deshabilitado"))
        {
            proveedores = proveedorDAO.obtenerTodosLosProveedoresPorEstado("Deshabilitado");
        }
        AdaptadorProveedores adaptador = new AdaptadorProveedores(getApplicationContext(), proveedores);
        listaProveedores.setAdapter(adaptador);
    }

    public void actualizarLista()
    {
        proveedores.clear();
        proveedores = proveedorDAO.obtenerTodosLosProveedores();
        filtrarEstado();
    }

    public boolean sePuedeEliminarProveedor(Proveedor proveedor)
    {
        List<Producto> productos = productoDAO.obtenerTodosLosProductosPorProveedor(proveedor.getIdProveedor());
        List<PedidoCompra> pedidos = pedidoCompraDAO.obtenerTodosLosPedidosComprasPorProveedor(proveedor.getIdProveedor());
        List<Remito> remitos = remitoDAO.obtenerTodosLosRemitosPorProveedor(proveedor.getIdProveedor());

        return productos.isEmpty() && pedidos.isEmpty() && remitos.isEmpty();
    }

    AdapterView.OnItemClickListener clickCortoEnProveedor = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            proveedorSeleccionado = (Proveedor) parent.getItemAtPosition(position);
            Intent irPantallaDetalleProveedor = new Intent(getApplicationContext(), PantallaDetalleProveedor.class);
            irPantallaDetalleProveedor.putExtra("proveedorSeleccionado",proveedorSeleccionado);
            startActivityForResult(irPantallaDetalleProveedor,200);
        }
    };

    AdapterView.OnItemLongClickListener clickLargoEnProveedor = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            proveedorSeleccionado = (Proveedor) listaProveedores.getItemAtPosition(position);
            return false;
        }
    };

    AdapterView.OnItemSelectedListener seleccionarEstado = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
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
            Intent irPantallaNuevoProveedor = new Intent(getApplicationContext(), PantallaNuevoProveedor.class);
            startActivityForResult(irPantallaNuevoProveedor,100);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        getMenuInflater().inflate(R.menu.menu_contextual_lista,menu);

        if (sePuedeEliminarProveedor(proveedorSeleccionado))
        {
            menu.getItem(1).setVisible(true);
            menu.getItem(2).setVisible(false);
            menu.getItem(3).setVisible(false);
        }
        else
        {
            menu.getItem(1).setVisible(false);
            if (proveedorSeleccionado.getEstado().contentEquals("Habilitado"))
            {
                menu.getItem(2).setVisible(false);
                menu.getItem(3).setVisible(true);
            }
            if (proveedorSeleccionado.getEstado().contentEquals("Deshabilitado"))
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
                Intent irPantallaModificarProveedor = new Intent(getApplicationContext(), PantallaModificarProveedor.class);
                irPantallaModificarProveedor.putExtra("proveedorSeleccionado",proveedorSeleccionado);
                irPantallaModificarProveedor.putExtra("pantallaAnterior","PantallaProveedores");
                startActivityForResult(irPantallaModificarProveedor,300);
                return true;
            case R.id.opEliminar:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("¿Desea eliminar el proveedor seleccionado?\n\nAclaración: Esta acción es irreversible.")
                        .setTitle("Confirmación")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                proveedorDAO.eliminarProveedor(proveedorSeleccionado.getIdProveedor());
                                Toast msj = Toast.makeText(getApplicationContext(),"Proveedor eliminado", Toast.LENGTH_SHORT);
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
                proveedorDAO.modificarEstadoProveedor("Habilitado",proveedorSeleccionado.getIdProveedor());
                Toast msj2 = Toast.makeText(getApplicationContext(),"Proveedor habilitado", Toast.LENGTH_SHORT);
                msj2.show();
                actualizarLista();
                return true;
            case R.id.opDeshabilitar:
                proveedorDAO.modificarEstadoProveedor("Deshabilitado",proveedorSeleccionado.getIdProveedor());
                Toast msj3 = Toast.makeText(getApplicationContext(),"Proveedor deshabilitado", Toast.LENGTH_SHORT);
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
            filtroEstadoProv.setSelection(0);
            actualizarLista();
        }
    }
}
