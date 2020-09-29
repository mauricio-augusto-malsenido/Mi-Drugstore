package com.example.midrugstore.Pantallas.Compras;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.midrugstore.Adaptadores.AdaptadorPedidos;
import com.example.midrugstore.BaseDeDatos.PedidoCompraDAO;
import com.example.midrugstore.BaseDeDatos.ProveedorDAO;
import com.example.midrugstore.Entidades.PedidoCompra;
import com.example.midrugstore.Entidades.Proveedor;
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

public class PantallaCompras extends AppCompatActivity {

    ListView listaPedidosCompras;
    Spinner filtroProveedorCompra, filtroEstadoCompra;
    Button filtroFechaDesdeCompra, filtroFechaHastaCompra;
    TextView txtTotalCompras;

    List<PedidoCompra> pedidos;
    List<PedidoCompra> pedidosFiltradosPorEstado;
    List<PedidoCompra> pedidosFiltradosPorFechas;
    List<PedidoCompra> pedidosFiltradosPorProveedor;
    List<Proveedor> proveedores;
    PedidoCompra pedidoSeleccionado;

    PedidoCompraDAO pedidoCompraDAO;
    ProveedorDAO proveedorDAO;

    DateFormat formatoFecha;
    DateFormat formatoFechaSQL;

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_compras);

        listaPedidosCompras = findViewById(R.id.listaPedidosCompras);
        filtroProveedorCompra = findViewById(R.id.filtroProveedorCompra);
        filtroEstadoCompra = findViewById(R.id.filtroEstadoCompra);
        filtroFechaDesdeCompra = findViewById(R.id.filtroFechaDesdeCompra);
        filtroFechaHastaCompra = findViewById(R.id.filtroFechaHastaCompra);
        txtTotalCompras = findViewById(R.id.txtTotalCompras);

        registerForContextMenu(listaPedidosCompras);

        pedidoCompraDAO = new PedidoCompraDAO(getApplicationContext());
        proveedorDAO = new ProveedorDAO(getApplicationContext());
        formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        formatoFechaSQL = new SimpleDateFormat("yyyy-MM-dd");

        cargarComboProveedor();

        pedidos = pedidoCompraDAO.obtenerTodosLosPedidosCompras();
        if (!pedidos.isEmpty())
        {
            String fechaDesde = formatoFecha.format(convertirFechaFormatoSQLDate(pedidos.get(0).getFechaPedido()));
            String fechaHasta = formatoFecha.format(convertirFechaFormatoSQLDate(pedidos.get(pedidos.size()-1).getFechaPedido()));
            filtroFechaDesdeCompra.setText(fechaDesde);
            filtroFechaHastaCompra.setText(fechaHasta);
        }
        else
        {
            filtroFechaDesdeCompra.setText(formatoFecha.format(new Date()));
            filtroFechaHastaCompra.setText(formatoFecha.format(new Date()));
        }

        filtroProveedorCompra.setOnItemSelectedListener(seleccionarProveedor);
        filtroEstadoCompra.setOnItemSelectedListener(seleccionarEstado);

        listaPedidosCompras.setOnItemClickListener(clickCortoEnPedido);
        listaPedidosCompras.setOnItemLongClickListener(clickLargoEnPedido);
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
        filtroProveedorCompra.setAdapter(adaptadorFiltroProveedor);
    }

    @SuppressLint("SimpleDateFormat")
    public void seleccionarFecha(View view) throws ParseException {
        Calendar calendario = Calendar.getInstance();
        if (view.getId() == filtroFechaDesdeCompra.getId())
        {
            Date fechaDesde = formatoFecha.parse(filtroFechaDesdeCompra.getText().toString());
            if (fechaDesde != null) calendario.setTime(fechaDesde);
            int año = calendario.get(Calendar.YEAR);
            int mes = calendario.get(Calendar.MONTH);
            int dia = calendario.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePicker = new DatePickerDialog(this,seleccionarFechaDesde,año,mes,dia);
            datePicker.show();
        }
        if (view.getId() == filtroFechaHastaCompra.getId())
        {
            Date fechaHasta = formatoFecha.parse(filtroFechaHastaCompra.getText().toString());
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
        Date fDesde = convertirFechaFormatoComunDate(filtroFechaDesdeCompra.getText().toString());
        Date fHasta = convertirFechaFormatoComunDate(filtroFechaHastaCompra.getText().toString());
        String fechaDesde = formatoFechaSQL.format(fDesde);
        String fechaHasta = formatoFechaSQL.format(fHasta);
        pedidosFiltradosPorFechas = pedidoCompraDAO.obtenerTodosLosPedidosComprasPorFechas(fechaDesde,fechaHasta);
    }

    public void filtrarProveedor()
    {
        pedidosFiltradosPorProveedor = new ArrayList<>();
        String proveedorSeleccionado = filtroProveedorCompra.getSelectedItem().toString();
        if (proveedorSeleccionado.contentEquals("Todos"))
        {
            pedidosFiltradosPorProveedor.addAll(pedidosFiltradosPorFechas);
        }
        else
        {
            Proveedor proveedor = proveedorDAO.obtenerProveedorPorNombre(proveedorSeleccionado);
            for (int i=0; i<pedidosFiltradosPorFechas.size();i++)
            {
                if (pedidosFiltradosPorFechas.get(i).getIdProveedor() == proveedor.getIdProveedor())
                {
                    pedidosFiltradosPorProveedor.add(pedidosFiltradosPorFechas.get(i));
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    public void filtrarEstado()
    {
        pedidosFiltradosPorEstado = new ArrayList<>();
        String estadoSeleccionado = filtroEstadoCompra.getSelectedItem().toString();
        DecimalFormat formatoDecimal = new DecimalFormat("0.00");
        float total = 0;
        if (estadoSeleccionado.contentEquals("Espera"))
        {
            for (int i = 0; i< pedidosFiltradosPorProveedor.size(); i++)
            {
                if (pedidosFiltradosPorProveedor.get(i).getEstado().contentEquals("Espera"))
                {
                    pedidosFiltradosPorEstado.add(pedidosFiltradosPorProveedor.get(i));
                    total = total + pedidosFiltradosPorProveedor.get(i).getTotal();
                }
            }
            txtTotalCompras.setText("$ " + formatoDecimal.format(total));
        }
        if (estadoSeleccionado.contentEquals("Completo"))
        {
            for (int i = 0; i< pedidosFiltradosPorProveedor.size(); i++)
            {
                if (pedidosFiltradosPorProveedor.get(i).getEstado().contentEquals("Completo"))
                {
                    pedidosFiltradosPorEstado.add(pedidosFiltradosPorProveedor.get(i));
                    total = total + pedidosFiltradosPorProveedor.get(i).getTotal();
                }
            }
            txtTotalCompras.setText("$ " + formatoDecimal.format(total));
        }
        if (estadoSeleccionado.contentEquals("Incompleto"))
        {
            for (int i = 0; i< pedidosFiltradosPorProveedor.size(); i++)
            {
                if (pedidosFiltradosPorProveedor.get(i).getEstado().contentEquals("Incompleto"))
                {
                    pedidosFiltradosPorEstado.add(pedidosFiltradosPorProveedor.get(i));
                    total = total + pedidosFiltradosPorProveedor.get(i).getTotal();
                }
            }
            txtTotalCompras.setText("$ " + formatoDecimal.format(total));
        }
        if (estadoSeleccionado.contentEquals("Cancelado"))
        {
            for (int i = 0; i< pedidosFiltradosPorProveedor.size(); i++)
            {
                if (pedidosFiltradosPorProveedor.get(i).getEstado().contentEquals("Cancelado"))
                {
                    pedidosFiltradosPorEstado.add(pedidosFiltradosPorProveedor.get(i));
                }
            }
            txtTotalCompras.setText("");
        }
        AdaptadorPedidos adaptador = new AdaptadorPedidos(getApplicationContext(), pedidosFiltradosPorEstado);
        listaPedidosCompras.setAdapter(adaptador);
    }

    public void actualizarListaSinReiniciarFiltros()
    {
        pedidos.clear();
        pedidos = pedidoCompraDAO.obtenerTodosLosPedidosCompras();
        filtrarFecha();
        filtrarProveedor();
        filtrarEstado();
    }

    public void actualizarListaReiniciandoFiltros()
    {
        pedidos.clear();
        pedidos = pedidoCompraDAO.obtenerTodosLosPedidosCompras();
        String fechaDesde = formatoFecha.format(convertirFechaFormatoSQLDate(pedidos.get(0).getFechaPedido()));
        String fechaHasta = formatoFecha.format(convertirFechaFormatoSQLDate(pedidos.get(pedidos.size()-1).getFechaPedido()));
        filtroFechaDesdeCompra.setText(fechaDesde);
        filtroFechaHastaCompra.setText(fechaHasta);
        filtroProveedorCompra.setSelection(0);
        filtroEstadoCompra.setSelection(0);
        filtrarFecha();
        filtrarProveedor();
        filtrarEstado();
    }

    AdapterView.OnItemClickListener clickCortoEnPedido = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            pedidoSeleccionado = (PedidoCompra) parent.getItemAtPosition(position);
            Intent irPantallaDetalleCompra = new Intent(getApplicationContext(), PantallaDetalleCompra.class);
            irPantallaDetalleCompra.putExtra("pedidoSeleccionado",pedidoSeleccionado);
            startActivityForResult(irPantallaDetalleCompra,200);
        }
    };

    AdapterView.OnItemLongClickListener clickLargoEnPedido = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            pedidoSeleccionado = (PedidoCompra) listaPedidosCompras.getItemAtPosition(position);
            return false;
        }
    };

    AdapterView.OnItemSelectedListener seleccionarEstado = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            filtrarFecha();
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
            filtrarFecha();
            filtrarProveedor();
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
            filtroFechaDesdeCompra.setText(fecha);
            filtrarFecha();
            filtrarProveedor();
            filtrarEstado();
        }
    };

    DatePickerDialog.OnDateSetListener seleccionarFechaHasta = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            NumberFormat nf = new DecimalFormat("00");
            String fecha = nf.format(dayOfMonth) + "/" + nf.format((month + 1)) + "/" + year;
            filtroFechaHastaCompra.setText(fecha);
            filtrarFecha();
            filtrarProveedor();
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
            Intent irPantallaNuevaCompra = new Intent(getApplicationContext(), PantallaNuevaCompra.class);
            startActivityForResult(irPantallaNuevaCompra,100);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        getMenuInflater().inflate(R.menu.menu_contextual_lista,menu);

        menu.getItem(0).setVisible(false);
        menu.getItem(1).setVisible(false);

        if (!pedidoSeleccionado.getEstado().contentEquals("Cancelado")) menu.getItem(5).setVisible(true);
        else menu.getItem(5).setVisible(false);

        if (pedidoSeleccionado.getEstado().contentEquals("Espera")) menu.getItem(6).setVisible(true);
        else menu.getItem(6).setVisible(false);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.opRemitos) {
            Intent irPantallaRemitos = new Intent(getApplicationContext(), PantallaRemitos.class);
            irPantallaRemitos.putExtra("pedidoSeleccionado",pedidoSeleccionado);
            startActivityForResult(irPantallaRemitos,300);
            return true;
        }
        if (item.getItemId() == R.id.opCancelar) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("¿Desea cancelar el pedido seleccionado?\n\nAclaración: Esta acción es irreversible.")
                    .setTitle("Confirmación")
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            pedidoCompraDAO.modificarEstadoPedidoCompra("Cancelado", pedidoSeleccionado.getIdPedidoCompra());

                            Toast msj = Toast.makeText(getApplicationContext(), "Pedido Cancelado", Toast.LENGTH_SHORT);
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
        if (resultCode == RESULT_OK && requestCode == 300) actualizarListaSinReiniciarFiltros();
    }
}
