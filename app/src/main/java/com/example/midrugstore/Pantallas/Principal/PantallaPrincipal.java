package com.example.midrugstore.Pantallas.Principal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.midrugstore.Pantallas.Compras.PantallaCompras;
import com.example.midrugstore.Pantallas.Ventas.PantallaVentas;
import com.example.midrugstore.Pantallas.Productos.PantallaProductos;
import com.example.midrugstore.Pantallas.Proveedores.PantallaProveedores;
import com.example.midrugstore.R;

public class PantallaPrincipal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_principal);
    }

    public void consultarVentas(View view)
    {
        Intent irPantallaVentas = new Intent(getApplicationContext(), PantallaVentas.class);
        startActivity(irPantallaVentas);
    }

    public void consultarCompras(View view)
    {
        Intent irPantallaCompras = new Intent(getApplicationContext(), PantallaCompras.class);
        startActivity(irPantallaCompras);
    }

    public void consultarProveedores(View view)
    {
        Intent irPantallaProveedores = new Intent(getApplicationContext(), PantallaProveedores.class);
        startActivity(irPantallaProveedores);
    }

    public void consultarProductos(View view)
    {
        Intent irPantallaProductos = new Intent(getApplicationContext(), PantallaProductos.class);
        startActivity(irPantallaProductos);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pantalla_principal,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.opSalir)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                finishAndRemoveTask(); // Finalizar la actividad y matar al proceso
            } else {
                finish(); // Finalizar la actividad
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
