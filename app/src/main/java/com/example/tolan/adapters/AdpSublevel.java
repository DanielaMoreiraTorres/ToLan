package com.example.tolan.adapters;

import com.bumptech.glide.Glide;
import com.example.tolan.R;
import com.example.tolan.clases.ClssConvertTextToSpeech;
import com.example.tolan.fragments.FrgAddSublevel;
import com.example.tolan.models.ModelSublevel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AdpSublevel extends RecyclerView.Adapter<AdpSublevel.ViewHolder>
        implements View.OnClickListener{

    private Fragment fragment;
    private Context ccontext;
    private View.OnClickListener listener;
    private ArrayList<ModelSublevel> lista;
    private ArrayList<ModelSublevel> listaOriginal;
    Bundle bundle;

    public AdpSublevel(Context context, ArrayList<ModelSublevel> lista) {
        ccontext = context;
        this.lista = lista;
        listaOriginal = new ArrayList<>();
        listaOriginal.addAll(lista);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflr = LayoutInflater.from(ccontext);
        View view = inflr.inflate(R.layout.item_sublevel,null,false);
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
                List<ModelSublevel> collection = lista.stream().filter(i -> i.getNombre().toLowerCase()
                        .contains(txtBuscar.toLowerCase()) || i.getDescripcion().toLowerCase()
                        .contains(txtBuscar.toLowerCase())).collect(Collectors.toList());
                lista.clear();
                lista.addAll(collection);
            }
            else {
                for (ModelSublevel l: listaOriginal){
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
        ModelSublevel sublevel = lista.get(position);
        holder.txtid.setText(Integer.toString(sublevel.getId()));
        holder.txtnombre.setText(sublevel.getNombre());
        holder.txtdescripcion.setText(sublevel.getDescripcion());
        holder.txtnumactividades.setText(Integer.toString(sublevel.getNumactividades()));
        holder.txtPrioridad.setText(Integer.toString(sublevel.getPrioridad()));
        holder.checkBoxSubnivel.setEnabled(sublevel.getActivo());
        Glide.with(ccontext)
                .load(sublevel.getUrl())
                .into(holder.imgSubnivel);
        holder.cardViewSubniveles.setId(position);
        holder.cardViewSubniveles.setOnClickListener(this);
        holder.editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notifyDataSetChanged();
                //Toast.makeText(ccontext,String.valueOf(level.getId()),Toast.LENGTH_SHORT).show();
                ClssConvertTextToSpeech.getIntancia(ccontext).reproduce("Editar nivel");
                fragment = new FrgAddSublevel();
                bundle = new Bundle();
                bundle.putSerializable("sublevelSelected", sublevel);
                fragment.setArguments(bundle);
                FragmentManager manager = ((AppCompatActivity)ccontext).getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.content, fragment).addToBackStack(null).commit();
            }
        });
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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardViewSubniveles;
        private TextView txtid, txtnombre, txtPrioridad, txtdescripcion, txtnumactividades;
        private CheckBox checkBoxSubnivel;
        ImageView imgSubnivel;
        FloatingActionButton editar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardViewSubniveles = (CardView) itemView.findViewById(R.id.cardViewSubniveles);
            txtid = itemView.findViewById(R.id.txtId);
            txtnombre = itemView.findViewById(R.id.txtTitleSubnivel);
            txtdescripcion = itemView.findViewById(R.id.txtDescription);
            txtnumactividades = itemView.findViewById(R.id.txtNumAct);
            txtPrioridad = itemView.findViewById(R.id.txtPrioridad);
            checkBoxSubnivel = itemView.findViewById(R.id.checkBoxSubnivel);
            imgSubnivel = (ImageView) itemView.findViewById(R.id.imgSubnivel);
            editar = (FloatingActionButton) itemView.findViewById(R.id.editarN);
        }
    }
}
