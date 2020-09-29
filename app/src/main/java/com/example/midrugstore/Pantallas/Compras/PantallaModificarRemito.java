package com.example.midrugstore.Pantallas.Compras;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.midrugstore.Adaptadores.AdaptadorLineasRemito;
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
import com.example.midrugstore.Pantallas.Dialogos.DialogoCamposVacios;
import com.example.midrugstore.Pantallas.Dialogos.DialogoCancelarOperacion;
import com.example.midrugstore.Pantallas.Dialogos.DialogoCantRecibidaMayorQueRemito;
import com.example.midrugstore.Pantallas.Dialogos.DialogoCantRemitoMayorQuePedido;
import com.example.midrugstore.Pantallas.Dialogos.DialogoFechaRemitoMayorQueRecepcion;
import com.example.midrugstore.Pantallas.Dialogos.DialogoRemitoExistente;
import com.example.midrugstore.R;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PantallaModificarRemito extends AppCompatActivity {

    ListView listaLineasRemitoMR;
    EditText etNroRemitoMR, etFechaRemitoMR, etFechaRecepcionMR;
    LinearLayout contenedorDatosRemitoMR;

    FragmentManager manager;

    List<LineaRemito> lineasRemitos;
    List<LineaPedidoCompra> lineasPedidos;
    PedidoCompra pedido;
    Remito remitoSeleccionado;
    LineaRemito lineaRemitoSeleccionada;

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
        setContentView(R.layout.pantalla_modificar_remito);

        listaLineasRemitoMR = findViewById(R.id.listaLineasRemitoMR);
        etNroRemitoMR = findViewById(R.id.etNroRemitoMR);
        etFechaRemitoMR = findViewById(R.id.etFechaRemitoMR);
        etFechaRecepcionMR = findViewById(R.id.etFechaRecepcionMR);
        contenedorDatosRemitoMR = findViewById(R.id.contenedorDatosRemitoMR);

        manager = getSupportFragmentManager();

        remitoDAO = new RemitoDAO(getApplicationContext());
        lineaRemitoDAO = new LineaRemitoDAO(getApplicationContext());
        pedidoCompraDAO = new PedidoCompraDAO(getApplicationContext());
        lineaPedidoCompraDAO = new LineaPedidoCompraDAO(getApplicationContext());
        productoDAO = new ProductoDAO(getApplicationContext());

        remitoSeleccionado = (Remito) getIntent().getExtras().getSerializable("remitoSeleccionado");
        lineasRemitos = lineaRemitoDAO.obtenerTodasLasLineasRemitosPorRemito(remitoSeleccionado.getIdRemito());
        pedido = pedidoCompraDAO.obtenerPedidoCompraPorId(remitoSeleccionado.getIdPedidoCompra());
        lineasPedidos = lineaPedidoCompraDAO.obtenerTodasLasLineasPedidosComprasPorPedido(remitoSeleccionado.getIdPedidoCompra());

        formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        formatoFechaSQL = new SimpleDateFormat("yyyy-MM-dd");

        etNroRemitoMR.setText(String.valueOf(remitoSeleccionado.getNroRemito()));
        String fechaRemito = formatoFecha.format(convertirFechaFormatoSQLDate(remitoSeleccionado.getFechaRemito()));
        String fechaRecepcion = formatoFecha.format(convertirFechaFormatoSQLDate(remitoSeleccionado.getFechaRecepcion()));
        etFechaRemitoMR.setText(fechaRemito);
        etFechaRecepcionMR.setText(fechaRecepcion);

        AdaptadorLineasRemito adaptadorLineasRemito = new AdaptadorLineasRemito(getApplicationContext(),lineasRemitos);
        listaLineasRemitoMR.setAdapter(adaptadorLineasRemito);

        listaLineasRemitoMR.setOnItemClickListener(clickCortoEnLineaRemito);
    }

    @SuppressLint("SimpleDateFormat")
    public void seleccionarFecha(View view) throws ParseException {
        etNroRemitoMR.clearFocus();
        listaLineasRemitoMR.requestFocus();
        esconderTeclado();

        Calendar calendario = Calendar.getInstance();
        if (view.getId() == etFechaRemitoMR.getId())
        {
            Date fechaRemito = formatoFecha.parse(etFechaRemitoMR.getText().toString());
            if (fechaRemito != null) calendario.setTime(fechaRemito);
            int año = calendario.get(Calendar.YEAR);
            int mes = calendario.get(Calendar.MONTH);
            int dia = calendario.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePicker = new DatePickerDialog(this,seleccionarFechaRemito,año,mes,dia);
            datePicker.show();
        }
        if (view.getId() == etFechaRecepcionMR.getId())
        {
            Date fechaRecepcion = formatoFecha.parse(etFechaRecepcionMR.getText().toString());
            if (fechaRecepcion != null) calendario.setTime(fechaRecepcion);
            int año = calendario.get(Calendar.YEAR);
            int mes = calendario.get(Calendar.MONTH);
            int dia = calendario.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePicker = new DatePickerDialog(this,seleccionarFechaRecepcion,año,mes,dia);
            datePicker.show();
        }
    }

    public void esconderTeclado()
    {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
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

    public boolean verificarRemitoExistente(int nroRemito)
    {
        boolean remitoExistente = false;
        List<Remito> remitosPorProveedor = remitoDAO.obtenerTodosLosRemitosPorProveedor(pedido.getIdProveedor());
        for (int i=0; i<remitosPorProveedor.size(); i++)
        {
            if (remitosPorProveedor.get(i).getNroRemito() == nroRemito) remitoExistente = true;
        }
        return remitoExistente;
    }

    AdapterView.OnItemClickListener clickCortoEnLineaRemito = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            etNroRemitoMR.clearFocus();
            listaLineasRemitoMR.requestFocus();
            esconderTeclado();

            lineaRemitoSeleccionada = (LineaRemito) parent.getItemAtPosition(position);

            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.dialogo_modificar_linea_remito, (ViewGroup) findViewById(R.id.contenedorDialogoLineaRemito));

            TextView txtProducto = layout.findViewById(R.id.txtProducto);
            final EditText etCantidadRemito = layout.findViewById(R.id.etCantidadRemito);
            final EditText etCantidadRecibida = layout.findViewById(R.id.etCantidadRecebida);

            Producto producto = productoDAO.obtenerProductoPorId(lineaRemitoSeleccionada.getIdProducto());
            txtProducto.setText(producto.getDescripcion());
            etCantidadRemito.setText(String.valueOf(lineaRemitoSeleccionada.getCantidadRemito()));
            etCantidadRecibida.setText(String.valueOf(lineaRemitoSeleccionada.getCantidadRecibida()));

            AlertDialog.Builder builder = new AlertDialog.Builder(parent.getContext());
            builder.setView(layout);
            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (etCantidadRemito.getText().toString().isEmpty() || etCantidadRecibida.getText().toString().isEmpty())
                    {
                        DialogoCamposVacios camposVacios = new DialogoCamposVacios();
                        camposVacios.show(manager,"Error");
                    }
                    else
                    {
                        int cantidadRemito = Integer.parseInt(etCantidadRemito.getText().toString());
                        int cantidadRecibida = Integer.parseInt(etCantidadRecibida.getText().toString());

                        if (cantidadRecibida > cantidadRemito)
                        {
                            DialogoCantRecibidaMayorQueRemito cantRecibidaMayorQueRemito = new DialogoCantRecibidaMayorQueRemito();
                            cantRecibidaMayorQueRemito.show(manager,"Error");
                        }
                        else
                        {
                            for (int i=0; i<lineasRemitos.size(); i++)
                            {
                                if (lineasRemitos.get(i).getIdProducto() == lineaRemitoSeleccionada.getIdProducto())
                                {
                                    if (cantidadRemito > lineasPedidos.get(i).getCantidadPedida() - (lineasPedidos.get(i).getCantidadRecibida() - lineaRemitoSeleccionada.getCantidadRecibida()))
                                    {
                                        DialogoCantRemitoMayorQuePedido cantRemitoMayorQuePedido = new DialogoCantRemitoMayorQuePedido();
                                        cantRemitoMayorQuePedido.show(manager,"Error");
                                    }
                                    else
                                    {
                                        lineasRemitos.get(i).setCantidadRemito(cantidadRemito);
                                        lineasRemitos.get(i).setCantidadRecibida(cantidadRecibida);
                                    }
                                }
                            }
                            AdaptadorLineasRemito adaptadorLineasRemito = new AdaptadorLineasRemito(getApplicationContext(),lineasRemitos);
                            listaLineasRemitoMR.setAdapter(adaptadorLineasRemito);

                            dialog.dismiss();
                        }
                    }
                }
            })
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            builder.create();
            builder.show();
        }
    };

    DatePickerDialog.OnDateSetListener seleccionarFechaRemito = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            NumberFormat nf = new DecimalFormat("00");
            String fecha = nf.format(dayOfMonth) + "/" + nf.format((month + 1)) + "/" + year;
            etFechaRemitoMR.setText(fecha);
        }
    };

    DatePickerDialog.OnDateSetListener seleccionarFechaRecepcion = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            NumberFormat nf = new DecimalFormat("00");
            String fecha = nf.format(dayOfMonth) + "/" + nf.format((month + 1)) + "/" + year;
            etFechaRecepcionMR.setText(fecha);
        }
    };

    @Override
    public void onBackPressed() {
        DialogoCancelarOperacion cancelarOperacion = new DialogoCancelarOperacion();
        cancelarOperacion.show(manager,"Confirmación");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_nuevo_modificar_registro,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.opCancelar)
        {
            DialogoCancelarOperacion cancelarOperacion = new DialogoCancelarOperacion();
            cancelarOperacion.show(manager,"Confirmación");
        }
        if (item.getItemId() == R.id.opGuardar)
        {
            if (etNroRemitoMR.getText().toString().isEmpty() || etFechaRemitoMR.getText().toString().isEmpty() || etFechaRecepcionMR.getText().toString().isEmpty())
            {
                DialogoCamposVacios camposVacios = new DialogoCamposVacios();
                camposVacios.show(manager,"Error");
            }
            else
            {
                Date fRemito = convertirFechaFormatoComunDate(etFechaRemitoMR.getText().toString());
                Date fRecepcion = convertirFechaFormatoComunDate(etFechaRecepcionMR.getText().toString());
                if (fRemito.after(fRecepcion))
                {
                    DialogoFechaRemitoMayorQueRecepcion fechaRemitoMayorQueRecepcion = new DialogoFechaRemitoMayorQueRecepcion();
                    fechaRemitoMayorQueRecepcion.show(manager,"Error");
                }
                else
                {
                    int idRemito = remitoSeleccionado.getIdRemito();
                    int nroRemito = Integer.parseInt(etNroRemitoMR.getText().toString());
                    if (verificarRemitoExistente(nroRemito) && remitoSeleccionado.getNroRemito() != nroRemito)
                    {
                        DialogoRemitoExistente remitoExistente = new DialogoRemitoExistente();
                        remitoExistente.show(manager,"Error");
                    }
                    else
                    {
                        String fechaRemito = formatoFechaSQL.format(fRemito);
                        String fechaRecepcion = formatoFechaSQL.format(fRecepcion);
                        int idPedido = remitoSeleccionado.getIdPedidoCompra();
                        int idProveedor = remitoSeleccionado.getIdProveedor();

                        boolean pedidoCompleto = false;
                        for (int i=0;i<lineasRemitos.size();i++)
                        {
                            Producto producto = productoDAO.obtenerProductoPorId(lineasRemitos.get(i).getIdProducto());
                            int diferenciaCantidades = lineasRemitos.get(i).getCantidadRecibida() - lineaRemitoDAO.obtenerLineaRemitoPorId(lineasRemitos.get(i).getIdLineaRemito()).getCantidadRecibida();
                            int nuevoStock = producto.getStock() + diferenciaCantidades;
                            producto.setStock(nuevoStock);
                            productoDAO.modificarProducto(producto);

                            int cantRecibidaPedido = lineasPedidos.get(i).getCantidadRecibida() + diferenciaCantidades;
                            lineasPedidos.get(i).setCantidadRecibida(cantRecibidaPedido);
                            lineaPedidoCompraDAO.modificarLineaPedidoCompra(lineasPedidos.get(i));

                            pedidoCompleto = lineasPedidos.get(i).getCantidadPedida() == lineasPedidos.get(i).getCantidadRecibida();
                            lineaRemitoDAO.modificarLineaPedidoCompra(lineasRemitos.get(i));
                        }

                        Remito remito = new Remito(idRemito,nroRemito,fechaRemito,fechaRecepcion,idPedido,idProveedor);
                        remitoDAO.modificarRemito(remito);

                        if (pedidoCompleto) pedidoCompraDAO.modificarEstadoPedidoCompra("Completo",pedido.getIdPedidoCompra());
                        else pedidoCompraDAO.modificarEstadoPedidoCompra("Incompleto",pedido.getIdPedidoCompra());

                        Toast msj2 = Toast.makeText(getApplicationContext(),"Remito modificado",Toast.LENGTH_SHORT);
                        msj2.show();

                        String pantallaAnterior = getIntent().getStringExtra("pantallaAnterior");

                        if (pantallaAnterior.contentEquals("PantallaDetalleRemito"))
                        {
                            Intent irPantallaDetalleRemito = getIntent();
                            irPantallaDetalleRemito.putExtra("remitoModificado", remito);
                            setResult(RESULT_OK,irPantallaDetalleRemito);
                        }

                        if (pantallaAnterior.contentEquals("PantallaRemitos")) setResult(RESULT_OK);

                        finish();
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
