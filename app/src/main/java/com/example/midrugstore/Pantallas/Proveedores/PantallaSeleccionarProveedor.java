package com.example.midrugstore.Pantallas.Proveedores;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.midrugstore.Adaptadores.AdaptadorProveedores;
import com.example.midrugstore.BaseDeDatos.ProveedorDAO;
import com.example.midrugstore.Entidades.Proveedor;
import com.example.midrugstore.R;

import java.util.List;

public class PantallaSeleccionarProveedor extends AppCompatActivity {

    ListView listaProveedoresCompra;

    List<Proveedor> proveedores;
    Proveedor proveedorSeleccionado;

    ProveedorDAO proveedorDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_seleccionar_proveedor);

        listaProveedoresCompra = findViewById(R.id.listaProveedoresCompra);
        registerForContextMenu(listaProveedoresCompra);

        proveedorDAO = new ProveedorDAO(getApplicationContext());

        proveedores = proveedorDAO.obtenerTodosLosProveedoresPorEstado("Habilitado");

        AdaptadorProveedores adaptador = new AdaptadorProveedores(getApplicationContext(), proveedores);
        listaProveedoresCompra.setAdapter(adaptador);

        listaProveedoresCompra.setOnItemClickListener(clickCortoEnProveedor);
        listaProveedoresCompra.setOnItemLongClickListener(clickLargoEnProveedor);
    }

    AdapterView.OnItemClickListener clickCortoEnProveedor = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            proveedorSeleccionado = (Proveedor) parent.getItemAtPosition(position);
            Intent irPantallaNuevaCompra = getIntent();
            irPantallaNuevaCompra.putExtra("proveedorSeleccionado", proveedorSeleccionado);
            setResult(RESULT_OK,irPantallaNuevaCompra);
            finish();
        }
    };

    AdapterView.OnItemLongClickListener clickLargoEnProveedor = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            proveedorSeleccionado = (Proveedor) listaProveedoresCompra.getItemAtPosition(position);
            return false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pantalla_secundaria,menu);
        menu.getItem(1).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.opAtras) finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        getMenuInflater().inflate(R.menu.menu_contextual_lista_seleccion,menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.opVerDetalles)
        {
            Intent irPantallaDetalleProveedor = new Intent(getApplicationContext(), PantallaDetalleProveedor.class);
            irPantallaDetalleProveedor.putExtra("proveedorSeleccionado",proveedorSeleccionado);
            irPantallaDetalleProveedor.putExtra("pantallaAnterior","PantallaSeleccionarProveedor");
            startActivity(irPantallaDetalleProveedor);
        }
        return super.onContextItemSelected(item);
    }
}
