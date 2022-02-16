package com.example.tolan.adapters;

import com.bumptech.glide.Glide;
import com.example.tolan.ActivityAddLevel;
import com.example.tolan.R;
import com.example.tolan.models.ModelSublevel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdpSublevel extends RecyclerView.Adapter<AdpSublevel.ViewHolder>
        implements View.OnClickListener{

    private Context ccontext;
    private View.OnClickListener listener;
    private ArrayList<ModelSublevel> lista;
    Bundle bundle;

    public AdpSublevel(Context context, ArrayList<ModelSublevel> lista) {
        ccontext = context;
        this.lista = lista;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflr = LayoutInflater.from(ccontext);
        View view = inflr.inflate(R.layout.item_sublevel,null,false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelSublevel sublevel = lista.get(position);
        holder.txtid.setText(Integer.toString(sublevel.getId()));
        holder.txtnombre.setText(sublevel.getNombre());
        holder.txtdescripcion.setText(sublevel.getDescripcion());
        holder.txtnumactividades.setText(Integer.toString(sublevel.getNumactividades()));
        holder.txtPrioridad.setText(Integer.toString(sublevel.getPrioridad()));
        holder.checkBoxSubnivel.setEnabled(sublevel.getActivo());
        Glide.with(ccontext)
                .load(sublevel.getUrl())
                .into(holder.imgSubnivel);
        holder.editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notifyDataSetChanged();
                //Toast.makeText(ccontext,String.valueOf(level.getId()),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ccontext, ActivityAddLevel.class);
                bundle = new Bundle();
                bundle.putSerializable("sublevelSelected", sublevel);
                intent.putExtras(bundle);
                ccontext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    @Override
    public void onClick(View view) {
        if(listener!=null)
            listener.onClick(view);
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtid, txtnombre, txtPrioridad, txtdescripcion, txtnumactividades;
        private CheckBox checkBoxSubnivel;
        ImageView imgSubnivel;
        FloatingActionButton editar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtid = itemView.findViewById(R.id.txtId);
            txtnombre = itemView.findViewById(R.id.txtTitleSubnivel);
            txtdescripcion = itemView.findViewById(R.id.txtDescription);
            txtnumactividades = itemView.findViewById(R.id.txtNumAct);
            txtPrioridad = itemView.findViewById(R.id.txtPrioridad);
            checkBoxSubnivel = itemView.findViewById(R.id.checkBoxSubnivel);
            imgSubnivel = (ImageView) itemView.findViewById(R.id.imgSubnivel);
            editar = (FloatingActionButton) itemView.findViewById(R.id.editarN);
        }
    }
}
