package com.example.midrugstore.Pantallas.Proveedores;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.midrugstore.BaseDeDatos.ProveedorDAO;
import com.example.midrugstore.Entidades.Proveedor;
import com.example.midrugstore.Pantallas.Dialogos.DialogoCamposVacios;
import com.example.midrugstore.Pantallas.Dialogos.DialogoCancelarOperacion;
import com.example.midrugstore.Pantallas.Dialogos.DialogoCuitInvalido;
import com.example.midrugstore.Pantallas.Dialogos.DialogoRegistroExistente;
import com.example.midrugstore.R;

import java.util.List;

public class PantallaModificarProveedor extends AppCompatActivity {

    EditText etCuitMProv, etNombreMProv, etDireccionMProv, etTelefonoMProv;
    Spinner comboEstadoMProv;
    FragmentManager manager;
    List<Proveedor> proveedores;
    Proveedor proveedorSeleccionado;
    ProveedorDAO proveedorDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_modificar_proveedor);

        etCuitMProv = findViewById(R.id.etCuitMProv);
        etNombreMProv = findViewById(R.id.etNombreMProv);
        etDireccionMProv = findViewById(R.id.etDireccionMProv);
        etTelefonoMProv = findViewById(R.id.etTelefonoMProv);
        comboEstadoMProv = findViewById(R.id.comboEstadoMProv);

        manager = getSupportFragmentManager();

        proveedorDAO = new ProveedorDAO(getApplicationContext());

        proveedorSeleccionado = (Proveedor) getIntent().getExtras().getSerializable("proveedorSeleccionado");

        etCuitMProv.setText(proveedorSeleccionado.getCuit());
        etNombreMProv.setText(proveedorSeleccionado.getNombre());
        etDireccionMProv.setText(proveedorSeleccionado.getDireccion());
        etTelefonoMProv.setText(proveedorSeleccionado.getTelefono());

        for (int i=0;i<comboEstadoMProv.getCount();i++)
        {
            if (comboEstadoMProv.getItemAtPosition(i).toString().contentEquals(proveedorSeleccionado.getEstado()))
            {
                comboEstadoMProv.setSelection(i);
            }
        }

        etCuitMProv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String cuit = etCuitMProv.getText().toString();
                if (cantidadDeGuiones(cuit) < 2 && before < count)
                {
                    StringBuilder sb = new StringBuilder(cuit);
                    if (cuit.length() == 3 && cantidadDeGuiones(cuit) == 0)
                    {
                        sb.insert(2,'-');
                        cuit = sb.toString();
                        etCuitMProv.setText(sb.toString());
                        etCuitMProv.setSelection(4);
                    }
                    else if (cuit.length() == 4 && cantidadDeGuiones(cuit) == 1) etCuitMProv.setSelection(4);
                    if (cuit.length() == 12)
                    {
                        if (cuit.charAt(2) != '-')
                        {
                            sb.insert(2,'-');
                            cuit = sb.toString();
                            etCuitMProv.setText(sb.toString());
                            etCuitMProv.setSelection(start + 2);
                        }
                        if (cuit.charAt(11) != '-')
                        {
                            sb.insert(11,'-');
                            etCuitMProv.setText(sb.toString());
                            etCuitMProv.setSelection(13);
                        }
                    }
                }
                if (cuit.length() == 12 && cantidadDeGuiones(cuit) < 2 && before > count)
                {
                    StringBuilder sb = new StringBuilder(cuit);
                    if (cuit.charAt(2) != '-')
                    {
                        sb.insert(2,'-');
                        cuit = sb.toString();
                        etCuitMProv.setText(sb.toString());
                        etCuitMProv.setSelection(2);
                    }
                    if (cuit.charAt(11) != '-')
                    {
                        sb.insert(11,'-');
                        etCuitMProv.setText(sb.toString());
                        etCuitMProv.setSelection(11);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public boolean verificarProveedorExistentePorCuit(String cuit)
    {
        boolean proveedorExistente = false;
        for (int i=0; i<proveedores.size(); i++)
        {
            if (proveedores.get(i).getCuit().contentEquals(cuit))
                proveedorExistente = true;
        }
        return proveedorExistente;
    }

    public boolean verificarProveedorExistentePorNombre(String nombre)
    {
        boolean proveedorExistente = false;
        for (int i=0; i<proveedores.size(); i++)
        {

            if (proveedores.get(i).getNombre().contentEquals(nombre))
                proveedorExistente = true;
        }
        return proveedorExistente;
    }

    public int cantidadDeGuiones(String s)
    {
        int contador = 0;
        for (int i=0;i<s.length();i++)
        {
            if (s.charAt(i) == '-') contador = contador + 1;
        }
        return contador;
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
            proveedores = proveedorDAO.obtenerTodosLosProveedores();

            int id = proveedorSeleccionado.getIdProveedor();
            String cuit = etCuitMProv.getText().toString();
            String nombre = etNombreMProv.getText().toString();
            String direccion = etDireccionMProv.getText().toString();
            String telefono = etTelefonoMProv.getText().toString();
            String estado = comboEstadoMProv.getSelectedItem().toString();

            if (cuit.isEmpty() || nombre.isEmpty() || direccion.isEmpty())
            {
                DialogoCamposVacios camposVacios = new DialogoCamposVacios();
                camposVacios.show(manager,"Error");
            }
            else
            {
                if (cuit.length() < 13 || cuit.charAt(2) != '-' || cuit.charAt(11) != '-' || cantidadDeGuiones(cuit) != 2)
                {
                    DialogoCuitInvalido cuitInvalido = new DialogoCuitInvalido();
                    cuitInvalido.show(manager,"Error");
                }
                else
                {
                    if (verificarProveedorExistentePorCuit(cuit) && !proveedorSeleccionado.getCuit().contentEquals(cuit))
                    {
                        DialogoRegistroExistente registroExistente = new DialogoRegistroExistente();
                        registroExistente.show(manager,"Error");
                    }
                    else
                    {
                        if (verificarProveedorExistentePorNombre(nombre) && !proveedorSeleccionado.getNombre().contentEquals(nombre))
                        {
                            DialogoRegistroExistente registroExistente = new DialogoRegistroExistente();
                            registroExistente.show(manager,"Error");
                        }
                        else
                        {
                            Proveedor proveedor = new Proveedor(id,cuit,nombre,direccion,telefono,estado);
                            proveedorDAO.modificarProveedor(proveedor);

                            Toast msj2 = Toast.makeText(getApplicationContext(),"Proveedor modificado",Toast.LENGTH_SHORT);
                            msj2.show();

                            String pantallaAnterior = getIntent().getStringExtra("pantallaAnterior");

                            if (pantallaAnterior.contentEquals("PantallaDetalleProveedor"))
                            {
                                Intent irPantallaDetalleProveedor = getIntent();
                                irPantallaDetalleProveedor.putExtra("proveedorModificado", proveedor);
                                setResult(RESULT_OK,irPantallaDetalleProveedor);
                            }

                            if (pantallaAnterior.contentEquals("PantallaProveedores")) setResult(RESULT_OK);

                            finish();
                        }
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
