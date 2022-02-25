package com.example.tolan.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.tolan.R;
import com.example.tolan.adapters.AdpEnunciado;
import com.example.tolan.adapters.AdpOptionReconocerImg;
import com.example.tolan.clases.ClssNavegacionActividades;
import com.example.tolan.clases.ClssStaticGrupo;
import com.example.tolan.models.ModelContent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Frg_ReconocerFiguras extends Fragment implements AdapterView.OnItemClickListener {

    JSONArray jsonActivities;
    NavController navController;
    private ListView lstLista;
    private ListView lstOptions;
    private Button btn;
    private ImageView img;
    private View state;
    private TextView txtResponse;
    private JSONArray contenido;
    List<ModelContent> modelContentsEnun;
    ModelContent imgEnunciado;
    ArrayList<ModelContent> modelContentsOp;
    List<Integer> lstIds;
    ArrayList<ModelContent> respuestas;
    //ArrayList<ModelContent> resp;
    private AdpEnunciado adpEnunciado;
    private AdpOptionReconocerImg adpOptionReconocerImg;
    ModelContent opSelected = new ModelContent();
    private RequestQueue requestQueue;
    private String url = "https://db-bartolucci.herokuapp.com/historial/completeActividad";
    Boolean respuesta = false;

    public Frg_ReconocerFiguras() {
        // Required empty public constructor
    }

    public static Frg_ReconocerFiguras newInstance(String param1, String param2) {
        Frg_ReconocerFiguras fragment = new Frg_ReconocerFiguras();
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
        View view = inflater.inflate(R.layout.fragment_reconocer_figuras, container, false);
        try {
            String lst_Activities = getArguments().getString("activities");
            jsonActivities = new JSONArray(lst_Activities);
            state = view.findViewById(R.id.state);
            state.setVisibility(View.GONE);
            txtResponse = view.findViewById(R.id.txtResponse);
            txtResponse.setVisibility(View.GONE);
            btn = view.findViewById(R.id.btn_comprobar_actividades);
            btn.setVisibility(View.GONE);
            img = view.findViewById(R.id.imgOp);
            lstLista = view.findViewById(R.id.lstEnunciado);
            lstOptions = view.findViewById(R.id.lstOpciones);
            contenido = jsonActivities.getJSONObject(0).getJSONArray("contenido");
            modelContentsEnun = new ArrayList<>();
            modelContentsOp = new ArrayList<>();
            respuestas = new ArrayList<>();
            MapContenido();
            if(respuestas.size() > 1)
                view.findViewById(R.id.btn_comprobar_actividades).setVisibility(View.VISIBLE);
            else
                view.findViewById(R.id.btn_comprobar_actividades).setVisibility(View.GONE);
            if(modelContentsEnun.size() > 0 & modelContentsOp.size() >0) {
                adpEnunciado = new AdpEnunciado(getContext(), modelContentsEnun);
                lstLista.setAdapter(adpEnunciado);
                Glide.with(getContext())
                        .load(imgEnunciado.getMultimedia().getJSONObject(0).getString("url"))
                        .into(img);
                adpOptionReconocerImg = new AdpOptionReconocerImg(getContext(), modelContentsOp);
                lstOptions.setAdapter(adpOptionReconocerImg);
                lstOptions.setOnItemClickListener(this);
            }
            else{
                Toast.makeText(getContext(), "La actividad no tiene contenido", Toast.LENGTH_SHORT).show();
                btn.setVisibility(View.VISIBLE);
                img.setVisibility(View.GONE);
                btn.setText("Continuar");
                btn.setOnClickListener(v -> Navegacion(v));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        view.findViewById(R.id.btn_comprobar_actividades).setOnClickListener(v -> Navegacion(v));
        return view;
    }

    private void MapContenido() {
        try {
            ModelContent modelContent = null;
            for (int i = 0; i < contenido.length(); i++) {
                modelContent = new ModelContent();
                modelContent.setId(contenido.getJSONObject(i).getInt("id"));
                modelContent.setDescripcion(contenido.getJSONObject(i).getString("descripcion"));
                modelContent.setEnunciado(contenido.getJSONObject(i).getBoolean("enunciado"));
                modelContent.setRespuesta(contenido.getJSONObject(i).getBoolean("respuesta"));
                modelContent.setActivo(contenido.getJSONObject(i).getBoolean("activo"));
                modelContent.setMultimedia((JSONArray) contenido.getJSONObject(i).get("multimedia"));
                if (contenido.getJSONObject(i).get("enunciado").equals(true)){
                    if(((JSONArray) contenido.getJSONObject(i).get("multimedia")).length() > 0)
                        imgEnunciado = modelContent;
                    else
                        modelContentsEnun.add(modelContent);
                }
                else {
                    modelContentsOp.add(modelContent);
                    if(contenido.getJSONObject(i).get("respuesta").equals(true))
                        respuestas.add(modelContent);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void CompleteActivity(View v) {
        try {
            // Crear nueva cola de peticiones
            requestQueue = Volley.newRequestQueue(getContext());
            //Parámetros a enviar a la API
            JSONObject param = new JSONObject();
            param.put("idEstudiante", ClssStaticGrupo.idestudiante);
            param.put("idActividad", jsonActivities.getJSONObject(0).getInt("id"));
            param.put("statusRespuesta", respuesta);
            param.put("idsContenido", new JSONObject());
            JsonObjectRequest request_json = new JsonObjectRequest(url, param,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response.length() > 1) {
                                    //Toast.makeText(getContext(), "Actividad exitosa", Toast.LENGTH_LONG).show();
                                    Navegacion(v);
                                } else
                                    Toast.makeText(getContext(), response.get("message").toString(), Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Toast.makeText(getContext(), "Error de conexión", Toast.LENGTH_LONG).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.e("Error: ", error.getMessage());
                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
            // Añadir petición a la cola
            requestQueue.add(request_json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void Navegacion(View v) {
        navController = Navigation.findNavController(v);
        //Eliminamos el item por el cual nos redirecccionamos aca
        jsonActivities.remove(0);
        ClssNavegacionActividades clssNavegacionActividades = new ClssNavegacionActividades(navController, jsonActivities, v);
        clssNavegacionActividades.navegar();

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        opSelected = ((ModelContent) adapterView.getItemAtPosition(i));
        if(respuestas.size() == 0){
            Toast.makeText(getContext(),"La actividad no tiene respuesta",Toast.LENGTH_SHORT).show();
        }
        else if(respuestas.size() == 1){
            state.setVisibility(View.VISIBLE);
            txtResponse.setVisibility(View.VISIBLE);
            if(opSelected.getRespuesta().equals(true)){
                //Toast.makeText(getContext(),"Respuesta correcta",Toast.LENGTH_SHORT).show();
                respuesta = true;
                state.setBackgroundColor(Color.parseColor("#7CB342"));
                txtResponse.setText(R.string.correcto);
                txtResponse.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.icon_valor,0);
                CompleteActivity(view);
            }
            else{
                state.setBackgroundColor(Color.parseColor("#e74c3c"));
                txtResponse.setText(R.string.incorrecto);
                txtResponse.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.sad,0);
                //Toast.makeText(getContext(),"Respuesta incorrecta",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
