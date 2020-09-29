package com.example.midrugstore.Adaptadores;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.midrugstore.Entidades.Proveedor;
import com.example.midrugstore.R;

import java.util.List;

public class AdaptadorProveedores extends BaseAdapter {

    private Context contexto;
    private List<Proveedor> lista;

    static class ViewHolder
    {
        private TextView txtNombre;
        private TextView txtCuit;

        ViewHolder(View view){
            txtNombre = view.findViewById(R.id.txtNombre);
            txtCuit = view.findViewById(R.id.txtCuit);
        }
    }

    public AdaptadorProveedores(Context contexto, List<Proveedor> lista) {
        this.contexto = contexto;
        this.lista = lista;
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int position) {
        return lista.get(position);
    }

    @Override
    public long getItemId(int position) {
        return lista.get(position).getIdProveedor();
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View vista = convertView;

        if (vista == null)
        {
            LayoutInflater inflater = LayoutInflater.from(contexto);
            vista = inflater.inflate(R.layout.item_lista_proveedores,null);
            holder = new ViewHolder(vista);
            vista.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) vista.getTag();
        }

        Proveedor objeto = lista.get(position);

        holder.txtNombre.setText(objeto.getNombre());
        holder.txtCuit.setText(objeto.getCuit());

        return vista;
    }
}
