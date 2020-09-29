package com.example.midrugstore.Pantallas.Ventas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.midrugstore.Adaptadores.AdaptadorLineasVenta;
import com.example.midrugstore.BaseDeDatos.LineaVentaDAO;
import com.example.midrugstore.BaseDeDatos.ProductoDAO;
import com.example.midrugstore.BaseDeDatos.VentaDAO;
import com.example.midrugstore.Entidades.LineaVenta;
import com.example.midrugstore.Entidades.Producto;
import com.example.midrugstore.Entidades.Venta;
import com.example.midrugstore.R;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PantallaDetalleVenta extends AppCompatActivity {

    TextView txtIdDV, txtFechaVentaDV, txtHoraVentaDV, txtEstadoDV, txtTotalVentaDV;
    ListView listaLineasVentasDV;

    FragmentManager manager;
    VentaDAO ventaDAO;
    LineaVentaDAO lineaVentaDAO;
    ProductoDAO productoDAO;
    Venta venta;
    List<LineaVenta> lineasVentas;
    DecimalFormat formatoDecimal;
    DateFormat formatoFecha;
    DateFormat formatoFechaSQL;

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_detalle_venta);

        txtIdDV = findViewById(R.id.txtIdDV);
        txtFechaVentaDV = findViewById(R.id.txtFechaVentaDV);
        txtHoraVentaDV = findViewById(R.id.txtHoraVentaDV);
        txtTotalVentaDV = findViewById(R.id.txtTotalVentaDV);
        listaLineasVentasDV = findViewById(R.id.listaLineasVentasDV);
        txtEstadoDV = findViewById(R.id.txtEstadoDV);

        manager = getSupportFragmentManager();

        ventaDAO = new VentaDAO(getApplicationContext());
        lineaVentaDAO = new LineaVentaDAO(getApplicationContext());
        productoDAO = new ProductoDAO(getApplicationContext());

        formatoDecimal = new DecimalFormat("0.00");
        formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        formatoFechaSQL = new SimpleDateFormat("yyyy-MM-dd");

        venta = (Venta) getIntent().getExtras().getSerializable("ventaSeleccionada");
        lineasVentas = lineaVentaDAO.obtenerTodasLasLineasVentasPorVenta(venta.getIdVenta());

        Date fecha = null;
        try {
            fecha = formatoFechaSQL.parse(venta.getFechaVenta());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        txtIdDV.setText(String.valueOf(venta.getIdVenta()));
        if (fecha != null) txtFechaVentaDV.setText(formatoFecha.format(fecha));
        txtHoraVentaDV.setText(venta.getHoraVenta());
        txtEstadoDV.setText(venta.getEstado());
        txtTotalVentaDV.setText(formatoDecimal.format(venta.getTotal()));

        AdaptadorLineasVenta adaptadorLineasVenta = new AdaptadorLineasVenta(getApplicationContext(),lineasVentas);
        listaLineasVentasDV.setAdapter(adaptadorLineasVenta);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detalle_registro,menu);
        menu.getItem(1).setVisible(false);
        if (venta.getEstado().contentEquals("No Anulada")) menu.getItem(2).setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.opAtras) finish();
        if (item.getItemId() == R.id.opAnular) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("¿Desea anular esta venta?\n\nAclaración: Esta acción es irreversible.")
                    .setTitle("Confirmación")
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ventaDAO.modificarEstadoVenta("Anulada", venta.getIdVenta());

                            List<LineaVenta> lineasVentas = lineaVentaDAO.obtenerTodasLasLineasVentasPorVenta(venta.getIdVenta());
                            for (int i=0;i<lineasVentas.size();i++)
                            {
                                Producto producto = productoDAO.obtenerProductoPorId(lineasVentas.get(i).getIdProducto());
                                int nuevoStock = producto.getStock() + lineasVentas.get(i).getCantidad();
                                producto.setStock(nuevoStock);
                                productoDAO.modificarProducto(producto);
                            }

                            Toast msj = Toast.makeText(getApplicationContext(), "Venta Anulada", Toast.LENGTH_SHORT);
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
        return super.onOptionsItemSelected(item);
    }
}
