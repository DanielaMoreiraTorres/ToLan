package com.example.tolan.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tolan.R;
import com.example.tolan.models.ModelEstudent;
import com.example.tolan.models.ModelHistorial;

import java.util.ArrayList;

public class adpHist_Estudent extends RecyclerView.Adapter<adpHist_Estudent.MyViewHolder>
        implements View.OnClickListener{
    private static final int TYPE_HEADER=0;
    private static final int TYPE_LIST=0;


    private View.OnClickListener listener;

    private ArrayList<ModelHistorial> mLista;

    Context context;
    public adpHist_Estudent(Context context,ArrayList<ModelHistorial> lista) {
        context = context;
        mLista=lista;
    }

    @Override
    public void onClick(View view) {
        if(listener!=null)
        {
            listener.onClick(view);
        }
    }
    public void setOnClickListener(View.OnClickListener listener){
        this.listener=listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtActividad, txtRecomp;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtActividad= (TextView) itemView.findViewById(R.id.txtActividad);
            txtRecomp= (TextView) itemView.findViewById(R.id.txtRecompensa);

       }
    }

    @NonNull
    @Override
    public adpHist_Estudent.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_historial_estd, null, false);
        view.setOnClickListener(this);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull adpHist_Estudent.MyViewHolder holder, int position) {
        try {
            holder.txtActividad.setText(mLista.get(position).getNombre());
            holder.txtRecomp.setText(mLista.get(position).getRecompensa());
        }catch (Exception e){
           String res=e.toString();
        }
    }

    @Override
    public int getItemCount() {
        return mLista.size();
    }
}
