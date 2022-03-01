package com.example.tolan.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tolan.R;
import com.example.tolan.models.ModelContent;

import java.util.ArrayList;

public class AdpOptionIdentifyImg extends RecyclerView.Adapter<AdpOptionIdentifyImg.ViewHolder>
        implements View.OnClickListener {

    private Context ccontext;
    private View.OnClickListener listener;
    private ArrayList<ModelContent> lista;

    public AdpOptionIdentifyImg(Context context, ArrayList<ModelContent> lista) {
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
    public AdpOptionIdentifyImg.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflr = LayoutInflater.from(ccontext);
        View view = inflr.inflate(R.layout.item_option_identify_img,null,false);
        //view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdpOptionIdentifyImg.ViewHolder holder, int position) {
        try {
            ModelContent content = lista.get(position);
            Glide.with(ccontext)
                    .load(content.getMultimedia().getJSONObject(0).getString("url"))
                    .into(holder.imgOption);
            holder.cvOpcion.setId(position);
            holder.cvOpcion.setOnClickListener(this);
            holder.imgView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    notifyDataSetChanged();
                    Toast.makeText(ccontext,"Ver más multimedia",Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            String res = e.toString();
        }
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cvOpcion;
        ImageView imgOption, imgView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgOption = (ImageView) itemView.findViewById(R.id.imgOp);
            imgView = (ImageView) itemView.findViewById(R.id.imgView);
            cvOpcion = (CardView) itemView.findViewById(R.id.cvOpcion);
        }
    }
}
