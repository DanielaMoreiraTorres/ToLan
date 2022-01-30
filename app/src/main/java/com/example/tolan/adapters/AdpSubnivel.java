package com.example.tolan.adapters;

import com.example.tolan.R;
import com.example.tolan.models.ModelSubnivel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdpSubnivel extends RecyclerView.Adapter<AdpSubnivel.RecyclerHolder> {
    private List<ModelSubnivel> items;

    public AdpSubnivel(List<ModelSubnivel> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sublevel, parent, false);
        return new RecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerHolder holder, int position) {
        ModelSubnivel item = items.get(position);
        holder.txtidnivel.setText(item.getIdnivel());
        holder.txtnombre.setText(item.getNombre());
        holder.txtdescripcion.setText(item.getDescripcion());
        holder.txtnumactividades.setText(item.getNumactividades());
            holder.btactivo.setText("Activo");
    }



    @Override
    public int getItemCount() {
        return items.size();
    }
    public static class RecyclerHolder extends RecyclerView.ViewHolder {
        private TextView txtidnivel;
        private TextView txtnombre;
        private TextView txtdescripcion;
        private TextView txtnumactividades;
        private Button btactivo;

        public RecyclerHolder(@NonNull View itemView) {
            super(itemView);
            txtidnivel = itemView.findViewById(R.id.txtID);
            txtnombre = itemView.findViewById(R.id.txtNombre);
            txtdescripcion = itemView.findViewById(R.id.txtDescrip);
            txtnumactividades = itemView.findViewById(R.id.txtNumAct);
            btactivo = itemView.findViewById(R.id.btEstado);
        }
    }
}
