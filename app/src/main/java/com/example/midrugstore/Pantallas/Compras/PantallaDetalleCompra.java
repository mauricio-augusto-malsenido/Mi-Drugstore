package com.example.midrugstore.Pantallas.Compras;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.midrugstore.Adaptadores.AdaptadorLineasPedidoDetalle;
import com.example.midrugstore.BaseDeDatos.LineaPedidoCompraDAO;
import com.example.midrugstore.BaseDeDatos.PedidoCompraDAO;
import com.example.midrugstore.BaseDeDatos.ProductoDAO;
import com.example.midrugstore.BaseDeDatos.ProveedorDAO;
import com.example.midrugstore.Entidades.LineaPedidoCompra;
import com.example.midrugstore.Entidades.PedidoCompra;
import com.example.midrugstore.R;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PantallaDetalleCompra extends AppCompatActivity {

    TextView txtIdDC, txtFechaPedidoDC, txtHoraPedidoDC, txtProveedorDC, txtEstadoDC, txtTotalPedidoDC;
    ListView listaLineasPedidoDC;

    FragmentManager manager;
    Menu menu;
    PedidoCompraDAO pedidoCompraDAO;
    LineaPedidoCompraDAO lineaPedidoCompraDAO;
    ProductoDAO productoDAO;
    ProveedorDAO proveedorDAO;
    PedidoCompra pedido;
    List<LineaPedidoCompra> lineasPedido;
    DecimalFormat formatoDecimal;
    DateFormat formatoFecha;
    DateFormat formatoFechaSQL;

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_detalle_compra);

        txtIdDC = findViewById(R.id.txtIdDC);
        txtFechaPedidoDC = findViewById(R.id.txtFechaPedidoDC);
        txtHoraPedidoDC = findViewById(R.id.txtHoraPedidoDC);
        txtProveedorDC = findViewById(R.id.txtProveedorDC);
        txtEstadoDC = findViewById(R.id.txtEstadoDC);
        txtTotalPedidoDC = findViewById(R.id.txtTotalPedidoDC);
        listaLineasPedidoDC = findViewById(R.id.listaLineasPedidoDC);

        manager = getSupportFragmentManager();

        pedidoCompraDAO = new PedidoCompraDAO(getApplicationContext());
        lineaPedidoCompraDAO = new LineaPedidoCompraDAO(getApplicationContext());
        productoDAO = new ProductoDAO(getApplicationContext());
        proveedorDAO = new ProveedorDAO(getApplicationContext());

        formatoDecimal = new DecimalFormat("0.00");
        formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        formatoFechaSQL = new SimpleDateFormat("yyyy-MM-dd");

        pedido = (PedidoCompra) getIntent().getExtras().getSerializable("pedidoSeleccionado");
        lineasPedido = lineaPedidoCompraDAO.obtenerTodasLasLineasPedidosComprasPorPedido(pedido.getIdPedidoCompra());

        Date fecha = null;
        try {
            fecha = formatoFechaSQL.parse(pedido.getFechaPedido());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        txtIdDC.setText(String.valueOf(pedido.getIdPedidoCompra()));
        if (fecha != null) txtFechaPedidoDC.setText(formatoFecha.format(fecha));
        txtHoraPedidoDC.setText(pedido.getHoraPedido());
        txtProveedorDC.setText(proveedorDAO.obtenerProveedorPorId(pedido.getIdProveedor()).getNombre());
        txtEstadoDC.setText(pedido.getEstado());
        txtTotalPedidoDC.setText(formatoDecimal.format(pedido.getTotal()));

        AdaptadorLineasPedidoDetalle adaptadorLineasPedido = new AdaptadorLineasPedidoDetalle(getApplicationContext(),lineasPedido);
        listaLineasPedidoDC.setAdapter(adaptadorLineasPedido);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detalle_registro,menu);
        this.menu = menu;
        menu.getItem(1).setVisible(false);
        if (pedido.getEstado().contentEquals("Espera")) menu.getItem(3).setVisible(true);
        else menu.getItem(3).setVisible(false);
        if (!pedido.getEstado().contentEquals("Cancelado")) menu.getItem(4).setVisible(true);
        else menu.getItem(4).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.opAtras)
        {
            setResult(RESULT_OK);
            finish();
        }
        if (item.getItemId() == R.id.opCancelar) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("¿Desea cancelar este pedido?\n\nAclaración: Esta acción es irreversible.")
                    .setTitle("Confirmación")
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            pedidoCompraDAO.modificarEstadoPedidoCompra("Cancelado", pedido.getIdPedidoCompra());

                            Toast msj = Toast.makeText(getApplicationContext(), "Pedido Cancelado", Toast.LENGTH_SHORT);
                            msj.show();

                            dialog.dismiss();

                            setResult(RESULT_OK);
                            finish();
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
        }
        if (item.getItemId() == R.id.opRemitos) {
            Intent irPantallaRemitos = new Intent(getApplicationContext(), PantallaRemitos.class);
            irPantallaRemitos.putExtra("pedidoSeleccionado",pedido);
            startActivityForResult(irPantallaRemitos,100);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK)
        {
            pedido = pedidoCompraDAO.obtenerPedidoCompraPorId(pedido.getIdPedidoCompra());
            txtEstadoDC.setText(pedido.getEstado());

            lineasPedido = lineaPedidoCompraDAO.obtenerTodasLasLineasPedidosComprasPorPedido(pedido.getIdPedidoCompra());
            AdaptadorLineasPedidoDetalle adaptadorLineasPedido = new AdaptadorLineasPedidoDetalle(getApplicationContext(),lineasPedido);
            listaLineasPedidoDC.setAdapter(adaptadorLineasPedido);

            if (!pedido.getEstado().contentEquals("Espera")) menu.getItem(3).setVisible(false);
            else menu.getItem(3).setVisible(true);
        }
    }
}
