package com.example.tolan.adapters;

import android.content.Context;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tolan.R;
import com.example.tolan.models.ModelContent;

import java.util.ArrayList;

public class AdpOptionArrastrarSoltarTxt extends RecyclerView.Adapter<AdpOptionArrastrarSoltarTxt.ViewHolder>
        implements View.OnClickListener, View.OnLongClickListener {

    private Context ccontext;
    private View.OnClickListener listener;
    private View.OnLongClickListener longClick;
    private ArrayList<ModelContent> lista;

    public AdpOptionArrastrarSoltarTxt(Context context, ArrayList<ModelContent> lista) {
        ccontext = context;
        this.lista = lista;
    }

    @Override
    public void onClick(View view) {
        if(listener!=null)
            listener.onClick(view);
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public AdpOptionArrastrarSoltarTxt.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflr = LayoutInflater.from(ccontext);
        View view = inflr.inflate(R.layout.item_option_arrastrar_soltar_txt,null,false);
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdpOptionArrastrarSoltarTxt.ViewHolder holder, int position) {
        try {
            ModelContent content = lista.get(position);
            holder.txtOp.setText(content.getDescripcion().trim());
            holder.txtOp.setTag(content.getDescripcion().trim());
        }catch (Exception e){
            String res = e.toString();
        }
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    @Override
    public boolean onLongClick(View view) {
        if(longClick != null)
            longClick.onLongClick(view);
        return true;
    }

    public void setOnLongClickListener(View.OnLongClickListener longClick){
        this.longClick = longClick;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtOp;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOp = (TextView) itemView.findViewById(R.id.txtOp);
        }
    }
}
