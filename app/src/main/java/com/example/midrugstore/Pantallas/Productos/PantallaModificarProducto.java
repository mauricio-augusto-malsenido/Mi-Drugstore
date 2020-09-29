package com.example.midrugstore.Pantallas.Productos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.midrugstore.BaseDeDatos.ProductoDAO;
import com.example.midrugstore.BaseDeDatos.ProveedorDAO;
import com.example.midrugstore.Entidades.Producto;
import com.example.midrugstore.Entidades.Proveedor;
import com.example.midrugstore.Pantallas.Dialogos.DialogoCamposVacios;
import com.example.midrugstore.Pantallas.Dialogos.DialogoCancelarOperacion;
import com.example.midrugstore.Pantallas.Dialogos.DialogoRegistroExistente;
import com.example.midrugstore.R;

import java.util.ArrayList;
import java.util.List;

public class PantallaModificarProducto extends AppCompatActivity {

    EditText etDescripcionMP, etPrecioMP, etCostoMP, etStockMP;
    Spinner comboCategoriaMP, comboProveedorMP, comboEstadoMP;
    FragmentManager manager;
    List<Producto> productos;
    Producto productoSeleccionado;
    ProductoDAO productoDAO;
    ProveedorDAO proveedorDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_modificar_producto);

        etDescripcionMP = findViewById(R.id.etDescripcionMP);
        etPrecioMP = findViewById(R.id.etPrecioMP);
        etCostoMP = findViewById(R.id.etCostoMP);
        etStockMP = findViewById(R.id.etStockMP);
        comboCategoriaMP = findViewById(R.id.comboCategoriaMP);
        comboProveedorMP = findViewById(R.id.comboProveedorMP);
        comboEstadoMP = findViewById(R.id.comboEstadoMP);

        manager = getSupportFragmentManager();

        productoDAO = new ProductoDAO(getApplicationContext());
        proveedorDAO = new ProveedorDAO(getApplicationContext());

        cargarComboProveedor();

        productoSeleccionado = (Producto) getIntent().getExtras().getSerializable("productoSeleccionado");

        etDescripcionMP.setText(productoSeleccionado.getDescripcion());
        etPrecioMP.setText(String.valueOf(productoSeleccionado.getPrecioVenta()));
        etCostoMP.setText(String.valueOf(productoSeleccionado.getCostoCompra()));
        etStockMP.setText(String.valueOf(productoSeleccionado.getStock()));
        Proveedor provProductoSeleccionado = proveedorDAO.obtenerProveedorPorId(productoSeleccionado.getIdProveedor());

        for (int i=0;i<comboCategoriaMP.getCount();i++)
        {
            if (comboCategoriaMP.getItemAtPosition(i).toString().contentEquals(productoSeleccionado.getCategoria()))
            {
                comboCategoriaMP.setSelection(i);
            }
        }

        for (int i=0;i<comboProveedorMP.getCount();i++)
        {
            if (comboProveedorMP.getItemAtPosition(i).toString().contentEquals(provProductoSeleccionado.getNombre()))
            {
                comboProveedorMP.setSelection(i);
            }
        }

        for (int i=0;i<comboEstadoMP.getCount();i++)
        {
            if (comboEstadoMP.getItemAtPosition(i).toString().contentEquals(productoSeleccionado.getEstado()))
            {
                comboEstadoMP.setSelection(i);
            }
        }
    }

    public void cargarComboProveedor()
    {
        List<Proveedor> proveedores = proveedorDAO.obtenerTodosLosProveedoresPorEstado("Habilitado");
        ArrayList<String> datosComboProveedor = new ArrayList<>();
        if(!proveedores.isEmpty())
        {
            for (int i=0; i<proveedores.size(); i++)
            {
                datosComboProveedor.add(proveedores.get(i).getNombre());
            }
        }
        ArrayAdapter<String> adaptadorComboProveedor = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_item,datosComboProveedor);
        adaptadorComboProveedor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        comboProveedorMP.setAdapter(adaptadorComboProveedor);
    }

    public boolean verificarProductoExistente(String descripcion)
    {
        boolean productoExistente = false;
        for (int i=0; i<productos.size(); i++)
        {
            if (productos.get(i).getDescripcion().contentEquals(descripcion)) productoExistente = true;
        }
        return productoExistente;
    }

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
            productos = productoDAO.obtenerTodosLosProductos();

            int id = productoSeleccionado.getIdProducto();
            String descripcion = etDescripcionMP.getText().toString();
            String precioVenta = etPrecioMP.getText().toString();
            String costoCompra = etCostoMP.getText().toString();
            String stockProducto = etStockMP.getText().toString();
            String categoria = comboCategoriaMP.getSelectedItem().toString();
            String estado = comboEstadoMP.getSelectedItem().toString();
            Proveedor proveedor = proveedorDAO.obtenerProveedorPorNombre(comboProveedorMP.getSelectedItem().toString());
            int idProveedor = proveedor.getIdProveedor();

            if (descripcion.isEmpty() || precioVenta.isEmpty() || costoCompra.isEmpty() || stockProducto.isEmpty())
            {
                DialogoCamposVacios camposVacios = new DialogoCamposVacios();
                camposVacios.show(manager,"Error");
            }
            else
            {
                float precio = Float.parseFloat(precioVenta);
                float costo = Float.parseFloat(costoCompra);
                int stock = Integer.parseInt(stockProducto);

                if (verificarProductoExistente(descripcion) && !productoSeleccionado.getDescripcion().contentEquals(descripcion))
                {
                    DialogoRegistroExistente registroExistente = new DialogoRegistroExistente();
                    registroExistente.show(manager,"Error");
                }
                else
                {
                    Producto producto = new Producto(id,categoria,descripcion,precio,costo,stock,estado,idProveedor);
                    productoDAO.modificarProducto(producto);

                    Toast msj2 = Toast.makeText(getApplicationContext(),"Producto modificado",Toast.LENGTH_SHORT);
                    msj2.show();

                    String pantallaAnterior = getIntent().getStringExtra("pantallaAnterior");

                    if (pantallaAnterior.contentEquals("PantallaDetalleProducto"))
                    {
                        Intent irPantallaDetalleProducto = getIntent();
                        irPantallaDetalleProducto.putExtra("productoModificado", producto);
                        setResult(RESULT_OK,irPantallaDetalleProducto);
                    }

                    if (pantallaAnterior.contentEquals("PantallaProductos")) setResult(RESULT_OK);

                    finish();
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
