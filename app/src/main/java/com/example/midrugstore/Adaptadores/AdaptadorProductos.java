package com.example.midrugstore.Adaptadores;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.midrugstore.Entidades.Producto;
import com.example.midrugstore.R;

import java.util.List;

public class AdaptadorProductos extends BaseAdapter {

    private Context contexto;
    private List<Producto> lista;

    static class ViewHolder
    {
        private TextView txtID;
        private TextView txtDescripcion;
        private TextView txtStock;

        ViewHolder(View view){
            txtID = view.findViewById(R.id.txtID);
            txtDescripcion = view.findViewById(R.id.txtDescripcion);
            txtStock = view.findViewById(R.id.txtStock);
        }
    }

    public AdaptadorProductos(Context contexto, List<Producto> lista) {
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
        return lista.get(position).getIdProducto();
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View vista = convertView;

        if (vista == null)
        {
            LayoutInflater inflater = LayoutInflater.from(contexto);
            vista = inflater.inflate(R.layout.item_lista_productos,null);
            holder = new ViewHolder(vista);
            vista.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) vista.getTag();
        }

        Producto objeto = lista.get(position);

        holder.txtID.setText(String.valueOf(objeto.getIdProducto()));
        holder.txtDescripcion.setText(objeto.getDescripcion());
        holder.txtStock.setText(String.valueOf(objeto.getStock()));

        return vista;
    }
}
