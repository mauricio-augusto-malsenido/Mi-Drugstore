package com.example.midrugstore.Pantallas.Productos;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.midrugstore.BaseDeDatos.ProveedorDAO;
import com.example.midrugstore.Entidades.Producto;
import com.example.midrugstore.R;

import java.text.DecimalFormat;

public class PantallaDetalleProducto extends AppCompatActivity {

    TextView txtIdDP, txtCategoriaDP, txtDescripcionDP, txtPrecioDP, txtCostoDP, txtStockDP, txtProveedorDP, txtEstadoDP;
    Producto producto;
    Producto productoModificado;
    ProveedorDAO proveedorDAO;
    DecimalFormat formatoDecimal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_detalle_producto);

        txtIdDP = findViewById(R.id.txtIdDP);
        txtCategoriaDP = findViewById(R.id.txtCategoriaDP);
        txtDescripcionDP = findViewById(R.id.txtDescripcionDP);
        txtPrecioDP = findViewById(R.id.txtPrecioDP);
        txtCostoDP = findViewById(R.id.txtCostoDP);
        txtStockDP = findViewById(R.id.txtStockDP);
        txtProveedorDP = findViewById(R.id.txtProveedorDP);
        txtEstadoDP = findViewById(R.id.txtEstadoDP);

        proveedorDAO = new ProveedorDAO(getApplicationContext());

        producto = (Producto) getIntent().getExtras().getSerializable("productoSeleccionado");

        formatoDecimal = new DecimalFormat("0.00");

        txtIdDP.setText(String.valueOf(producto.getIdProducto()));
        txtCategoriaDP.setText(producto.getCategoria());
        txtDescripcionDP.setText(producto.getDescripcion());
        txtPrecioDP.setText(formatoDecimal.format(producto.getPrecioVenta()));
        txtCostoDP.setText(formatoDecimal.format(producto.getCostoCompra()));
        txtStockDP.setText(String.valueOf(producto.getStock()));
        txtEstadoDP.setText(producto.getEstado());
        txtProveedorDP.setText(proveedorDAO.obtenerProveedorPorId(producto.getIdProveedor()).getNombre());
    }

    @Override
    public void onBackPressed() {
        if (productoModificado != null) setResult(RESULT_OK);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detalle_registro,menu);
        String pantallaAnterior = getIntent().getStringExtra("pantallaAnterior");
        if (pantallaAnterior != null && pantallaAnterior.contentEquals("PantallaSeleccionarProducto"))
            menu.getItem(1).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.opAtras)
        {
            if (productoModificado != null) setResult(RESULT_OK);
            finish();
        }
        if (item.getItemId() == R.id.opEditar)
        {
            Intent irPantallaModificarProducto = new Intent(getApplicationContext(),PantallaModificarProducto.class);
            irPantallaModificarProducto.putExtra("productoSeleccionado",producto);
            irPantallaModificarProducto.putExtra("pantallaAnterior","PantallaDetalleProducto");
            startActivityForResult(irPantallaModificarProducto,100);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK)
        {
            productoModificado = (Producto) data.getExtras().getSerializable("productoModificado");
            txtCategoriaDP.setText(productoModificado.getCategoria());
            txtDescripcionDP.setText(productoModificado.getDescripcion());
            txtPrecioDP.setText(formatoDecimal.format(productoModificado.getPrecioVenta()));
            txtCostoDP.setText(formatoDecimal.format(productoModificado.getCostoCompra()));
            txtProveedorDP.setText(proveedorDAO.obtenerProveedorPorId(productoModificado.getIdProveedor()).getNombre());
            txtStockDP.setText(String.valueOf(productoModificado.getStock()));
            txtEstadoDP.setText(productoModificado.getEstado());
            producto = productoModificado;
        }
    }
}
