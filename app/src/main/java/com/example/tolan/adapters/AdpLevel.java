package com.example.tolan.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tolan.ActivityAddLevel;
import com.example.tolan.ActivityLevel;
import com.example.tolan.R;
import com.example.tolan.models.ModelLevel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;

public class AdpLevel extends RecyclerView.Adapter<AdpLevel.ViewHolder>
        implements View.OnClickListener{

    private Context ccontext;
    private View.OnClickListener listener;
    private ArrayList<ModelLevel> lista;
    Bundle bundle;

    public AdpLevel(Context context, ArrayList<ModelLevel> lista) {
        ccontext = context;
        this.lista = lista;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflr = LayoutInflater.from(ccontext);
        View view = inflr.inflate(R.layout.item_level,null,false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            ModelLevel level = lista.get(position);
            holder.txtIdNivel.setText(Integer.toString(level.getId()));
            holder.txtTitleNivel.setText(level.getNombre());
            holder.txtDescription.setText(level.getDescripcion());
            holder.txtPrioridad.setText(Integer.toString(level.getPrioridad()));
            holder.checkBoxNivel.setEnabled(level.getActivo());
            Glide.with(ccontext)
                    .load(level.getUrl())
                    .into(holder.imgNivel);
            holder.editar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    notifyDataSetChanged();
                    //Toast.makeText(ccontext,String.valueOf(level.getId()),Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ccontext, ActivityAddLevel.class);
                    bundle = new Bundle();
                    bundle.putSerializable("levelSelected", level);
                    intent.putExtras(bundle);
                    ccontext.startActivity(intent);
                }
            });
        }catch (Exception e){
            String res=e.toString();
        }
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtIdNivel, txtTitleNivel, txtDescription, txtPrioridad;
        CheckBox checkBoxNivel;
        ImageView imgNivel;
        FloatingActionButton editar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtIdNivel = (TextView) itemView.findViewById(R.id.txtId);
            txtTitleNivel = (TextView) itemView.findViewById(R.id.txtTitleNivel);
            txtDescription = (TextView) itemView.findViewById(R.id.txtDescription);
            txtPrioridad = (TextView) itemView.findViewById(R.id.txtPrioridad);
            checkBoxNivel = (CheckBox) itemView.findViewById(R.id.checkBoxNivel);
            imgNivel = (ImageView) itemView.findViewById(R.id.imgNivel);
            editar = (FloatingActionButton) itemView.findViewById(R.id.editarN);
        }
    }
}
