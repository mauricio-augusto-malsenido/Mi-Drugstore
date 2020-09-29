package com.example.midrugstore.Adaptadores;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.midrugstore.Entidades.Remito;
import com.example.midrugstore.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AdaptadorRemitos extends BaseAdapter {

    private Context contexto;
    private List<Remito> lista;

    static class ViewHolder
    {
        private TextView txtNroRemito;
        private TextView txtFechaRemito;
        private TextView txtFechaRecepcion;

        ViewHolder(View view){
            txtNroRemito = view.findViewById(R.id.txtNroRemito);
            txtFechaRemito = view.findViewById(R.id.txtFechaRemito);
            txtFechaRecepcion = view.findViewById(R.id.txtFechaRecepcion);
        }
    }

    public AdaptadorRemitos(Context contexto, List<Remito> lista) {
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
        return lista.get(position).getIdRemito();
    }

    @SuppressLint({"SimpleDateFormat", "InflateParams"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View vista = convertView;

        if (vista == null)
        {
            LayoutInflater inflater = LayoutInflater.from(contexto);
            vista = inflater.inflate(R.layout.item_lista_remitos,null);
            holder = new ViewHolder(vista);
            vista.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) vista.getTag();
        }

        Remito objeto = lista.get(position);
        DateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat formatoFechaSQL = new SimpleDateFormat("yyyy-MM-dd");

        Date fechaRemito = null;
        Date fechaRecepcion = null;
        try {
            fechaRemito = formatoFechaSQL.parse(objeto.getFechaRemito());
            fechaRecepcion = formatoFechaSQL.parse(objeto.getFechaRecepcion());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.txtNroRemito.setText(String.valueOf(objeto.getNroRemito()));
        if (fechaRemito != null) holder.txtFechaRemito.setText(formatoFecha.format(fechaRemito));
        if (fechaRecepcion != null) holder.txtFechaRecepcion.setText(formatoFecha.format(fechaRecepcion));

        return vista;
    }
}
