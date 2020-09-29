package com.example.midrugstore.Pantallas.Compras;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PantallaNuevoRemito extends AppCompatActivity {

    ListView listaLineasRemitoNR;
    EditText etNroRemitoNR, etFechaRemitoNR, etFechaRecepcionNR;
    LinearLayout contenedorDatosRemitoNR;

    FragmentManager manager;

    List<Remito> remitos;
    List<LineaRemito> lineasRemitos;
    List<LineaRemito> lineasRemitosRegistradas;
    List<LineaPedidoCompra> lineasPedidos;
    PedidoCompra pedidoSeleccionado;
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
        setContentView(R.layout.pantalla_nuevo_remito);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        listaLineasRemitoNR = findViewById(R.id.listaLineasRemitoNR);
        etNroRemitoNR = findViewById(R.id.etNroRemitoNR);
        etFechaRemitoNR = findViewById(R.id.etFechaRemitoNR);
        etFechaRecepcionNR = findViewById(R.id.etFechaRecepcionNR);
        contenedorDatosRemitoNR = findViewById(R.id.contenedorDatosRemitoNR);

        manager = getSupportFragmentManager();

        remitoDAO = new RemitoDAO(getApplicationContext());
        lineaRemitoDAO = new LineaRemitoDAO(getApplicationContext());
        pedidoCompraDAO = new PedidoCompraDAO(getApplicationContext());
        lineaPedidoCompraDAO = new LineaPedidoCompraDAO(getApplicationContext());
        productoDAO = new ProductoDAO(getApplicationContext());
        lineasRemitos = new ArrayList<>();

        pedidoSeleccionado = (PedidoCompra) getIntent().getExtras().getSerializable("pedidoSeleccionado");
        lineasPedidos = lineaPedidoCompraDAO.obtenerTodasLasLineasPedidosComprasPorPedido(pedidoSeleccionado.getIdPedidoCompra());

        for (int i=0; i<lineasPedidos.size(); i++)
        {
            int idLineaRemito = 0;
            int cantidadRemito = lineasPedidos.get(i).getCantidadPedida() - lineasPedidos.get(i).getCantidadRecibida();
            int cantidadRecibida = 0;
            int idRemito = 0;
            int idProducto = lineasPedidos.get(i).getIdProducto();

            LineaRemito lineaRemito = new LineaRemito(idLineaRemito,cantidadRemito,cantidadRecibida,idRemito,idProducto);
            lineasRemitos.add(lineaRemito);
        }

        AdaptadorLineasRemito adaptadorLineasRemito = new AdaptadorLineasRemito(getApplicationContext(),lineasRemitos);
        listaLineasRemitoNR.setAdapter(adaptadorLineasRemito);

        listaLineasRemitoNR.setOnItemClickListener(clickCortoEnLineaRemito);

        formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        formatoFechaSQL = new SimpleDateFormat("yyyy-MM-dd");
    }

    @SuppressLint("SimpleDateFormat")
    public void seleccionarFecha(View view) throws ParseException {
        etNroRemitoNR.clearFocus();
        listaLineasRemitoNR.requestFocus();
        esconderTeclado();

        Calendar calendario = Calendar.getInstance();
        if (view.getId() == etFechaRemitoNR.getId())
        {
            Date fechaRemito;
            if (!etFechaRemitoNR.getText().toString().isEmpty()) fechaRemito = formatoFecha.parse(etFechaRemitoNR.getText().toString());
            else fechaRemito = new Date();
            if (fechaRemito != null) calendario.setTime(fechaRemito);
            int año = calendario.get(Calendar.YEAR);
            int mes = calendario.get(Calendar.MONTH);
            int dia = calendario.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePicker = new DatePickerDialog(this,seleccionarFechaRemito,año,mes,dia);
            datePicker.show();
        }
        if (view.getId() == etFechaRecepcionNR.getId())
        {
            Date fechaRecepcion;
            if (!etFechaRecepcionNR.getText().toString().isEmpty()) fechaRecepcion = formatoFecha.parse(etFechaRecepcionNR.getText().toString());
            else fechaRecepcion = new Date();
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
        List<Remito> remitosPorProveedor = remitoDAO.obtenerTodosLosRemitosPorProveedor(pedidoSeleccionado.getIdProveedor());
        for (int i=0; i<remitosPorProveedor.size(); i++)
        {
            if (remitosPorProveedor.get(i).getNroRemito() == nroRemito) remitoExistente = true;
        }
        return remitoExistente;
    }

    AdapterView.OnItemClickListener clickCortoEnLineaRemito = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            etNroRemitoNR.clearFocus();
            listaLineasRemitoNR.requestFocus();
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
                                    if (cantidadRemito > lineasPedidos.get(i).getCantidadPedida() - lineasPedidos.get(i).getCantidadRecibida())
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
                            listaLineasRemitoNR.setAdapter(adaptadorLineasRemito);

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
            etFechaRemitoNR.setText(fecha);
        }
    };

    DatePickerDialog.OnDateSetListener seleccionarFechaRecepcion = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            NumberFormat nf = new DecimalFormat("00");
            String fecha = nf.format(dayOfMonth) + "/" + nf.format((month + 1)) + "/" + year;
            etFechaRecepcionNR.setText(fecha);
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
            if (etNroRemitoNR.getText().toString().isEmpty() || etFechaRemitoNR.getText().toString().isEmpty() || etFechaRecepcionNR.getText().toString().isEmpty())
            {
                DialogoCamposVacios camposVacios = new DialogoCamposVacios();
                camposVacios.show(manager,"Error");
            }
            else
            {
                Date fRemito = convertirFechaFormatoComunDate(etFechaRemitoNR.getText().toString());
                Date fRecepcion = convertirFechaFormatoComunDate(etFechaRecepcionNR.getText().toString());
                if (fRemito.after(fRecepcion))
                {
                    DialogoFechaRemitoMayorQueRecepcion fechaRemitoMayorQueRecepcion = new DialogoFechaRemitoMayorQueRecepcion();
                    fechaRemitoMayorQueRecepcion.show(manager,"Error");
                }
                else
                {
                    remitos = remitoDAO.obtenerTodosLosRemitos();
                    lineasRemitosRegistradas = lineaRemitoDAO.obtenerTodasLasLineasRemitos();

                    int idRemito;
                    if(remitos.size() > 0) idRemito = remitos.get(remitos.size() - 1).getIdRemito() + 1;
                    else idRemito = 1;

                    int nroRemito = Integer.parseInt(etNroRemitoNR.getText().toString());
                    if (verificarRemitoExistente(nroRemito))
                    {
                        DialogoRemitoExistente remitoExistente = new DialogoRemitoExistente();
                        remitoExistente.show(manager,"Error");
                    }
                    else
                    {
                        String fechaRemito = formatoFechaSQL.format(fRemito);
                        String fechaRecepcion = formatoFechaSQL.format(fRecepcion);
                        int idPedido = pedidoSeleccionado.getIdPedidoCompra();
                        int idProveedor = pedidoSeleccionado.getIdProveedor();

                        boolean pedidoCompleto = false;
                        for (int i=0;i<lineasRemitos.size();i++)
                        {
                            int idLineaRemito;
                            if(lineasRemitosRegistradas.size() > 0) idLineaRemito = lineasRemitosRegistradas.get(lineasRemitosRegistradas.size() - 1).getIdLineaRemito() + i + 1;
                            else idLineaRemito = i + 1;

                            lineasRemitos.get(i).setIdLineaRemito(idLineaRemito);
                            lineasRemitos.get(i).setIdRemito(idRemito);

                            Producto producto = productoDAO.obtenerProductoPorId(lineasRemitos.get(i).getIdProducto());
                            int nuevoStock = producto.getStock() + lineasRemitos.get(i).getCantidadRecibida();
                            producto.setStock(nuevoStock);
                            productoDAO.modificarProducto(producto);

                            int cantRecibidaPedido = lineasPedidos.get(i).getCantidadRecibida() + lineasRemitos.get(i).getCantidadRecibida();
                            lineasPedidos.get(i).setCantidadRecibida(cantRecibidaPedido);
                            lineaPedidoCompraDAO.modificarLineaPedidoCompra(lineasPedidos.get(i));

                            pedidoCompleto = lineasPedidos.get(i).getCantidadPedida() == lineasPedidos.get(i).getCantidadRecibida();
                            lineaRemitoDAO.crearLineaRemito(lineasRemitos.get(i));
                        }

                        Remito remito = new Remito(idRemito,nroRemito,fechaRemito,fechaRecepcion,idPedido,idProveedor);
                        remitoDAO.crearRemito(remito);

                        if (pedidoCompleto) pedidoCompraDAO.modificarEstadoPedidoCompra("Completo",pedidoSeleccionado.getIdPedidoCompra());
                        else pedidoCompraDAO.modificarEstadoPedidoCompra("Incompleto",pedidoSeleccionado.getIdPedidoCompra());

                        Toast msj2 = Toast.makeText(getApplicationContext(),"Remito registrado",Toast.LENGTH_SHORT);
                        msj2.show();

                        setResult(RESULT_OK);
                        finish();
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
