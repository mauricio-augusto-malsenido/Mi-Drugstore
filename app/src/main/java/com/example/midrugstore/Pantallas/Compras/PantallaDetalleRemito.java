package com.example.midrugstore.Pantallas.Compras;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.example.midrugstore.Adaptadores.AdaptadorLineasRemito;
import com.example.midrugstore.BaseDeDatos.LineaRemitoDAO;
import com.example.midrugstore.BaseDeDatos.ProductoDAO;
import com.example.midrugstore.BaseDeDatos.ProveedorDAO;
import com.example.midrugstore.BaseDeDatos.RemitoDAO;
import com.example.midrugstore.Entidades.LineaRemito;
import com.example.midrugstore.Entidades.Remito;
import com.example.midrugstore.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PantallaDetalleRemito extends AppCompatActivity {

    TextView txtNroRemitoDR, txtFechaRemitoDR, txtFechaRecepcionDR, txtIdPedidoDR, txtProveedorDR;
    ListView listaLineasRemitoDR;

    FragmentManager manager;

    Remito remito;
    Remito remitoModificado;
    List<LineaRemito> lineasRemito;

    RemitoDAO remitoDAO;
    LineaRemitoDAO lineaRemitoDAO;
    ProductoDAO productoDAO;
    ProveedorDAO proveedorDAO;

    DateFormat formatoFecha;
    DateFormat formatoFechaSQL;

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_detalle_remito);

        txtNroRemitoDR = findViewById(R.id.txtNroRemitoDR);
        txtFechaRemitoDR = findViewById(R.id.txtFechaRemitoDR);
        txtFechaRecepcionDR = findViewById(R.id.txtFechaRecepcionDR);
        txtIdPedidoDR = findViewById(R.id.txtIdPedidoDR);
        txtProveedorDR = findViewById(R.id.txtProveedorDR);
        listaLineasRemitoDR = findViewById(R.id.listaLineasRemitoDR);

        manager = getSupportFragmentManager();

        remitoDAO = new RemitoDAO(getApplicationContext());
        lineaRemitoDAO = new LineaRemitoDAO(getApplicationContext());
        productoDAO = new ProductoDAO(getApplicationContext());
        proveedorDAO = new ProveedorDAO(getApplicationContext());

        formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        formatoFechaSQL = new SimpleDateFormat("yyyy-MM-dd");

        remito = (Remito) getIntent().getExtras().getSerializable("remitoSeleccionado");
        lineasRemito = lineaRemitoDAO.obtenerTodasLasLineasRemitosPorRemito(remito.getIdRemito());

        Date fechaRemito = null;
        Date fechaRecepcion = null;
        try {
            fechaRemito = formatoFechaSQL.parse(remito.getFechaRemito());
            fechaRecepcion = formatoFechaSQL.parse(remito.getFechaRecepcion());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        txtNroRemitoDR.setText(String.valueOf(remito.getNroRemito()));
        if (fechaRemito != null) txtFechaRemitoDR.setText(formatoFecha.format(fechaRemito));
        if (fechaRecepcion != null) txtFechaRecepcionDR.setText(formatoFecha.format(fechaRecepcion));
        txtIdPedidoDR.setText(String.valueOf(remito.getIdPedidoCompra()));
        txtProveedorDR.setText(proveedorDAO.obtenerProveedorPorId(remito.getIdProveedor()).getNombre());

        AdaptadorLineasRemito adaptadorLineasRemito = new AdaptadorLineasRemito(getApplicationContext(),lineasRemito);
        listaLineasRemitoDR.setAdapter(adaptadorLineasRemito);
    }

    @Override
    public void onBackPressed() {
        if (remitoModificado != null) setResult(RESULT_OK);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detalle_registro,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.opAtras)
        {
            if (remitoModificado != null) setResult(RESULT_OK);
            finish();
        }
        if (item.getItemId() == R.id.opEditar) {
            Intent irPantallaModificarRemito = new Intent(getApplicationContext(),PantallaModificarRemito.class);
            irPantallaModificarRemito.putExtra("remitoSeleccionado",remito);
            irPantallaModificarRemito.putExtra("pantallaAnterior","PantallaDetalleRemito");
            startActivityForResult(irPantallaModificarRemito,100);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK)
        {
            remitoModificado = (Remito) data.getExtras().getSerializable("remitoModificado");
            lineasRemito = lineaRemitoDAO.obtenerTodasLasLineasRemitosPorRemito(remitoModificado.getIdRemito());

            Date fechaRemito = null;
            Date fechaRecepcion = null;
            try {
                fechaRemito = formatoFechaSQL.parse(remitoModificado.getFechaRemito());
                fechaRecepcion = formatoFechaSQL.parse(remitoModificado.getFechaRecepcion());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            txtNroRemitoDR.setText(String.valueOf(remitoModificado.getNroRemito()));
            if (fechaRemito != null) txtFechaRemitoDR.setText(formatoFecha.format(fechaRemito));
            if (fechaRecepcion != null) txtFechaRecepcionDR.setText(formatoFecha.format(fechaRecepcion));

            AdaptadorLineasRemito adaptadorLineasRemito = new AdaptadorLineasRemito(getApplicationContext(),lineasRemito);
            listaLineasRemitoDR.setAdapter(adaptadorLineasRemito);

            remito = remitoModificado;
        }
    }
}
