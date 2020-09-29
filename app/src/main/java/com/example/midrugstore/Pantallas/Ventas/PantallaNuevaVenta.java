package com.example.midrugstore.Pantallas.Ventas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.example.midrugstore.Pantallas.Dialogos.DialogoCamposVacios;
import com.example.midrugstore.Pantallas.Dialogos.DialogoCancelarOperacion;
import com.example.midrugstore.Pantallas.Dialogos.DialogoProductoAgregado;
import com.example.midrugstore.Pantallas.Dialogos.DialogoStockInsuficiente;
import com.example.midrugstore.Pantallas.Dialogos.DialogoVentaVacia;
import com.example.midrugstore.Pantallas.Productos.PantallaSeleccionarProductoVenta;
import com.example.midrugstore.R;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PantallaNuevaVenta extends AppCompatActivity {

    ListView listaLineasVentasNV;
    EditText etProductoNV, etCantidadNV;
    TextView txtTotalVentaNV;
    Button btnAgregarNV;
    LinearLayout contenedorAgregarProductoNV;

    FragmentManager manager;
    List<Venta> ventas;
    List<LineaVenta> lineasVentas;
    List<LineaVenta> lineasVentasRegistradas;
    VentaDAO ventaDAO;
    LineaVentaDAO lineaVentaDAO;
    ProductoDAO productoDAO;
    Producto productoSeleccionado;
    LineaVenta lineaVentaSeleccionada;

    DateFormat formatoFecha;
    DateFormat formatoHora;
    DecimalFormat formatoDecimal;

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_nueva_venta);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        listaLineasVentasNV = findViewById(R.id.listaLineasVentasNV);
        etProductoNV = findViewById(R.id.etProductoNV);
        etCantidadNV = findViewById(R.id.etCantidadNV);
        txtTotalVentaNV = findViewById(R.id.txtTotalVentaNV);
        btnAgregarNV = findViewById(R.id.btnAgregarNV);
        contenedorAgregarProductoNV = findViewById(R.id.contenedorAgregarProductoNV);

        registerForContextMenu(listaLineasVentasNV);

        manager = getSupportFragmentManager();

        ventaDAO = new VentaDAO(getApplicationContext());
        lineaVentaDAO = new LineaVentaDAO(getApplicationContext());
        productoDAO = new ProductoDAO(getApplicationContext());
        lineasVentas = new ArrayList<>();

        listaLineasVentasNV.setOnItemLongClickListener(clickLargoEnLineaVenta);

        formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
        formatoHora = new SimpleDateFormat("HH:mm");
        formatoDecimal = new DecimalFormat("0.00");
    }

    public void seleccionarProducto(View view)
    {
        etCantidadNV.clearFocus();
        contenedorAgregarProductoNV.requestFocus();
        esconderTeclado();

        Intent irPantallaSeleccionarProductoVenta = new Intent(getApplicationContext(), PantallaSeleccionarProductoVenta.class);
        startActivityForResult(irPantallaSeleccionarProductoVenta,100);
    }

    public void agregarProducto(View view)
    {
        etCantidadNV.clearFocus();
        contenedorAgregarProductoNV.requestFocus();
        esconderTeclado();

        if (etProductoNV.getText().toString().isEmpty() || etCantidadNV.getText().toString().isEmpty())
        {
            DialogoCamposVacios camposVacios = new DialogoCamposVacios();
            camposVacios.show(manager,"Error");
        }
        else
        {
            if (productoAgregado())
            {
                DialogoProductoAgregado productoAgregado = new DialogoProductoAgregado();
                productoAgregado.show(manager,"Error");
            }
            else
            {
                int cantidad = Integer.parseInt(etCantidadNV.getText().toString());
                if (productoSeleccionado.getStock() < cantidad)
                {
                    DialogoStockInsuficiente stockInsuficiente = new DialogoStockInsuficiente();
                    stockInsuficiente.show(manager,"Error");
                }
                else
                {
                    int idLineaVenta = 0;
                    float precio = productoSeleccionado.getPrecioVenta();
                    float subtotal = precio * cantidad;
                    int idVenta = 0;
                    int idProducto = productoSeleccionado.getIdProducto();

                    LineaVenta lineaVenta = new LineaVenta(idLineaVenta,cantidad,subtotal,idVenta,idProducto);
                    lineasVentas.add(lineaVenta);

                    AdaptadorLineasVenta adaptadorLineasVenta = new AdaptadorLineasVenta(getApplicationContext(),lineasVentas);
                    listaLineasVentasNV.setAdapter(adaptadorLineasVenta);

                    float total;
                    try {
                        total = formatoDecimal.parse(txtTotalVentaNV.getText().toString()).floatValue();
                        float nuevoTotal = total + subtotal;
                        txtTotalVentaNV.setText(formatoDecimal.format(nuevoTotal));
                    }
                    catch (ParseException e) { e.printStackTrace(); }

                    etProductoNV.setText("");
                    etCantidadNV.setText("");
                }
            }
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

    public boolean productoAgregado()
    {
        Producto producto = productoDAO.obtenerProveedorPorDescripcion(etProductoNV.getText().toString());
        boolean productoAgregado = false;
        if (!lineasVentas.isEmpty())
        {
            for (int i=0; i<lineasVentas.size(); i++)
            {
                if (lineasVentas.get(i).getIdProducto() == producto.getIdProducto())
                    productoAgregado = true;
            }
        }
        return productoAgregado;
    }

    AdapterView.OnItemLongClickListener clickLargoEnLineaVenta = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            lineaVentaSeleccionada = (LineaVenta) listaLineasVentasNV.getItemAtPosition(position);
            return false;
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
            if (lineasVentas.isEmpty())
            {
                DialogoVentaVacia ventaVacia = new DialogoVentaVacia();
                ventaVacia.show(manager,"Error");
            }
            else
            {
                ventas = ventaDAO.obtenerTodasLasVentas();
                lineasVentasRegistradas = lineaVentaDAO.obtenerTodasLasLineasVentas();

                int idVenta;
                if(ventas.size() > 0) idVenta = ventas.get(ventas.size() - 1).getIdVenta() + 1;
                else idVenta = 1;

                String fechaVenta = formatoFecha.format(new Date());
                String horaVenta = formatoHora.format(new Date());
                float total = 0;
                try {
                    total = formatoDecimal.parse(txtTotalVentaNV.getText().toString()).floatValue();;
                } catch (ParseException e) { e.printStackTrace(); }
                String estado = "No Anulada";

                for (int i=0;i<lineasVentas.size();i++)
                {
                    int idLineaVenta;
                    if(lineasVentasRegistradas.size() > 0) idLineaVenta = lineasVentasRegistradas.get(lineasVentasRegistradas.size() - 1).getIdLineaVenta() + i + 1;
                    else idLineaVenta = i + 1;

                    lineasVentas.get(i).setIdLineaVenta(idLineaVenta);
                    lineasVentas.get(i).setIdVenta(idVenta);
                    lineaVentaDAO.crearLineaVenta(lineasVentas.get(i));

                    Producto producto = productoDAO.obtenerProductoPorId(lineasVentas.get(i).getIdProducto());
                    int nuevoStock = producto.getStock() - lineasVentas.get(i).getCantidad();
                    producto.setStock(nuevoStock);
                    productoDAO.modificarProducto(producto);
                }

                Venta venta = new Venta(idVenta,fechaVenta,horaVenta,estado,total);
                ventaDAO.crearVenta(venta);

                Toast msj2 = Toast.makeText(getApplicationContext(),"Venta registrada",Toast.LENGTH_SHORT);
                msj2.show();

                setResult(RESULT_OK);
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        getMenuInflater().inflate(R.menu.menu_contextual_lista_lineas,menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.opQuitar) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("¿Desea quitar la linea de venta seleccionada?")
                    .setTitle("Confirmación")
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            float total;
                            try {
                                total = formatoDecimal.parse(txtTotalVentaNV.getText().toString()).floatValue();
                                float nuevoTotal = total - lineaVentaSeleccionada.getSubtotal();
                                txtTotalVentaNV.setText(formatoDecimal.format(nuevoTotal));
                            }
                            catch (ParseException e) { e.printStackTrace(); }

                            lineasVentas.remove(lineaVentaSeleccionada);
                            AdaptadorLineasVenta adaptadorLineasVenta = new AdaptadorLineasVenta(getApplicationContext(),lineasVentas);
                            listaLineasVentasNV.setAdapter(adaptadorLineasVenta);
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

        if (resultCode == RESULT_OK && requestCode == 100)
        {
            productoSeleccionado = (Producto) data.getExtras().getSerializable("productoSeleccionado");
            etProductoNV.setText(productoSeleccionado.getDescripcion());
        }
    }
}
