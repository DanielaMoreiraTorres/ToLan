package com.example.tolan.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tolan.R;
import com.example.tolan.fragments.FrgAddActividad;
import com.example.tolan.models.ModelActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class AdpActividad extends RecyclerView.Adapter<AdpActividad.ViewHolder> {


    // creating a variable for array list and context.
    private ArrayList<ModelActivity> listaActividades;
    private Fragment fragment;

    public AdpActividad(ArrayList<ModelActivity> listaActividades, Fragment fragment) {
        this.listaActividades = listaActividades;
        this.fragment = fragment;
    }

    // method for filtering our recyclerview items.
    public void filterList(ArrayList<ModelActivity> filterllist) {
        // below line is to add our filtered
        // list in our course array list.
        listaActividades = filterllist;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AdpActividad.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        try {
            //LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            //View viewd = layoutInflater.inflate(R.layout.ly_item_actividades, parent, false);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ly_item_actividades, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdpActividad.ViewHolder holder, int position) {
        // setting data to our views of recycler view.
        ModelActivity modal = listaActividades.get(position);


        holder.txt_TitleActividad.setText(modal.getNombre());
        holder.txt_DescripctionActividad.setText(modal.getDescripcion());
        holder.txt_idActividad.setText(String.valueOf(modal.getId()));
        holder.checkedTextView.setChecked(modal.getActivo());
        holder.txt_SubnivelActividad.setText(modal.getSubnivel());

        //Glide.with(context).load(modal.getImage()).into(holder.imgCIV_Actividad);


        String data = "";
        switch (modal.getRecompensavalor()) {
            case 1:
                data = "Muy Baja";
                break;
            case 2:
                data = "Baja";
                break;
            case 4:
                data = "Media";
                break;
            case 6:
                data = "Alta";
                break;
            case 8:
                data = "Muy Alta";
                break;
            case 10:
                data = "Extrema";
                break;

        }
        holder.txt_DificultadActividad.setText(data);

        switch (modal.getTipo()) {
            case "AC":

                data = "Actividad Común";
                break;
            case "EV":
                //holder.txt_tipoActividad.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                //holder.txt_tipoActividad.setTextColor(context.getResources().getColor(R.color.white));
                holder.rly.setBackgroundColor(Color.parseColor("#3CE9E9"));
                holder.ryl_superior.setBackgroundColor(Color.parseColor("#3CE9E9"));


                data = "Evaluación";
                break;

        }

        if (modal.getActivo()) {
            holder.checkedTextView.setTextColor(Color.parseColor("#96B774"));

        } else {
            holder.checkedTextView.setTextColor(Color.parseColor("#E74C3C"));
            holder.checkedTextView.setText("Inactivo");

        }
        holder.txt_tipoActividad.setText(data);

        //holder.txt_DificultadActividad.setRotation(90);;


        holder.fabtneditarActividad.setOnClickListener(v -> {
            FragmentManager manager = ((AppCompatActivity) fragment.getContext()).getSupportFragmentManager();
            Fragment frg = new FrgAddActividad("edit", listaActividades.get(position));
            manager.beginTransaction().replace(R.id.content, frg).addToBackStack(null).commit();

            //fragment.getFragmentManager().beginTransaction().replace(R.id.content, frg).addToBackStack(null).commit();

        });
    }

    @Override
    public int getItemCount() {
        return listaActividades.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        // creating variables for our views.
        private TextView txt_TitleActividad,
                txt_DescripctionActividad,
                txt_DificultadActividad,
                txt_tipoActividad,
                txt_idActividad, txt_SubnivelActividad;
        CheckBox checkedTextView;
        //CircleImageView imgCIV_Actividad;

        CardView cardview_activity;
        RelativeLayout rly, ryl_superior;
        FloatingActionButton fabtneditarActividad;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_TitleActividad = itemView.findViewById(R.id.txt_TitleActividad);
            txt_DescripctionActividad = itemView.findViewById(R.id.txt_DescripctionActividad);
            checkedTextView = itemView.findViewById(R.id.checkedTextView);
            //imgCIV_Actividad = itemView.findViewById(R.id.imgciv_actividad);
            txt_DificultadActividad = itemView.findViewById(R.id.txt_dificultadActividad);
            txt_tipoActividad = itemView.findViewById(R.id.txt_tipoActividad);
            txt_idActividad = itemView.findViewById(R.id.txt_idActividad);
            txt_SubnivelActividad = itemView.findViewById(R.id.txt_SubnivelActividad);

            cardview_activity = itemView.findViewById(R.id.cardview_activity);
            rly = itemView.findViewById(R.id.rly);
            ryl_superior = itemView.findViewById(R.id.ryl_superior);
            fabtneditarActividad = itemView.findViewById(R.id.fabtneditarActividad);
        }
    }
}
