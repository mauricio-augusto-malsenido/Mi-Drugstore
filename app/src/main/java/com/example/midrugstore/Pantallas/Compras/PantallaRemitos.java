package com.example.midrugstore.Pantallas.Compras;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.midrugstore.Adaptadores.AdaptadorRemitos;
import com.example.midrugstore.BaseDeDatos.LineaPedidoCompraDAO;
import com.example.midrugstore.BaseDeDatos.LineaRemitoDAO;
import com.example.midrugstore.BaseDeDatos.PedidoCompraDAO;
import com.example.midrugstore.BaseDeDatos.ProductoDAO;
import com.example.midrugstore.BaseDeDatos.RemitoDAO;
import com.example.midrugstore.Entidades.LineaPedidoCompra;
import com.example.midrugstore.Entidades.LineaRemito;
import com.example.midrugstore.Entidades.PedidoCompra;
import com.example.midrugstore.Entidades.Producto;
import com.example.midrugstore.Entidades.Remito;
import com.example.midrugstore.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class PantallaRemitos extends AppCompatActivity {

    ListView listaRemitos;
    Menu menuPrincipal;

    List<Remito> remitos;
    List<LineaRemito> lineasRemito;
    List<LineaPedidoCompra> lineasPedido;
    PedidoCompra pedidoSeleccionado;
    Remito remitoSeleccionado;

    RemitoDAO remitoDAO;
    LineaRemitoDAO lineaRemitoDAO;
    PedidoCompraDAO pedidoCompraDAO;
    LineaPedidoCompraDAO lineaPedidoCompraDAO;
    ProductoDAO productoDAO;

    DateFormat formatoFecha;
    DateFormat formatoFechaSQL;

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_remitos);

        listaRemitos = findViewById(R.id.listaRemitos);
        registerForContextMenu(listaRemitos);

        remitoDAO = new RemitoDAO(getApplicationContext());
        lineaRemitoDAO = new LineaRemitoDAO(getApplicationContext());
        pedidoCompraDAO = new PedidoCompraDAO(getApplicationContext());
        lineaPedidoCompraDAO = new LineaPedidoCompraDAO(getApplicationContext());
        productoDAO = new ProductoDAO(getApplicationContext());
        formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        formatoFechaSQL = new SimpleDateFormat("yyyy-MM-dd");

        pedidoSeleccionado = (PedidoCompra) getIntent().getExtras().getSerializable("pedidoSeleccionado");
        lineasPedido = lineaPedidoCompraDAO.obtenerTodasLasLineasPedidosComprasPorPedido(pedidoSeleccionado.getIdPedidoCompra());
        remitos = remitoDAO.obtenerTodosLosRemitosPorPedidoCompra(pedidoSeleccionado.getIdPedidoCompra());

        AdaptadorRemitos adaptadorRemitos = new AdaptadorRemitos(getApplicationContext(),remitos);
        listaRemitos.setAdapter(adaptadorRemitos);

        listaRemitos.setOnItemClickListener(clickCortoEnRemito);
        listaRemitos.setOnItemLongClickListener(clickLargoEnRemito);
    }

    public void actualizarLista()
    {
        remitos.clear();
        pedidoSeleccionado = pedidoCompraDAO.obtenerPedidoCompraPorId(pedidoSeleccionado.getIdPedidoCompra());
        lineasPedido = lineaPedidoCompraDAO.obtenerTodasLasLineasPedidosComprasPorPedido(pedidoSeleccionado.getIdPedidoCompra());
        remitos = remitoDAO.obtenerTodosLosRemitosPorPedidoCompra(pedidoSeleccionado.getIdPedidoCompra());

        AdaptadorRemitos adaptadorRemitos = new AdaptadorRemitos(getApplicationContext(),remitos);
        listaRemitos.setAdapter(adaptadorRemitos);

        String estado = pedidoSeleccionado.getEstado();
        if (estado.contentEquals("Completo")) menuPrincipal.getItem(1).setVisible(false);
        else menuPrincipal.getItem(1).setVisible(true);
    }

    AdapterView.OnItemClickListener clickCortoEnRemito = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            remitoSeleccionado = (Remito) parent.getItemAtPosition(position);
            Intent irPantallaDetalleRemito = new Intent(getApplicationContext(), PantallaDetalleRemito.class);
            irPantallaDetalleRemito.putExtra("remitoSeleccionado",remitoSeleccionado);
            startActivityForResult(irPantallaDetalleRemito,200);
        }
    };

    AdapterView.OnItemLongClickListener clickLargoEnRemito = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            remitoSeleccionado = (Remito) listaRemitos.getItemAtPosition(position);
            return false;
        }
    };

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pantalla_secundaria,menu);
        menuPrincipal = menu;
        String estado = pedidoSeleccionado.getEstado();
        if (estado.contentEquals("Completo")) menuPrincipal.getItem(1).setVisible(false);
        else menuPrincipal.getItem(1).setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.opAtras)
        {
            setResult(RESULT_OK);
            finish();
        }
        if (item.getItemId() == R.id.opNuevo)
        {
            Intent irPantallaNuevoRemito = new Intent(getApplicationContext(), PantallaNuevoRemito.class);
            irPantallaNuevoRemito.putExtra("pedidoSeleccionado",pedidoSeleccionado);
            startActivityForResult(irPantallaNuevoRemito,100);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        getMenuInflater().inflate(R.menu.menu_contextual_lista,menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.opModifcar:
                Intent irPantallaModificarRemito = new Intent(getApplicationContext(),PantallaModificarRemito.class);
                irPantallaModificarRemito.putExtra("remitoSeleccionado",remitoSeleccionado);
                irPantallaModificarRemito.putExtra("pantallaAnterior","PantallaRemitos");
                startActivityForResult(irPantallaModificarRemito,300);
                return true;
            case R.id.opEliminar:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("¿Desea eliminar el remito seleccionado?\n\nAclaración: Esta acción es irreversible.")
                        .setTitle("Confirmación")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                lineasRemito = lineaRemitoDAO.obtenerTodasLasLineasRemitosPorRemito(remitoSeleccionado.getIdRemito());
                                for (int i=0; i<lineasRemito.size(); i++)
                                {
                                    Producto producto = productoDAO.obtenerProductoPorId(lineasRemito.get(i).getIdProducto());
                                    int nuevoStock = producto.getStock() - lineasRemito.get(i).getCantidadRecibida();
                                    producto.setStock(nuevoStock);
                                    productoDAO.modificarProducto(producto);

                                    int cantRecibidaPedido = lineasPedido.get(i).getCantidadRecibida() - lineasRemito.get(i).getCantidadRecibida();
                                    lineasPedido.get(i).setCantidadRecibida(cantRecibidaPedido);
                                    lineaPedidoCompraDAO.modificarLineaPedidoCompra(lineasPedido.get(i));

                                    lineaRemitoDAO.eliminarLineaRemito(lineasRemito.get(i).getIdLineaRemito());
                                }
                                remitoDAO.eliminarRemito(remitoSeleccionado.getIdRemito());

                                if (remitoDAO.obtenerTodosLosRemitosPorPedidoCompra(pedidoSeleccionado.getIdPedidoCompra()).isEmpty())
                                {
                                    pedidoCompraDAO.modificarEstadoPedidoCompra("Espera",pedidoSeleccionado.getIdPedidoCompra());
                                }
                                else
                                {
                                    pedidoCompraDAO.modificarEstadoPedidoCompra("Incompleto",pedidoSeleccionado.getIdPedidoCompra());
                                }

                                Toast msj = Toast.makeText(getApplicationContext(),"Remito eliminado", Toast.LENGTH_SHORT);
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
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) actualizarLista();
    }
}
