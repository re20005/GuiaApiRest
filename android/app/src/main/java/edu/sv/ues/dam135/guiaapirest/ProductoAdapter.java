package edu.sv.ues.dam135.guiaapirest;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.sv.ues.dam135.guiaapirest.models.Producto;

/**
 * Muestra la lista de productos en un RecyclerView. Un toque sobre el
 * producto lo edita y el botón de la derecha lo elimina.
 */
public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder> {

    public interface Acciones {
        void alEditar(Producto producto);

        void alEliminar(Producto producto);
    }

    private final List<Producto> productos = new ArrayList<>();
    private final Acciones acciones;

    public ProductoAdapter(Acciones acciones) {
        this.acciones = acciones;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setProductos(List<Producto> nuevos) {
        productos.clear();
        if (nuevos != null) {
            productos.addAll(nuevos);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup padre, int tipo) {
        View vista = LayoutInflater.from(padre.getContext())
                .inflate(R.layout.item_producto, padre, false);
        return new ProductoViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int posicion) {
        Producto producto = productos.get(posicion);
        holder.tvNombre.setText(producto.getName());
        holder.tvCodigo.setText(holder.itemView.getContext()
                .getString(R.string.producto_codigo, producto.getCode()));
        holder.tvEstado.setText(producto.isStatus()
                ? R.string.producto_activo : R.string.producto_inactivo);
        holder.tvEstado.setTextColor(ContextCompat.getColor(holder.itemView.getContext(),
                producto.isStatus() ? R.color.activo : R.color.inactivo));

        holder.itemView.setOnClickListener(v -> acciones.alEditar(producto));
        holder.buttonEliminar.setOnClickListener(v -> acciones.alEliminar(producto));
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    static class ProductoViewHolder extends RecyclerView.ViewHolder {
        final TextView tvNombre;
        final TextView tvCodigo;
        final TextView tvEstado;
        final View buttonEliminar;

        ProductoViewHolder(@NonNull View vista) {
            super(vista);
            tvNombre = vista.findViewById(R.id.tvNombre);
            tvCodigo = vista.findViewById(R.id.tvCodigo);
            tvEstado = vista.findViewById(R.id.tvEstado);
            buttonEliminar = vista.findViewById(R.id.buttonEliminar);
        }
    }
}
