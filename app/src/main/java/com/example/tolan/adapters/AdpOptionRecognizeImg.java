package com.example.tolan.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tolan.R;
import com.example.tolan.models.ModelContent;

import java.util.ArrayList;
import java.util.List;

public class AdpOptionRecognizeImg extends RecyclerView.Adapter<AdpOptionRecognizeImg.ViewHolder>
        implements View.OnClickListener {

    private Context ccontext;
    private View.OnClickListener listener;
    private ArrayList<ModelContent> lista;

    public AdpOptionRecognizeImg(Context context, ArrayList<ModelContent> lista) {
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
    public AdpOptionRecognizeImg.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflr = LayoutInflater.from(ccontext);
        View view = inflr.inflate(R.layout.item_option_recognize_figure,null,false);
        //view.setOnClickListener(this);
        return new AdpOptionRecognizeImg.ViewHolder(view);
    }

    /*public View getView(int position, View convertView, ViewGroup parent) {
        //Funciona como DataBinding
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View item = inflater.inflate(R.layout.item_option_recognize_figure, null);
        TextView lblOp = (TextView)item.findViewById(R.id.lblOp);
        lblOp.setText("Opción " + (position + 1) + ".- ");
        TextView txtOp = (TextView)item.findViewById(R.id.txtOp);
        txtOp.setText(getItem(position).getDescripcion());
        return(item);
    }*/

    @Override
    public void onBindViewHolder(@NonNull AdpOptionRecognizeImg.ViewHolder holder, int position) {
        try {
            ModelContent content = lista.get(position);
            holder.lblOp.setText("Opción " + (position + 1) + ".- ");
            holder.txtOp.setText(content.getDescripcion());
            holder.rlOption.setId(position);
            holder.rlOption.setOnClickListener(this);
        }catch (Exception e){
            String res = e.toString();
        }
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout rlOption;
        TextView lblOp, txtOp;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rlOption = (RelativeLayout) itemView.findViewById(R.id.rlOption);
            lblOp = (TextView) itemView.findViewById(R.id.lblOp);
            txtOp = (TextView) itemView.findViewById(R.id.txtOp);
        }
    }

}
