package com.example.midrugstore.Adaptadores;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.midrugstore.BaseDeDatos.ProductoDAO;
import com.example.midrugstore.Entidades.LineaVenta;
import com.example.midrugstore.R;

import java.text.DecimalFormat;
import java.util.List;

public class AdaptadorLineasVenta extends BaseAdapter {

    private Context contexto;
    private List<LineaVenta> lista;
    private ProductoDAO productoDAO;

    static class ViewHolder
    {
        private TextView txtDescripcionProducto;
        private TextView txtCantidad;
        private TextView txtPrecioUnitario;
        private TextView txtSubtotal;

        ViewHolder(View view){
            txtDescripcionProducto = view.findViewById(R.id.txtDescripcionProducto);
            txtCantidad = view.findViewById(R.id.txtCantidad);
            txtPrecioUnitario = view.findViewById(R.id.txtPrecioUnitario);
            txtSubtotal = view.findViewById(R.id.txtSubtotal);
        }
    }

    public AdaptadorLineasVenta(Context contexto, List<LineaVenta> lista) {
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
        return lista.get(position).getIdLineaVenta();
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View vista = convertView;

        if (vista == null)
        {
            LayoutInflater inflater = LayoutInflater.from(contexto);
            vista = inflater.inflate(R.layout.item_lista_lineas_venta,null);
            holder = new ViewHolder(vista);
            vista.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) vista.getTag();
        }

        LineaVenta objeto = lista.get(position);
        DecimalFormat formatoDecimal = new DecimalFormat("0.00");

        holder.txtDescripcionProducto.setText(productoDAO.obtenerProductoPorId(objeto.getIdProducto()).getDescripcion());
        holder.txtCantidad.setText(String.valueOf(objeto.getCantidad()));
        holder.txtPrecioUnitario.setText(formatoDecimal.format(productoDAO.obtenerProductoPorId(objeto.getIdProducto()).getPrecioVenta()));
        holder.txtSubtotal.setText(formatoDecimal.format(objeto.getSubtotal()));

        return vista;
    }
}
