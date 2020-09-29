package com.example.midrugstore.Pantallas.Principal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.midrugstore.R;

public class PantallaInicio extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_inicio);
        getSupportActionBar().hide();

        Thread splashThread = new Thread(){
            @Override
            public void run() {
                try{
                    sleep(1500);
                    Intent irPantallaPrincipal = new Intent(getApplicationContext(),PantallaPrincipal.class);
                    startActivity(irPantallaPrincipal);
                    finish();
                }
                catch (Exception ex) {
                    Log.e("MiDrugstoreLog",ex.getMessage(),ex);
                }
            }
        };
        splashThread.start();
    }
}
