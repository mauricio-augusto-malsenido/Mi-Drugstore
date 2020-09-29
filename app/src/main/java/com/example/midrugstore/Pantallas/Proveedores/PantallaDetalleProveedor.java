package com.example.midrugstore.Pantallas.Proveedores;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.midrugstore.Entidades.Proveedor;
import com.example.midrugstore.R;

public class PantallaDetalleProveedor extends AppCompatActivity {

    TextView txtIdDProv, txtCuitDProv, txtNombreDProv, txtDireccionDProv, txtTelefonoDProv, txtEstadoDProv;
    Proveedor proveedor;
    Proveedor proveedorModificado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_detalle_proveedor);

        txtIdDProv = findViewById(R.id.txtIdDProv);
        txtCuitDProv = findViewById(R.id.txtCuitDProv);
        txtNombreDProv = findViewById(R.id.txtNombreDProv);
        txtDireccionDProv = findViewById(R.id.txtDireccionDProv);
        txtTelefonoDProv = findViewById(R.id.txtTelefonoDProv);
        txtEstadoDProv = findViewById(R.id.txtEstadoDProv);

        proveedor = (Proveedor) getIntent().getExtras().getSerializable("proveedorSeleccionado");

        txtIdDProv.setText(String.valueOf(proveedor.getIdProveedor()));
        txtCuitDProv.setText(proveedor.getCuit());
        txtNombreDProv.setText(proveedor.getNombre());
        txtDireccionDProv.setText(proveedor.getDireccion());
        txtTelefonoDProv.setText(proveedor.getTelefono());
        txtEstadoDProv.setText(proveedor.getEstado());
    }

    @Override
    public void onBackPressed() {
        if (proveedorModificado != null) setResult(RESULT_OK);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detalle_registro,menu);
        String pantallaAnterior = getIntent().getStringExtra("pantallaAnterior");
        if (pantallaAnterior != null && pantallaAnterior.contentEquals("PantallaSeleccionarProveedor"))
            menu.getItem(1).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.opAtras)
        {
            if (proveedorModificado != null) setResult(RESULT_OK);
            finish();
        }
        if (item.getItemId() == R.id.opEditar)
        {
            Intent irPantallaModificarProveedor = new Intent(getApplicationContext(),PantallaModificarProveedor.class);
            irPantallaModificarProveedor.putExtra("proveedorSeleccionado",proveedor);
            irPantallaModificarProveedor.putExtra("pantallaAnterior","PantallaDetalleProveedor");
            startActivityForResult(irPantallaModificarProveedor,100);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK)
        {
            proveedorModificado = (Proveedor) data.getExtras().getSerializable("proveedorModificado");
            txtCuitDProv.setText(proveedorModificado.getCuit());
            txtNombreDProv.setText(proveedorModificado.getNombre());
            txtDireccionDProv.setText(proveedorModificado.getDireccion());
            txtTelefonoDProv.setText(proveedorModificado.getTelefono());
            txtEstadoDProv.setText(proveedorModificado.getEstado());
            proveedor = proveedorModificado;
        }
    }
}
