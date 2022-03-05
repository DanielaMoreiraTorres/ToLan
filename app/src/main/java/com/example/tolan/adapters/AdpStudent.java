package com.example.tolan.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tolan.R;
import com.example.tolan.models.ModelStudent;

import java.util.ArrayList;

public class AdpStudent extends RecyclerView.Adapter<AdpStudent.MyViewHolder>
        implements View.OnClickListener{
    private static final int TYPE_HEADER=0;
    private static final int TYPE_LIST=0;


    private View.OnClickListener listener;

    private ArrayList<ModelStudent> mLista;

    public AdpStudent(ArrayList<ModelStudent> lista) {
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
        TextView txtNombre, txtFecha;
        Button btnActivo;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre= (TextView) itemView.findViewById(R.id.txtNombreEstudiante);
            txtFecha= (TextView) itemView.findViewById(R.id.fecha);
            btnActivo= (Button) itemView.findViewById(R.id.arrowBtn);

       }
    }

    @NonNull
    @Override
    public AdpStudent.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_group_student, null, false);
        view.setOnClickListener(this);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdpStudent.MyViewHolder holder, int position) {
        try {

            holder.txtNombre.setText(mLista.get(position).getEstudiante());
            holder.txtFecha.setText(mLista.get(position).getFecha());
            holder.btnActivo.setText(mLista.get(position).getActivoS());


        }catch (Exception e){
           String res=e.toString();
        }
    }

    @Override
    public int getItemCount() {
        return mLista.size();
    }
}
