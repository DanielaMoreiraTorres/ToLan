package com.example.tolan.adapters;

import android.content.Context;
import android.text.Html;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tolan.R;
import com.example.tolan.models.ModelEstudent;
import com.example.tolan.models.ModelGroup;

import java.util.ArrayList;

public class adpGrupo_Admin extends RecyclerView.Adapter<adpGrupo_Admin.MyViewHolder>
        implements View.OnClickListener {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_LIST = 0;


    public adpGrupo_Admin(ArrayList<ModelGroup> mLista) {
        this.mLista = mLista;
    }

    private int selectedPos = RecyclerView.NO_POSITION;
    private View.OnClickListener listener;
    private ArrayList<ModelGroup> mLista;




    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onClick(view);
        }
    }


    @NonNull
    @Override
    public adpGrupo_Admin.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_group_admin, null, false);

        view.setOnClickListener(this);
        return new MyViewHolder(view);
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }


    @Override
    public void onBindViewHolder(@NonNull adpGrupo_Admin.MyViewHolder holder, int position) {
        try {
            holder.lblNombreDocente.setText("Docente Encargado: "+mLista.get(position).getDocente());
            holder.lblDescripcion.setText(mLista.get(position).getEstudiantesNombre());
            holder.lblFecha.setText("Alumnos Totales: "+mLista.get(position).getEstudiantesT());
        } catch (Exception e) {

        }
    }

    @Override
    public int getItemCount() {
        return mLista.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout expandableView;
        public Button arrowBtn;
        public CardView cardView;

        public TextView lblNombreDocente, lblDescripcion, lblFecha;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            lblNombreDocente = (TextView) itemView.findViewById(R.id.txtNombreDocente);
            lblFecha = (TextView) itemView.findViewById(R.id.fecha);
            lblDescripcion = (TextView) itemView.findViewById(R.id.txtDescripcion);


            expandableView = itemView.findViewById(R.id.expandableView);
            arrowBtn = itemView.findViewById(R.id.arrowBtn);
            cardView = itemView.findViewById(R.id.cardView);

            arrowBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (expandableView.getVisibility() == View.GONE) {
                        TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                        expandableView.setVisibility(View.VISIBLE);
                    } else {
                        TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                        expandableView.setVisibility(View.GONE);
                        arrowBtn.setBackgroundResource(R.drawable.expand);
                    }
                }
            });
        }
    }
}
