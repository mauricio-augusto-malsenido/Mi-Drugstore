package com.example.midrugstore.Adaptadores;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.midrugstore.BaseDeDatos.ProductoDAO;
import com.example.midrugstore.Entidades.LineaPedidoCompra;
import com.example.midrugstore.R;

import java.text.DecimalFormat;
import java.util.List;

public class AdaptadorLineasPedidoNuevo extends BaseAdapter {

    private Context contexto;
    private List<LineaPedidoCompra> lista;
    private ProductoDAO productoDAO;

    static class ViewHolder
    {
        private TextView txtDescripcionProducto;
        private TextView txtCantidadPedida;
        private TextView txtCostoUnitario;
        private TextView txtSubtotal;

        ViewHolder(View view){
            txtDescripcionProducto = view.findViewById(R.id.txtDescripcionProducto);
            txtCantidadPedida = view.findViewById(R.id.txtCantidadPedida);
            txtCostoUnitario = view.findViewById(R.id.txtCostoUnitario);
            txtSubtotal = view.findViewById(R.id.txtSubtotal);
        }
    }

    public AdaptadorLineasPedidoNuevo(Context contexto, List<LineaPedidoCompra> lista) {
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
        return lista.get(position).getIdLineaPedidoCompra();
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View vista = convertView;

        if (vista == null)
        {
            LayoutInflater inflater = LayoutInflater.from(contexto);
            vista = inflater.inflate(R.layout.item_lista_lineas_nuevo_pedido,null);
            holder = new ViewHolder(vista);
            vista.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) vista.getTag();
        }

        LineaPedidoCompra objeto = lista.get(position);
        DecimalFormat formatoDecimal = new DecimalFormat("0.00");

        holder.txtDescripcionProducto.setText(productoDAO.obtenerProductoPorId(objeto.getIdProducto()).getDescripcion());
        holder.txtCantidadPedida.setText(String.valueOf(objeto.getCantidadPedida()));
        holder.txtCostoUnitario.setText(formatoDecimal.format(productoDAO.obtenerProductoPorId(objeto.getIdProducto()).getCostoCompra()));
        holder.txtSubtotal.setText(formatoDecimal.format(objeto.getSubtotal()));

        return vista;
    }
}
