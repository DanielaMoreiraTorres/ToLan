package com.example.tolan.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tolan.R;
import com.example.tolan.models.ModelContent;

import java.util.ArrayList;
import java.util.List;

public class AdpStatement extends RecyclerView.Adapter<AdpStatement.ViewHolder> {

    private Context ccontext;
    private List<ModelContent> lista;

    //constructor
    public AdpStatement(Context context, List<ModelContent> lista) {
        ccontext = context;
        this.lista = lista;
    }

    @NonNull
    @Override
    public AdpStatement.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflr = LayoutInflater.from(ccontext);
        View view = inflr.inflate(R.layout.item_enunciado,null,false);
        //view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdpStatement.ViewHolder holder, int position) {
        try {
            ModelContent content = lista.get(position);
            holder.lblTitulo.setText(content.getDescripcion());
        }catch (Exception e){
            String res = e.toString();
        }
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView lblTitulo;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lblTitulo = (TextView) itemView.findViewById(R.id.lblTitulo);
        }
    }

    /*public View getView(int position, View convertView, ViewGroup parent) {
        //Funciona como DataBinding
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View item = inflater.inflate(R.layout.item_enunciado, null);
        TextView lblTitulo = (TextView)item.findViewById(R.id.lblTitulo);
        int c = getCount();
        String n = getItem(position).getDescripcion();
        lblTitulo.setText(getItem(position).getDescripcion());
        return(item);
    }*/
}
