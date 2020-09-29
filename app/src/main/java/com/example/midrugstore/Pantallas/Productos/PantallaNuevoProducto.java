package com.example.midrugstore.Pantallas.Productos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

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

public class PantallaNuevoProducto extends AppCompatActivity {

    EditText etDescripcionNP, etPrecioNP, etCostoNP, etStockNP;
    Spinner comboCategoriaNP, comboProveedorNP;
    FragmentManager manager;
    List<Producto> productos;
    ProductoDAO productoDAO;
    ProveedorDAO proveedorDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_nuevo_producto);

        etDescripcionNP = findViewById(R.id.etDescripcionNP);
        etPrecioNP = findViewById(R.id.etPrecioNP);
        etCostoNP = findViewById(R.id.etCostoNP);
        etStockNP = findViewById(R.id.etStockNP);
        comboCategoriaNP = findViewById(R.id.comboCategoriaNP);
        comboProveedorNP = findViewById(R.id.comboProveedorNP);

        manager = getSupportFragmentManager();

        productoDAO = new ProductoDAO(getApplicationContext());
        proveedorDAO = new ProveedorDAO(getApplicationContext());

        cargarComboProveedor();
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
        comboProveedorNP.setAdapter(adaptadorComboProveedor);
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

            int id;
            if(productos.size() > 0) id = productos.get(productos.size() - 1).getIdProducto() + 1;
            else id = 1;

            String descripcion = etDescripcionNP.getText().toString();
            String precioVenta = etPrecioNP.getText().toString();
            String costoCompra = etCostoNP.getText().toString();
            String stockProducto = etStockNP.getText().toString();
            String categoria = comboCategoriaNP.getSelectedItem().toString();
            String estado = "Habilitado";
            if (descripcion.isEmpty() || precioVenta.isEmpty() || costoCompra.isEmpty() || stockProducto.isEmpty() || comboProveedorNP.getSelectedItem() == null)
            {
                DialogoCamposVacios camposVacios = new DialogoCamposVacios();
                camposVacios.show(manager,"Error");
            }
            else
            {
                Proveedor proveedor = proveedorDAO.obtenerProveedorPorNombre(comboProveedorNP.getSelectedItem().toString());
                int idProveedor = proveedor.getIdProveedor();
                float precio = Float.parseFloat(precioVenta);
                float costo = Float.parseFloat(costoCompra);
                int stock = Integer.parseInt(stockProducto);

                if (verificarProductoExistente(descripcion))
                {
                    DialogoRegistroExistente registroExistente = new DialogoRegistroExistente();
                    registroExistente.show(manager,"Error");
                }
                else
                {
                    Producto producto = new Producto(id,categoria,descripcion,precio,costo,stock,estado,idProveedor);
                    productoDAO.crearProducto(producto);

                    Toast msj2 = Toast.makeText(getApplicationContext(),"Producto registrado",Toast.LENGTH_SHORT);
                    msj2.show();

                    setResult(RESULT_OK);
                    finish();
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
