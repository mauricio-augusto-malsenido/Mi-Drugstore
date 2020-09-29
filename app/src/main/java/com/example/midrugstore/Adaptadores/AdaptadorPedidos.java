package com.example.midrugstore.Adaptadores;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.midrugstore.Entidades.PedidoCompra;
import com.example.midrugstore.R;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AdaptadorPedidos extends BaseAdapter {

    private Context contexto;
    private List<PedidoCompra> lista;

    static class ViewHolder
    {
        private TextView txtID;
        private TextView txtFechaCompra;
        private TextView txtHoraCompra;
        private TextView txtTotal;

        ViewHolder(View view){
            txtID = view.findViewById(R.id.txtID);
            txtFechaCompra = view.findViewById(R.id.txtFechaCompra);
            txtHoraCompra = view.findViewById(R.id.txtHoraCompra);
            txtTotal = view.findViewById(R.id.txtTotal);
        }
    }

    public AdaptadorPedidos(Context contexto, List<PedidoCompra> lista) {
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
        return lista.get(position).getIdPedidoCompra();
    }

    @SuppressLint({"SimpleDateFormat", "InflateParams"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View vista = convertView;

        if (vista == null)
        {
            LayoutInflater inflater = LayoutInflater.from(contexto);
            vista = inflater.inflate(R.layout.item_lista_pedidos,null);
            holder = new ViewHolder(vista);
            vista.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) vista.getTag();
        }

        PedidoCompra objeto = lista.get(position);
        DecimalFormat formatoDecimal = new DecimalFormat("0.00");
        DateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat formatoFechaSQL = new SimpleDateFormat("yyyy-MM-dd");

        Date fecha = null;
        try {
            fecha = formatoFechaSQL.parse(objeto.getFechaPedido());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.txtID.setText(String.valueOf(objeto.getIdPedidoCompra()));
        if (fecha != null) holder.txtFechaCompra.setText(formatoFecha.format(fecha));
        holder.txtHoraCompra.setText(objeto.getHoraPedido());
        holder.txtTotal.setText(formatoDecimal.format(objeto.getTotal()));

        return vista;
    }
}
