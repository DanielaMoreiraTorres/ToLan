package com.example.tolan.fragments;

import static android.content.ClipData.newPlainText;

import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.example.tolan.R;
import com.example.tolan.adapters.AdpEnunciado;
import com.example.tolan.adapters.AdpOptionArrastrarSoltarTxt;
import com.example.tolan.adapters.AdpOptionIdentifyTxt;
import com.example.tolan.clases.ClssNavegacionActividades;
import com.example.tolan.models.ModelContent;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONArray;
import org.json.JSONException;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Frg_ArrastrarSoltar#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Frg_ArrastrarSoltar extends Fragment implements View.OnClickListener {

    JSONArray jsonActivities;
    NavController navController;
    private ListView lstLista;
    private RecyclerView rcvOptions;
    private View state;
    private TextView txtResponse;
    ModelContent modelContent;
    private JSONArray contenido;
    List<ModelContent> modelContentsEnun;
    ArrayList<ModelContent> modelContentsOp;
    ArrayList<ModelContent> respuestas;
    ArrayList<ModelContent> resp;
    LinearLayout destino, dest, salida;
    private AdpEnunciado adpEnunciado;
    private AdpOptionArrastrarSoltarTxt adpOptionArrastrarSoltarTxt;
    ModelContent opSelected = new ModelContent();
    private RequestQueue requestQueue;
    private String url = "https://db-bartolucci.herokuapp.com/historial/completeActividad";
    Boolean respuesta = false;

    View option;
    String uno;
    String dos;
    View textView;
    ViewGroup cardView, linearLayout;

    public Frg_ArrastrarSoltar() {
        // Required empty public constructor
    }

    public static Frg_ArrastrarSoltar newInstance(String param1, String param2) {
        Frg_ArrastrarSoltar fragment = new Frg_ArrastrarSoltar();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_arrastrar_soltar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            String lst_Activities = getArguments().getString("activities");
            jsonActivities = new JSONArray(lst_Activities);
            state = view.findViewById(R.id.state);
            state.setVisibility(View.GONE);
            txtResponse = view.findViewById(R.id.txtResponse);
            txtResponse.setVisibility(View.GONE);
            destino = view.findViewById(R.id.destino);
            lstLista = view.findViewById(R.id.lstEnunciado);
            rcvOptions = (RecyclerView) view.findViewById(R.id.rcvOption);
            rcvOptions.setLayoutManager(new GridLayoutManager(getContext(),2));
            contenido = jsonActivities.getJSONObject(0).getJSONArray("contenido");
            modelContentsEnun = new ArrayList<>();
            modelContentsOp = new ArrayList<>();
            respuestas = new ArrayList<>();
            resp = new ArrayList<>();
            modelContent = new ModelContent();
            modelContent.MapContenido(contenido,modelContentsEnun,modelContentsOp,respuestas);
            if(modelContentsEnun.size() > 0 & modelContentsOp.size() >0) {
                if (respuestas.size() == 1)
                    destino.setTag(respuestas.get(0).getDescripcion().trim());
                adpEnunciado = new AdpEnunciado(getContext(), modelContentsEnun);
                lstLista.setAdapter(adpEnunciado);
                adpOptionArrastrarSoltarTxt = new AdpOptionArrastrarSoltarTxt(getContext(), modelContentsOp);
                rcvOptions.setAdapter(adpOptionArrastrarSoltarTxt);
                adpOptionArrastrarSoltarTxt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int opcselec = rcvOptions.getChildAdapterPosition(view);
                        opSelected = modelContentsOp.get(opcselec);
                        Toast.makeText(getContext(), opSelected.getDescripcion(), Toast.LENGTH_SHORT).show();
                    }
                });
                adpOptionArrastrarSoltarTxt.setOnLongClickListener(v -> LongClickListener(v));
                //adpOptionArrastrarSoltarTxt.setOnDragListener((v, event) -> DragListener(v, event));
                destino.setOnDragListener((v, event) -> DragListener(v, event));
            }
            else {
                Toast.makeText(getContext(), "La actividad no tiene contenido", Toast.LENGTH_SHORT).show();
                view.findViewById(R.id.btn_comprobar_actividades).setVisibility(View.VISIBLE);
                view.findViewById(R.id.btn_comprobar_actividades).setOnClickListener(v -> Navegacion(v));
            }
            /*option = view.findViewById(R.id.option);
            option.setOnLongClickListener(v -> LongClickListener(v));
            salida = view.findViewById(R.id.salida);
            salida.setOnDragListener((v, event) -> DragListener(v, event));*/
        } catch (JSONException e) {
            e.printStackTrace();
        }
        view.findViewById(R.id.btn_comprobar_actividades).setOnClickListener(this);
    }

    private void Navegacion(View v){
        navController = Navigation.findNavController(v);
        //Eliminamos el item por el cual nos redirecccionamos aca
        jsonActivities.remove(0);
        ClssNavegacionActividades clssNavegacionActividades= new ClssNavegacionActividades(navController,jsonActivities,v);
        clssNavegacionActividades.navegar();
    }

    private boolean LongClickListener(View v){
        LinearLayout linearLayout = (LinearLayout) v;
        MaterialCardView cardView = (MaterialCardView) linearLayout.getChildAt(0);
        View textView = cardView.getChildAt(0);
        ClipData data = newPlainText((CharSequence) textView.getTag(), "");
        View.DragShadowBuilder myShadow = new View.DragShadowBuilder(v);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            v.startDragAndDrop(data,myShadow,textView,0);
        else
            v.startDrag(data,myShadow,textView,0);
        v.setVisibility(View.INVISIBLE);
        return true;
    }

    private boolean DragListener(View v, DragEvent event){
        switch (event.getAction()){
            case DragEvent.ACTION_DRAG_STARTED:
                event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN);
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                v.invalidate();
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                v.invalidate();
                break;
            case DragEvent.ACTION_DRAG_LOCATION:
                break;
            case DragEvent.ACTION_DRAG_ENDED:
                v.invalidate();
                textView = (View) event.getLocalState();
                cardView = (ViewGroup) textView.getParent();
                linearLayout = (ViewGroup) cardView.getParent();
                linearLayout.setVisibility(View.VISIBLE);
                break;
            case DragEvent.ACTION_DROP:
                v.invalidate();
                uno = v.getTag().toString();
                dos = (String) event.getClipDescription().getLabel();
                textView = (View) event.getLocalState();
                cardView = (ViewGroup) textView.getParent();
                linearLayout = (ViewGroup) cardView.getParent();
                if(uno != null & dos != null){
                    if(v.getTag().toString().equals(event.getClipDescription().getLabel())){
                        Toast.makeText(getContext(),"Correcto",Toast.LENGTH_SHORT).show();
                        //v.setBackgroundColor(Color.GREEN);
                        cardView.removeView(textView);
                        dest = (LinearLayout) v;
                        dest.addView(textView);
                        textView.setVisibility(View.VISIBLE);
                    }
                    else {
                        Toast.makeText(getContext(),"Incorrecto",Toast.LENGTH_SHORT).show();
                        linearLayout.setVisibility(View.VISIBLE);
                    }
                }
                else
                    linearLayout.setVisibility(View.VISIBLE);
                break;
            default: break;
        }
        return true;
    }

    /*float x, y, dx, dy;
    private boolean TouchListener(MotionEvent event){
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(option);
            option.startDrag(data, shadowBuilder, option, 0);
            option.setVisibility(View.INVISIBLE);
            return true;
        } else {
            return false;
        }
    }*/

    @Override
    public void onClick(View v) {

        navController = Navigation.findNavController(v);
        //Eliminamos el item por el cual nos redirecccionamos acá
        jsonActivities.remove(0);
        ClssNavegacionActividades clssNavegacionActividades= new ClssNavegacionActividades(navController,jsonActivities,v);
        clssNavegacionActividades.navegar();
    }
}