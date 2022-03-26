package com.example.tolan.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tolan.R;
import com.example.tolan.clases.ClssConvertTextToSpeech;
import com.example.tolan.dialogs.Diag_Frg_AyudaEspecial;
import com.example.tolan.models.ModelContent;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdpOptionDragAndDropImg extends RecyclerView.Adapter<AdpOptionDragAndDropImg.ViewHolder>
        implements View.OnClickListener, View.OnLongClickListener {

    private Context ccontext;
    private View.OnClickListener listener;
    private View.OnLongClickListener longClick;
    private ArrayList<ModelContent> lista;
    private ArrayList<ModelContent> inicial;
    private ArrayList<ModelContent> respuestas;
    Map<String, List<String>> map_MultimediaExtra = new HashMap<>();
    ArrayList<String> listRutasMultimedia, listItemsMultimedia;

    public AdpOptionDragAndDropImg(Context context, ArrayList<ModelContent> lista, ArrayList<ModelContent> inicial, ArrayList<ModelContent> respuestas) {
        ccontext = context;
        this.lista = lista;
        this.inicial = inicial;
        this.respuestas = respuestas;
        listItemsMultimedia = new ArrayList<>();
        listRutasMultimedia = new ArrayList<>();
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
    public void onBindViewHolder(@NonNull AdpOptionDragAndDropImg.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        try {
            ModelContent content = inicial.get(position);
            String urlInicial = "";
            List<String> lstUrls_Ayuda= new ArrayList<>();
            int contMulti = content.getMultimedia().length();
            if(respuestas.size() == 1)
                holder.imgOp.setTag(content.getDescripcion().trim());
            else{
                if(content.getRespuesta())
                    holder.imgOp.setTag("respuesta");
                else
                    holder.imgOp.setTag(content.getDescripcion().trim());
            }
            for (int i = 0; i < contMulti; i++) {
                if (content.getMultimedia().getJSONObject(i).getBoolean("inicial")) {
                    urlInicial = content.getMultimedia().getJSONObject(i).getString("url");
                    Glide.with(ccontext)
                            .load(urlInicial)
                            .into(holder.imgOp);
                } else {
                    lstUrls_Ayuda.add(content.getMultimedia().getJSONObject(i).getString("url"));
                }
            }
            map_MultimediaExtra.put(urlInicial, lstUrls_Ayuda);
            holder.imgView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    notifyDataSetChanged();
                    //Toast.makeText(ccontext,"Ver más multimedia",Toast.LENGTH_SHORT).show();
                    try {
                        /*int opcselec = rcvOptions.getChildAdapterPosition(view);*/
                        if(contMulti > 0) {
                            String img = content.getMultimedia().getJSONObject(0).getString("url");
                            String descripcion = content.getMultimedia().getJSONObject(0).getString("descripcion");
                            FragmentManager manager = ((AppCompatActivity) ccontext).getSupportFragmentManager();
                            ClssConvertTextToSpeech.getIntancia(ccontext).reproduce("Ayuda");
                            Diag_Frg_AyudaEspecial diag_frg_ayudaEspecial = new Diag_Frg_AyudaEspecial(img, descripcion, map_MultimediaExtra.get(img), "Arrastrar y soltar imagen");
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
        return inicial.size();
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
