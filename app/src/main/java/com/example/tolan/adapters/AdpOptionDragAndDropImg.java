package com.example.tolan.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tolan.R;
import com.example.tolan.models.ModelContent;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class AdpOptionDragAndDropImg extends RecyclerView.Adapter<AdpOptionDragAndDropImg.ViewHolder>
        implements View.OnClickListener, View.OnLongClickListener {

    private Context ccontext;
    private View.OnClickListener listener;
    private View.OnLongClickListener longClick;
    private ArrayList<ModelContent> lista;
    private ArrayList<ModelContent> respuestas;

    public AdpOptionDragAndDropImg(Context context, ArrayList<ModelContent> lista, ArrayList<ModelContent> respuestas) {
        ccontext = context;
        this.lista = lista;
        this.respuestas = respuestas;
    }

    @Override
    public void onClick(View view) {
        if(listener!=null)
            listener.onClick(view);
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
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

    @NonNull
    @Override
    public AdpOptionDragAndDropImg.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflr = LayoutInflater.from(ccontext);
        View view = inflr.inflate(R.layout.item_option_drag_and_drop_img,null,false);
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdpOptionDragAndDropImg.ViewHolder holder, int position) {
        try {
            ModelContent content = lista.get(position);
            if(respuestas.size() == 1)
                holder.imgOp.setTag(content.getDescripcion().trim());
            else{
                if(content.getRespuesta())
                    holder.imgOp.setTag("respuesta");
                else
                    holder.imgOp.setTag(content.getDescripcion().trim());
            }
            Glide.with(ccontext)
                    .load(content.getMultimedia().getJSONObject(0).getString("url"))
                    .into(holder.imgOp);
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
        ImageView imgView, imgOp;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgView = (ImageView) itemView.findViewById(R.id.imgView);
            cvOpcion = (CardView) itemView.findViewById(R.id.cvOpcion);
            imgOp = (ImageView) itemView.findViewById(R.id.imgOp);
        }
    }
}
