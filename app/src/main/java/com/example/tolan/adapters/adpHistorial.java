package com.example.tolan.adapters;

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
import com.example.tolan.models.ModelGroup;
import com.example.tolan.models.ModelHistorial_Cabz;

import java.util.ArrayList;

public class adpHistorial extends RecyclerView.Adapter<adpHistorial.MyViewHolder>
        implements View.OnClickListener {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_LIST = 0;

    public adpHistorial(ArrayList<ModelHistorial_Cabz> mLista) {
        this.mLista = mLista;
    }

    private int selectedPos = RecyclerView.NO_POSITION;
    private View.OnClickListener listener;
    private ArrayList<ModelHistorial_Cabz> mLista;

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onClick(view);
        }
    }

    @NonNull
    @Override
    public adpHistorial.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_historial, null, false);
        view.setOnClickListener(this);
        return new MyViewHolder(view);
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull adpHistorial.MyViewHolder holder, int position) {
        try {
            holder.lblNombre.setText("Estudiante: " + mLista.get(position).getEstudiante());
            holder.lblActiv.setText(mLista.get(position).getActividades());
            holder.lblActvComp.setText("Actividades Completas: " + mLista.get(position).getHistorial().size());
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

        public TextView lblNombre, lblActiv, lblActvComp;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            lblNombre = (TextView) itemView.findViewById(R.id.txtNombre);
            lblActiv = (TextView) itemView.findViewById(R.id.txtDescripcion);
            lblActvComp = (TextView) itemView.findViewById(R.id.NumActivCompl);

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
