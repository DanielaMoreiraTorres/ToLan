package com.example.tolan.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tolan.R;
import com.example.tolan.clases.ClssConvertirTextoAVoz;
import com.example.tolan.fragments.FrgAddLevel;
import com.example.tolan.models.ModelLevel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AdpLevel extends RecyclerView.Adapter<AdpLevel.ViewHolder>
        implements View.OnClickListener{

    Fragment fragment;
    private Context ccontext;
    private View.OnClickListener listener;
    private ArrayList<ModelLevel> lista;
    private ArrayList<ModelLevel> listaOriginal;
    Bundle bundle;

    public AdpLevel(Context context, ArrayList<ModelLevel> lista) {
        ccontext = context;
        this.lista = lista;
        listaOriginal = new ArrayList<>();
        listaOriginal.addAll(lista);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflr = LayoutInflater.from(ccontext);
        View view = inflr.inflate(R.layout.item_level,null,false);
        //view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    public void filtrado(String txtBuscar){
        int longitud = txtBuscar.length();
        if(longitud == 0){
            lista.clear();
            lista.addAll(listaOriginal);
        }
        else {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                List<ModelLevel> collection = lista.stream().filter(i -> i.getNombre().toLowerCase()
                        .contains(txtBuscar.toLowerCase()) || i.getDescripcion().toLowerCase()
                        .contains(txtBuscar.toLowerCase())).collect(Collectors.toList());
                lista.clear();
                lista.addAll(collection);
            }
            else {
                for (ModelLevel l: listaOriginal){
                    if(l.getNombre().toLowerCase().contains(txtBuscar.toLowerCase()) ||
                            l.getDescripcion().toLowerCase().contains(txtBuscar.toLowerCase())){
                        lista.add(l);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            ModelLevel level = lista.get(position);
            holder.txtIdNivel.setText(Integer.toString(level.getId()));
            holder.txtTitleNivel.setText(level.getNombre());
            holder.txtDescription.setText(level.getDescripcion());
            holder.txtPrioridad.setText(Integer.toString(level.getPrioridad()));
            holder.checkBoxNivel.setChecked(level.getActivo());
            holder.cardViewNiveles.setId(position);
            holder.cardViewNiveles.setOnClickListener(this);
            Glide.with(ccontext)
                    .load(level.getUrl())
                    .into(holder.imgNivel);
            holder.editar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    notifyDataSetChanged();
                    //Toast.makeText(ccontext,String.valueOf(level.getId()),Toast.LENGTH_SHORT).show();
                    ClssConvertirTextoAVoz.getIntancia(ccontext).reproduce("Editar nivel");
                    fragment = new FrgAddLevel();
                    bundle = new Bundle();
                    bundle.putSerializable("levelSelected", level);
                    fragment.setArguments(bundle);
                    FragmentManager manager = ((AppCompatActivity)ccontext).getSupportFragmentManager();
                    manager.beginTransaction().replace(R.id.content, fragment).addToBackStack(null).commit();
                    /*Intent intent = new Intent(ccontext, ActivityAddLevel.class);
                    intent.putExtras(bundle);
                    ccontext.startActivity(intent);*/
                }
            });
        }catch (Exception e){
            String res=e.toString();
        }
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    @Override
    public void onClick(View view) {
        if(listener!=null)
            listener.onClick(view);
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardViewNiveles;
        TextView txtIdNivel, txtTitleNivel, txtDescription, txtPrioridad;
        CheckBox checkBoxNivel;
        ImageView imgNivel;
        FloatingActionButton editar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardViewNiveles = (CardView) itemView.findViewById(R.id.cardViewNiveles);
            txtIdNivel = (TextView) itemView.findViewById(R.id.txtId);
            txtTitleNivel = (TextView) itemView.findViewById(R.id.txtTitleNivel);
            txtDescription = (TextView) itemView.findViewById(R.id.txtDescription);
            txtPrioridad = (TextView) itemView.findViewById(R.id.txtPrioridad);
            checkBoxNivel = (CheckBox) itemView.findViewById(R.id.checkBoxNivel);
            imgNivel = (ImageView) itemView.findViewById(R.id.imgNivel);
            editar = (FloatingActionButton) itemView.findViewById(R.id.editarN);
        }
    }
}
