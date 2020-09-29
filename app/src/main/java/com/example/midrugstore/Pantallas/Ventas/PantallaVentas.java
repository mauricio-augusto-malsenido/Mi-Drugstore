package com.example.midrugstore.Pantallas.Ventas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.midrugstore.Adaptadores.AdaptadorVentas;
import com.example.midrugstore.BaseDeDatos.LineaVentaDAO;
import com.example.midrugstore.BaseDeDatos.ProductoDAO;
import com.example.midrugstore.BaseDeDatos.VentaDAO;
import com.example.midrugstore.Entidades.LineaVenta;
import com.example.midrugstore.Entidades.Producto;
import com.example.midrugstore.Entidades.Venta;
import com.example.midrugstore.R;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PantallaVentas extends AppCompatActivity{

    ListView listaVentas;
    Spinner filtroEstadoVenta;
    Button filtroFechaDesdeVenta, filtroFechaHastaVenta;
    TextView txtTotalVentas;

    List<Venta> ventas;
    List<Venta> ventasFiltradasPorEstado;
    List<Venta> ventasFiltradasPorFechas;
    Venta ventaSeleccionada;

    VentaDAO ventaDAO;
    LineaVentaDAO lineaVentaDAO;
    ProductoDAO productoDAO;

    DateFormat formatoFecha;
    DateFormat formatoFechaSQL;

    @Override
    @SuppressLint("SimpleDateFormat")
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_ventas);

        listaVentas = findViewById(R.id.listaVentas);
        filtroEstadoVenta = findViewById(R.id.filtroEstadoVenta);
        filtroFechaDesdeVenta = findViewById(R.id.filtroFechaDesdeVenta);
        filtroFechaHastaVenta = findViewById(R.id.filtroFechaHastaVenta);
        txtTotalVentas = findViewById(R.id.txtTotalVentas);

        registerForContextMenu(listaVentas);

        ventaDAO = new VentaDAO(getApplicationContext());
        lineaVentaDAO = new LineaVentaDAO(getApplicationContext());
        productoDAO = new ProductoDAO(getApplicationContext());
        formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        formatoFechaSQL = new SimpleDateFormat("yyyy-MM-dd");

        ventas = ventaDAO.obtenerTodasLasVentas();
        if (!ventas.isEmpty())
        {
            String fechaDesde = formatoFecha.format(convertirFechaFormatoSQLDate(ventas.get(0).getFechaVenta()));
            String fechaHasta = formatoFecha.format(convertirFechaFormatoSQLDate(ventas.get(ventas.size()-1).getFechaVenta()));
            filtroFechaDesdeVenta.setText(fechaDesde);
            filtroFechaHastaVenta.setText(fechaHasta);
        }
        else
        {
            filtroFechaDesdeVenta.setText(formatoFecha.format(new Date()));
            filtroFechaHastaVenta.setText(formatoFecha.format(new Date()));
        }

        filtroEstadoVenta.setOnItemSelectedListener(seleccionarEstado);
        listaVentas.setOnItemClickListener(clickCortoEnVenta);
        listaVentas.setOnItemLongClickListener(clickLargoEnVenta);
    }

    @SuppressLint("SimpleDateFormat")
    public void seleccionarFecha(View view) throws ParseException {
        Calendar calendario = Calendar.getInstance();
        if (view.getId() == filtroFechaDesdeVenta.getId())
        {
            Date fechaDesde = formatoFecha.parse(filtroFechaDesdeVenta.getText().toString());
            if (fechaDesde != null) calendario.setTime(fechaDesde);
            int año = calendario.get(Calendar.YEAR);
            int mes = calendario.get(Calendar.MONTH);
            int dia = calendario.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePicker = new DatePickerDialog(this,seleccionarFechaDesde,año,mes,dia);
            datePicker.show();
        }
        if (view.getId() == filtroFechaHastaVenta.getId())
        {
            Date fechaHasta = formatoFecha.parse(filtroFechaHastaVenta.getText().toString());
            if (fechaHasta != null) calendario.setTime(fechaHasta);
            int año = calendario.get(Calendar.YEAR);
            int mes = calendario.get(Calendar.MONTH);
            int dia = calendario.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePicker = new DatePickerDialog(this,seleccionarFechaHasta,año,mes,dia);
            datePicker.show();
        }
    }

    public Date convertirFechaFormatoSQLDate(String fechaString)
    {
        Date fecha = null;
        try
        {
            fecha = formatoFechaSQL.parse(fechaString);
        }
        catch (ParseException e) { e.printStackTrace(); }
        return fecha;
    }

    public Date convertirFechaFormatoComunDate(String fechaString)
    {
        Date fecha = null;
        try
        {
            fecha = formatoFecha.parse(fechaString);
        }
        catch (ParseException e) { e.printStackTrace(); }
        return fecha;
    }

    public void filtrarFecha()
    {
        Date fDesde = convertirFechaFormatoComunDate(filtroFechaDesdeVenta.getText().toString());
        Date fHasta = convertirFechaFormatoComunDate(filtroFechaHastaVenta.getText().toString());
        String fechaDesde = formatoFechaSQL.format(fDesde);
        String fechaHasta = formatoFechaSQL.format(fHasta);
        ventasFiltradasPorFechas = ventaDAO.obtenerTodasLasVentasPorFechas(fechaDesde,fechaHasta);
    }

    @SuppressLint("SetTextI18n")
    public void filtrarEstado()
    {
        ventasFiltradasPorEstado = new ArrayList<>();
        String estadoSeleccionado = filtroEstadoVenta.getSelectedItem().toString();
        DecimalFormat formatoDecimal = new DecimalFormat("0.00");
        float total = 0;
        if (estadoSeleccionado.contentEquals("No Anulada"))
        {
            for (int i = 0; i< ventasFiltradasPorFechas.size(); i++)
            {
                if (ventasFiltradasPorFechas.get(i).getEstado().contentEquals("No Anulada"))
                {
                    ventasFiltradasPorEstado.add(ventasFiltradasPorFechas.get(i));
                    total = total + ventasFiltradasPorFechas.get(i).getTotal();
                }
            }
            txtTotalVentas.setText("$ " + formatoDecimal.format(total));
        }
        if (estadoSeleccionado.contentEquals("Anulada"))
        {
            for (int i = 0; i< ventasFiltradasPorFechas.size(); i++)
            {
                if (ventasFiltradasPorFechas.get(i).getEstado().contentEquals("Anulada"))
                {
                    ventasFiltradasPorEstado.add(ventasFiltradasPorFechas.get(i));
                }
            }
            txtTotalVentas.setText("");
        }
        AdaptadorVentas adaptador = new AdaptadorVentas(getApplicationContext(), ventasFiltradasPorEstado);
        listaVentas.setAdapter(adaptador);
    }

    public void actualizarListaSinReiniciarFiltros()
    {
        ventas.clear();
        ventas = ventaDAO.obtenerTodasLasVentas();
        filtrarFecha();
        filtrarEstado();
    }

    public void actualizarListaReiniciandoFiltros()
    {
        ventas.clear();
        ventas = ventaDAO.obtenerTodasLasVentas();
        String fechaDesde = formatoFecha.format(convertirFechaFormatoSQLDate(ventas.get(0).getFechaVenta()));
        String fechaHasta = formatoFecha.format(convertirFechaFormatoSQLDate(ventas.get(ventas.size()-1).getFechaVenta()));
        filtroFechaDesdeVenta.setText(fechaDesde);
        filtroFechaHastaVenta.setText(fechaHasta);
        filtroEstadoVenta.setSelection(0);
        filtrarFecha();
        filtrarEstado();
    }

    AdapterView.OnItemClickListener clickCortoEnVenta = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ventaSeleccionada = (Venta) parent.getItemAtPosition(position);
            Intent irPantallaDetalleVenta = new Intent(getApplicationContext(), PantallaDetalleVenta.class);
            irPantallaDetalleVenta.putExtra("ventaSeleccionada",ventaSeleccionada);
            startActivityForResult(irPantallaDetalleVenta,200);
        }
    };

    AdapterView.OnItemLongClickListener clickLargoEnVenta = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            ventaSeleccionada = (Venta) listaVentas.getItemAtPosition(position);
            return false;
        }
    };

    AdapterView.OnItemSelectedListener seleccionarEstado = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            filtrarFecha();
            filtrarEstado();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    DatePickerDialog.OnDateSetListener seleccionarFechaDesde = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            NumberFormat nf = new DecimalFormat("00");
            String fecha = nf.format(dayOfMonth) + "/" + nf.format((month + 1)) + "/" + year;
            filtroFechaDesdeVenta.setText(fecha);
            filtrarFecha();
            filtrarEstado();
        }
    };

    DatePickerDialog.OnDateSetListener seleccionarFechaHasta = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            NumberFormat nf = new DecimalFormat("00");
            String fecha = nf.format(dayOfMonth) + "/" + nf.format((month + 1)) + "/" + year;
            filtroFechaHastaVenta.setText(fecha);
            filtrarFecha();
            filtrarEstado();
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
            Intent irPantallaNuevaVenta = new Intent(getApplicationContext(), PantallaNuevaVenta.class);
            startActivityForResult(irPantallaNuevaVenta,100);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        getMenuInflater().inflate(R.menu.menu_contextual_lista,menu);

        menu.getItem(0).setVisible(false);
        menu.getItem(1).setVisible(false);

        if (ventaSeleccionada.getEstado().contentEquals("No Anulada")) menu.getItem(4).setVisible(true);
        else menu.getItem(4).setVisible(false);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.opAnular) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("¿Desea anular la venta seleccionada?\n\nAclaración: Esta acción es irreversible.")
                    .setTitle("Confirmación")
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ventaDAO.modificarEstadoVenta("Anulada", ventaSeleccionada.getIdVenta());

                            List<LineaVenta> lineasVentas = lineaVentaDAO.obtenerTodasLasLineasVentasPorVenta(ventaSeleccionada.getIdVenta());
                            for (int i=0;i<lineasVentas.size();i++)
                            {
                                Producto producto = productoDAO.obtenerProductoPorId(lineasVentas.get(i).getIdProducto());
                                int nuevoStock = producto.getStock() + lineasVentas.get(i).getCantidad();
                                producto.setStock(nuevoStock);
                                productoDAO.modificarProducto(producto);
                            }

                            Toast msj = Toast.makeText(getApplicationContext(), "Venta Anulada", Toast.LENGTH_SHORT);
                            msj.show();

                            actualizarListaSinReiniciarFiltros();
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
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == 100) actualizarListaReiniciandoFiltros();
        if (resultCode == RESULT_OK && requestCode == 200) actualizarListaSinReiniciarFiltros();
    }
}
