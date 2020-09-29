package com.example.midrugstore.Pantallas.Compras;

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

import com.example.midrugstore.Adaptadores.AdaptadorLineasPedidoNuevo;
import com.example.midrugstore.BaseDeDatos.LineaPedidoCompraDAO;
import com.example.midrugstore.BaseDeDatos.PedidoCompraDAO;
import com.example.midrugstore.BaseDeDatos.ProductoDAO;
import com.example.midrugstore.BaseDeDatos.ProveedorDAO;
import com.example.midrugstore.Entidades.LineaPedidoCompra;
import com.example.midrugstore.Entidades.PedidoCompra;
import com.example.midrugstore.Entidades.Producto;
import com.example.midrugstore.Entidades.Proveedor;
import com.example.midrugstore.Pantallas.Dialogos.DialogoCamposVacios;
import com.example.midrugstore.Pantallas.Dialogos.DialogoCancelarOperacion;
import com.example.midrugstore.Pantallas.Dialogos.DialogoNingunProveedorSeleccionado;
import com.example.midrugstore.Pantallas.Dialogos.DialogoPedidoVacio;
import com.example.midrugstore.Pantallas.Dialogos.DialogoProductoAgregado;
import com.example.midrugstore.Pantallas.Productos.PantallaSeleccionarProductoCompra;
import com.example.midrugstore.Pantallas.Proveedores.PantallaSeleccionarProveedor;
import com.example.midrugstore.R;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PantallaNuevaCompra extends AppCompatActivity {

    ListView listaLineasPedidoNC;
    EditText etProveedorNC, etProductoNC, etCantidadNC;
    TextView txtTotalPedidoNC;
    Button btnAgregarNC;
    LinearLayout contenedorAgregarProductoNC;

    FragmentManager manager;
    List<PedidoCompra> pedidos;
    List<LineaPedidoCompra> lineasPedidos;
    List<LineaPedidoCompra> lineasPedidosRegistradas;
    PedidoCompraDAO pedidoCompraDAO;
    LineaPedidoCompraDAO lineaPedidoCompraDAO;
    ProveedorDAO proveedorDAO;
    ProductoDAO productoDAO;
    Producto productoSeleccionado;
    Proveedor proveedorSeleccionado;
    LineaPedidoCompra lineaPedidoSeleccionada;

    DateFormat formatoFecha;
    DateFormat formatoHora;
    DecimalFormat formatoDecimal;

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_nueva_compra);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        listaLineasPedidoNC = findViewById(R.id.listaLineasPedidoNC);
        etProductoNC = findViewById(R.id.etProductoNC);
        etCantidadNC = findViewById(R.id.etCantidadNC);
        etProveedorNC = findViewById(R.id.etProveedorNC);
        txtTotalPedidoNC = findViewById(R.id.txtTotalPedidoNC);
        btnAgregarNC = findViewById(R.id.btnAgregarNC);
        contenedorAgregarProductoNC = findViewById(R.id.contenedorAgregarProductoNC);

        registerForContextMenu(listaLineasPedidoNC);

        manager = getSupportFragmentManager();

        pedidoCompraDAO = new PedidoCompraDAO(getApplicationContext());
        lineaPedidoCompraDAO = new LineaPedidoCompraDAO(getApplicationContext());
        proveedorDAO = new ProveedorDAO(getApplicationContext());
        productoDAO = new ProductoDAO(getApplicationContext());
        lineasPedidos = new ArrayList<>();

        listaLineasPedidoNC.setOnItemLongClickListener(clickLargoEnLineaPedido);

        formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
        formatoHora = new SimpleDateFormat("HH:mm");
        formatoDecimal = new DecimalFormat("0.00");
    }

    public void seleccionarProveedor(View view)
    {
        etCantidadNC.clearFocus();
        contenedorAgregarProductoNC.requestFocus();
        esconderTeclado();
        Intent irPantallaSeleccionarProveedor = new Intent(getApplicationContext(), PantallaSeleccionarProveedor.class);
        startActivityForResult(irPantallaSeleccionarProveedor,200);
    }

    public void seleccionarProducto(View view)
    {
        etCantidadNC.clearFocus();
        contenedorAgregarProductoNC.requestFocus();
        esconderTeclado();
        if (etProveedorNC.getText().toString().isEmpty())
        {
            DialogoNingunProveedorSeleccionado ningunProveedorSeleccionado = new DialogoNingunProveedorSeleccionado();
            ningunProveedorSeleccionado.show(manager,"Error");
        }
        else
        {
            Intent irPantallaSeleccionarProductoCompra = new Intent(getApplicationContext(), PantallaSeleccionarProductoCompra.class);
            irPantallaSeleccionarProductoCompra.putExtra("proveedorSeleccionado",etProveedorNC.getText().toString());
            startActivityForResult(irPantallaSeleccionarProductoCompra,100);
        }
    }

    public void agregarProducto(View view)
    {
        etCantidadNC.clearFocus();
        contenedorAgregarProductoNC.requestFocus();
        esconderTeclado();

        if (etProductoNC.getText().toString().isEmpty() || etCantidadNC.getText().toString().isEmpty())
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
                int idLineaPedido = 0;
                int cantidadPedida = Integer.parseInt(etCantidadNC.getText().toString());
                int cantidadRecibida = 0;
                float costo = productoSeleccionado.getCostoCompra();
                float subtotal = costo * cantidadPedida;
                int idPedido = 0;
                int idProducto = productoSeleccionado.getIdProducto();

                LineaPedidoCompra lineaPedido = new LineaPedidoCompra(idLineaPedido,cantidadPedida,cantidadRecibida,subtotal,idPedido,idProducto);
                lineasPedidos.add(lineaPedido);

                AdaptadorLineasPedidoNuevo adaptadorLineasPedido = new AdaptadorLineasPedidoNuevo(getApplicationContext(),lineasPedidos);
                listaLineasPedidoNC.setAdapter(adaptadorLineasPedido);

                float total;
                try {
                    total = formatoDecimal.parse(txtTotalPedidoNC.getText().toString()).floatValue();
                    float nuevoTotal = total + subtotal;
                    txtTotalPedidoNC.setText(formatoDecimal.format(nuevoTotal));
                }
                catch (ParseException e) { e.printStackTrace(); }

                etProductoNC.setText("");
                etCantidadNC.setText("");
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
        Producto producto = productoDAO.obtenerProveedorPorDescripcion(etProductoNC.getText().toString());
        boolean productoAgregado = false;
        if (!lineasPedidos.isEmpty())
        {
            for (int i=0; i<lineasPedidos.size(); i++)
            {
                if (lineasPedidos.get(i).getIdProducto() == producto.getIdProducto())
                    productoAgregado = true;
            }
        }
        return productoAgregado;
    }

    AdapterView.OnItemLongClickListener clickLargoEnLineaPedido = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            lineaPedidoSeleccionada = (LineaPedidoCompra) listaLineasPedidoNC.getItemAtPosition(position);
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
            if (lineasPedidos.isEmpty())
            {
                DialogoPedidoVacio pedidoVacio = new DialogoPedidoVacio();
                pedidoVacio.show(manager,"Error");
            }
            else
            {
                pedidos = pedidoCompraDAO.obtenerTodosLosPedidosCompras();
                lineasPedidosRegistradas = lineaPedidoCompraDAO.obtenerTodasLasLineasPedidosCompras();

                int idPedido;
                if(pedidos.size() > 0) idPedido = pedidos.get(pedidos.size() - 1).getIdPedidoCompra() + 1;
                else idPedido = 1;

                Proveedor proveedor = proveedorDAO.obtenerProveedorPorNombre(etProveedorNC.getText().toString());
                String fechaPedido = formatoFecha.format(new Date());
                String horaPedido = formatoHora.format(new Date());
                float total = 0;
                try {
                    total = formatoDecimal.parse(txtTotalPedidoNC.getText().toString()).floatValue();;
                } catch (ParseException e) { e.printStackTrace(); }
                String estado = "Espera";

                for (int i=0;i<lineasPedidos.size();i++)
                {
                    int idLineaPedido;
                    if(lineasPedidosRegistradas.size() > 0) idLineaPedido = lineasPedidosRegistradas.get(lineasPedidosRegistradas.size() - 1).getIdLineaPedidoCompra() + i + 1;
                    else idLineaPedido = i + 1;

                    lineasPedidos.get(i).setIdLineaPedidoCompra(idLineaPedido);
                    lineasPedidos.get(i).setIdPedidoCompra(idPedido);
                    lineaPedidoCompraDAO.crearLineaPedidoCompra(lineasPedidos.get(i));
                }

                PedidoCompra pedido = new PedidoCompra(idPedido,fechaPedido,horaPedido,estado,total,proveedor.getIdProveedor());
                pedidoCompraDAO.crearPedidoCompra(pedido);

                Toast msj2 = Toast.makeText(getApplicationContext(),"Pedido registrado",Toast.LENGTH_SHORT);
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
            builder.setMessage("¿Desea quitar la linea de pedido seleccionada?")
                    .setTitle("Confirmación")
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            float total;
                            try {
                                total = formatoDecimal.parse(txtTotalPedidoNC.getText().toString()).floatValue();
                                float nuevoTotal = total - lineaPedidoSeleccionada.getSubtotal();
                                txtTotalPedidoNC.setText(formatoDecimal.format(nuevoTotal));
                            }
                            catch (ParseException e) { e.printStackTrace(); }

                            lineasPedidos.remove(lineaPedidoSeleccionada);
                            AdaptadorLineasPedidoNuevo adaptadorLineasPedido = new AdaptadorLineasPedidoNuevo(getApplicationContext(),lineasPedidos);
                            listaLineasPedidoNC.setAdapter(adaptadorLineasPedido);
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
            etProductoNC.setText(productoSeleccionado.getDescripcion());
        }

        if (resultCode == RESULT_OK && requestCode == 200)
        {
            proveedorSeleccionado = (Proveedor) data.getExtras().getSerializable("proveedorSeleccionado");
            if (!etProveedorNC.getText().toString().isEmpty())
            {
                if (!etProveedorNC.getText().toString().contentEquals(proveedorSeleccionado.getNombre()))
                {
                    if (!etProductoNC.getText().toString().isEmpty() || !etCantidadNC.getText().toString().isEmpty() || !lineasPedidos.isEmpty())
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage("¿Desea cambiar el proveedor?\n\nAclaración: Se limpiarán el \ncontenido de los campos y las lineas de pedido cargadas previamente.")
                                .setTitle("Confirmación")
                                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                    @SuppressLint("SetTextI18n")
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        etProductoNC.setText("");
                                        etCantidadNC.setText("");
                                        txtTotalPedidoNC.setText("0,00");

                                        lineasPedidos.clear();
                                        AdaptadorLineasPedidoNuevo adaptadorLineasPedido = new AdaptadorLineasPedidoNuevo(getApplicationContext(),lineasPedidos);
                                        listaLineasPedidoNC.setAdapter(adaptadorLineasPedido);

                                        contenedorAgregarProductoNC.requestFocus();
                                        esconderTeclado();

                                        etProveedorNC.setText(proveedorSeleccionado.getNombre());
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
                    }
                    else etProveedorNC.setText(proveedorSeleccionado.getNombre());
                }
            }
            else etProveedorNC.setText(proveedorSeleccionado.getNombre());
        }
    }
}
