package com.example.midrugstore.Adaptadores;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.midrugstore.BaseDeDatos.ProductoDAO;
import com.example.midrugstore.Entidades.LineaRemito;
import com.example.midrugstore.R;

import java.util.List;

public class AdaptadorLineasRemito extends BaseAdapter {

    private Context contexto;
    private List<LineaRemito> lista;
    private ProductoDAO productoDAO;

    static class ViewHolder
    {
        private TextView txtDescripcionProducto;
        private TextView txtCantidadRemito;
        private TextView txtCantidadRecibida;

        ViewHolder(View view){
            txtDescripcionProducto = view.findViewById(R.id.txtDescripcionProducto);
            txtCantidadRemito = view.findViewById(R.id.txtCantidadRemito);
            txtCantidadRecibida = view.findViewById(R.id.txtCantidadRecibida);
        }
    }

    public AdaptadorLineasRemito(Context contexto, List<LineaRemito> lista) {
        this.contexto = contexto;
        this.lista = lista;
        this.productoDAO = new ProductoDAO(contexto);
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
        return lista.get(position).getIdLineaRemito();
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View vista = convertView;

        if (vista == null)
        {
            LayoutInflater inflater = LayoutInflater.from(contexto);
            vista = inflater.inflate(R.layout.item_lista_lineas_remito,null);
            holder = new ViewHolder(vista);
            vista.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) vista.getTag();
        }

        LineaRemito objeto = lista.get(position);

        holder.txtDescripcionProducto.setText(productoDAO.obtenerProductoPorId(objeto.getIdProducto()).getDescripcion());
        holder.txtCantidadRemito.setText(String.valueOf(objeto.getCantidadRemito()));
        holder.txtCantidadRecibida.setText(String.valueOf(objeto.getCantidadRecibida()));

        return vista;
    }
}
