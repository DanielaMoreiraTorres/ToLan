package com.example.tolan.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tolan.R;
import com.example.tolan.clases.ClssConvertTextToSpeech;
import com.example.tolan.dialogs.Diag_Frg_AyudaEspecial;
import com.example.tolan.models.ModelContent;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdpOptionIdentifyImg extends RecyclerView.Adapter<AdpOptionIdentifyImg.ViewHolder>
        implements View.OnClickListener {

    private Context ccontext;
    private View.OnClickListener listener;
    private ArrayList<ModelContent> lista;
    Map<String, List<String>> map_MultimediaExtra = new HashMap<>();
    ArrayList<String> listRutasMultimedia, listItemsMultimedia;

    public AdpOptionIdentifyImg(Context context, ArrayList<ModelContent> lista, ArrayList<String> listRutasMultimedia,
                                ArrayList<String> listItemsMultimedia, ArrayList<ModelContent> respuestas) {
        ccontext = context;
        this.lista = lista;
        this.listRutasMultimedia = listRutasMultimedia;
        this.listItemsMultimedia = listItemsMultimedia;
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
    public void onBindViewHolder(@NonNull AdpOptionIdentifyImg.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        try {
            ModelContent content = lista.get(position);
            String urlInicial = "";
            List<String> lstUrls_Ayuda= new ArrayList<>();
            int contMulti = content.getMultimedia().length();
            for (int i = 0; i < contMulti; i++) {
                if(content.getMultimedia().getJSONObject(i).getBoolean("inicial")) {
                    urlInicial = content.getMultimedia().getJSONObject(i).getString("url");
                    Glide.with(ccontext)
                            .load(urlInicial)
                            .into(holder.imgOption);
                } else {
                    lstUrls_Ayuda.add(content.getMultimedia().getJSONObject(i).getString("url"));
                }
            }
            map_MultimediaExtra.put(urlInicial, lstUrls_Ayuda);
            holder.cvOpcion.setId(position);
            holder.cvOpcion.setOnClickListener(this);
            holder.imgView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    notifyDataSetChanged();
                    //Toast.makeText(ccontext,"Ver más multimedia",Toast.LENGTH_SHORT).show();
                    try {
                        if(contMulti > 0) {
                            String img = listRutasMultimedia.get(position);
                            String descripcion = listItemsMultimedia.get(position);
                            //String img = content.getMultimedia().getJSONObject(0).getString("url");
                            //String descripcion = content.getMultimedia().getJSONObject(0).getString("descripcion");
                            FragmentManager manager = ((AppCompatActivity) ccontext).getSupportFragmentManager();
                            ClssConvertTextToSpeech.getIntancia(ccontext).reproduce("Ayuda");
                            Diag_Frg_AyudaEspecial diag_frg_ayudaEspecial = new Diag_Frg_AyudaEspecial(img, descripcion, map_MultimediaExtra.get(img), "Identificar entre imágenes");
                            diag_frg_ayudaEspecial.show(manager, "Ayuda");
                        }
                    } catch (Exception e) {}
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
