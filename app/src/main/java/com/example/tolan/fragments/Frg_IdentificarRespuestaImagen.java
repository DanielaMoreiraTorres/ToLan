package com.example.tolan.fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.tolan.R;
import com.example.tolan.adapters.AdpEnunciado;
import com.example.tolan.adapters.AdpOptionIdentifyImg;
import com.example.tolan.clases.ClssStaticGrupo;
import com.example.tolan.models.ModelContent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Frg_IdentificarRespuestaImagen extends Fragment implements View.OnClickListener {

    NavController navController;
    private ListView lstLista;
    private RecyclerView rcvOptions;
    private View state;
    private TextView txtResponse;
    JSONArray jsonActivities;
    private JSONArray contenido;
    List<ModelContent> modelContentsEnun;
    ArrayList<ModelContent> modelContentsOp;
    ArrayList<ModelContent> respuestas;
    ArrayList<ModelContent> resp;
    private AdpEnunciado adpEnunciado;
    private AdpOptionIdentifyImg adpOptiosIdentifyImg;
    ModelContent opSelected = new ModelContent();
    private RequestQueue requestQueue;
    private String url = "https://db-bartolucci.herokuapp.com/historial/completeActividad";
    Boolean respuesta = false;

    public Frg_IdentificarRespuestaImagen() {
        // Required empty public constructor
    }

    public static Frg_IdentificarRespuestaImagen newInstance(String param1, String param2) {
        Frg_IdentificarRespuestaImagen fragment = new Frg_IdentificarRespuestaImagen();
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
        return inflater.inflate(R.layout.fragment_identificar_respuesta_imagen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            String lst_Activities = getArguments().getString("activities");
            jsonActivities = new JSONArray(lst_Activities);
            ArrayList<JSONObject> activities = new ArrayList<>();
            state = view.findViewById(R.id.state);
            state.setVisibility(View.GONE);
            txtResponse = view.findViewById(R.id.txtResponse);
            txtResponse.setVisibility(View.GONE);
            lstLista = view.findViewById(R.id.lstEnunciado);
            rcvOptions = (RecyclerView) view.findViewById(R.id.rcvImg);
            rcvOptions.setLayoutManager(new GridLayoutManager(getContext(),2));
            contenido = jsonActivities.getJSONObject(0).getJSONArray("contenido");
            modelContentsEnun = new ArrayList<>();
            modelContentsOp = new ArrayList<>();
            respuestas = new ArrayList<>();
            resp = new ArrayList<>();
            MapContenido();
            if(respuestas.size() > 1)
                view.findViewById(R.id.btn_comprobar_actividades).setVisibility(View.VISIBLE);
            else
                view.findViewById(R.id.btn_comprobar_actividades).setVisibility(View.GONE);
            adpEnunciado = new AdpEnunciado(getContext(),modelContentsEnun);
            lstLista.setAdapter(adpEnunciado);
            adpOptiosIdentifyImg = new AdpOptionIdentifyImg(getContext(),modelContentsOp);
            rcvOptions.setAdapter(adpOptiosIdentifyImg);
            adpOptiosIdentifyImg.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onClick(View view) {
                    int opcselec = rcvOptions.getChildAdapterPosition(view);
                    opSelected = modelContentsOp.get(opcselec);
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
                            CompleteActivity(view);
                        }
                        else{
                            state.setBackgroundColor(Color.parseColor("#e74c3c"));
                            txtResponse.setText(R.string.incorrecto);
                            //Toast.makeText(getContext(),"Respuesta incorrecta",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else if(respuestas.size() > 1){
                        resp.add(opSelected);
                    }
                    //Toast.makeText(getContext(),opSelected.getDescripcion(),Toast.LENGTH_SHORT).show();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
        view.findViewById(R.id.btn_comprobar_actividades).setOnClickListener(this);
    }

    private void MapContenido(){
        try {
            ModelContent modelContent = null;
            for (int i=0;i<contenido.length();i++){
                modelContent = new ModelContent();
                modelContent.setId(contenido.getJSONObject(i).getInt("id"));
                modelContent.setDescripcion(contenido.getJSONObject(i).getString("descripcion"));
                modelContent.setEnunciado(contenido.getJSONObject(i).getBoolean("enunciado"));
                modelContent.setRespuesta(contenido.getJSONObject(i).getBoolean("respuesta"));
                modelContent.setActivo(contenido.getJSONObject(i).getBoolean("activo"));
                modelContent.setMultimedia((JSONArray) contenido.getJSONObject(i).get("multimedia"));
                if(contenido.getJSONObject(i).get("enunciado").equals(true))
                    modelContentsEnun.add(modelContent);
                else{
                    modelContentsOp.add(modelContent);
                    if(contenido.getJSONObject(i).get("respuesta").equals(true))
                        respuestas.add(modelContent);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if(resp.equals(respuestas)){
            respuesta = true;
            state.setBackgroundColor(Color.parseColor("#7CB342"));
            txtResponse.setText(R.string.correcto);
            CompleteActivity(v);
        }
        else{
            respuesta = false;
            state.setBackgroundColor(Color.parseColor("#e74c3c"));
            txtResponse.setText(R.string.incorrecto);
            resp.clear();
            Toast.makeText(getContext(),"Respuesta incorrecta\n Vuelve a intentarlo",Toast.LENGTH_SHORT).show();
        }
    }

    private void CompleteActivity(View v){
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

    private void Navegacion(View v){
        //Inicio Navegación
        navController = Navigation.findNavController(v);
        Bundle bundle;
        String actividad;
        NavController navController = Navigation.findNavController(v);
        //Eliminamos el item por el cual nos redirecccionamos aca
        jsonActivities.remove(0);
        //Avanzar hacia la siguiente actividad
        try {
            if (jsonActivities.length() > 0) {
                //Pasamos al siguiente fragmento
                JSONObject activity = jsonActivities.getJSONObject(0);
                //Tomamos nuestra activiad del objeto
                actividad = activity.getString("nombre");
                bundle = new Bundle();
                bundle.putString("activities", jsonActivities.toString());
                switch (actividad) {
                    //El case nos permitira redireccionar hacia el Layout correspondiente para navegar hacia el
                    case "Reconocer figuras":
                        Toast.makeText(v.getContext(), actividad, Toast.LENGTH_SHORT).show();
                        navController.navigate(R.id.fragmentReconocerFiguras, bundle);
                        break;
                    case "Ordenar la secuencia":
                        Toast.makeText(v.getContext(), actividad, Toast.LENGTH_SHORT).show();
                        navController.navigate(R.id.fragmentOrdenarSecuenciasImagenes, bundle);
                        break;
                    case "Identificar entre palabras":
                        Toast.makeText(v.getContext(), actividad, Toast.LENGTH_SHORT).show();
                        navController.navigate(R.id.fragmentIdentificarRespuestaPalabra, bundle);
                        //Toast.makeText(v.getContext(), "Layout Identificar entre palabras no existe", Toast.LENGTH_SHORT).show();
                        break;
                    case "Identificar entre imágenes":
                        Toast.makeText(v.getContext(), actividad, Toast.LENGTH_SHORT).show();
                        navController.navigate(R.id.fragmentIdentificarRespuestaImagen, bundle);
                        //Toast.makeText(v.getContext(), "Layout Identificar entre palabras no existe", Toast.LENGTH_SHORT).show();
                        break;
                    case "Armar rompecabezass":
                        Toast.makeText(v.getContext(), "Layout Armar rompecabezass no existe", Toast.LENGTH_SHORT).show();
                        break;
                    case "Seleccionar pares. - Imagen-Texto":
                        Toast.makeText(v.getContext(), actividad, Toast.LENGTH_SHORT).show();
                        navController.navigate(R.id.fragmentSeleccionarParesImagenTexto, bundle);
                        //Toast.makeText(v.getContext(), "Layout Seleccionar pares. - Imagen-Texto no existe", Toast.LENGTH_SHORT).show();
                        break;
                    case "Seleccionar pares. - Imagen-Imagen":
                        Toast.makeText(v.getContext(), actividad, Toast.LENGTH_SHORT).show();
                        navController.navigate(R.id.fragmentSeleccionarParesImagenImagen, bundle);
                        //Toast.makeText(v.getContext(), "Layout Seleccionar pares. - Imagen-Texto no existe", Toast.LENGTH_SHORT).show();
                        break;
                    case "Grafomotricidad":
                        Toast.makeText(v.getContext(), actividad, Toast.LENGTH_SHORT).show();
                        navController.navigate(R.id.fragmentGrafomotricidad, bundle);
                        //Toast.makeText(v.getContext(), "Layout Grafomotricidad no existe", Toast.LENGTH_SHORT).show();
                        break;
                    case "Arrastrar y Soltar":
                        Toast.makeText(v.getContext(), actividad, Toast.LENGTH_SHORT).show();
                        navController.navigate(R.id.fragmentArrastrarSoltar, bundle);
                        break;
                }
            } else {
                Toast.makeText(getContext(), "Redirigiendo al menu principal..", Toast.LENGTH_SHORT).show();
                //Volvemos al fragmento principal eliminando los recursos en pila
                navController.navigate(R.id.inicioFragment, null, new NavOptions.Builder()
                        .setPopUpTo(R.id.inicioFragment, true)
                        .build());
            }
        } catch (JSONException ex) {
            Toast.makeText(v.getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        //Fin Navegación
    }
}