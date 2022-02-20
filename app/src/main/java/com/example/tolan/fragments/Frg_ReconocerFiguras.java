package com.example.tolan.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.tolan.clases.ClssStaticGrupo;
import com.example.tolan.models.ModelContent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Frg_ReconocerFiguras extends Fragment implements View.OnClickListener {

    JSONArray jsonActivities;
    NavController navController;
    private ListView lstLista;
    private ListView lstOptions;
    private ImageView img;
    private View state;
    private TextView txtResponse;
    private JSONArray contenido;
    List<ModelContent> modelContentsEnun;
    ArrayList<ModelContent> modelContentsOp;
    List<Integer> lstIds;
    /*ArrayList<ModelContent> respuestas;
    ArrayList<ModelContent> resp;*/
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
        return inflater.inflate(R.layout.fragment_reconocer_figuras, container, false);
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
            img = view.findViewById(R.id.imgOp);
            lstLista = view.findViewById(R.id.lstEnunciado);
            lstOptions = view.findViewById(R.id.lstOpciones);
            contenido = jsonActivities.getJSONObject(0).getJSONArray("contenido");
            modelContentsEnun = new ArrayList<>();
            modelContentsOp = new ArrayList<>();
            lstIds = Arrays.asList();
            //respuestas = new ArrayList<>();
            MapContenido();
            /*if(respuestas.size() > 1)
                view.findViewById(R.id.btn_comprobar_actividades).setVisibility(View.VISIBLE);
            else
                view.findViewById(R.id.btn_comprobar_actividades).setVisibility(View.GONE);*/
            adpEnunciado = new AdpEnunciado(getContext(),modelContentsEnun);
            lstLista.setAdapter(adpEnunciado);
            //FALTA CARGAR LA IMAGEN DE ENUNCIADO

            Random aleatorio = new Random();
            int idRandon = lstIds.get(aleatorio.nextInt(lstIds.size()));
            Glide.with(getContext())
                    .load(modelContentsEnun.get(0).getMultimedia().getJSONObject(0).getString("url"))
                    .into(img);
            //
            adpOptionReconocerImg = new AdpOptionReconocerImg(getContext(),modelContentsOp);
            lstOptions.setAdapter(adpOptionReconocerImg);

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
                else {
                    modelContentsOp.add(modelContent);
                    lstIds.add(modelContent.getId());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        /*if(resp.equals(respuestas)){
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
            //Toast.makeText(getContext(),"Respuesta incorrecta \n Vuelve a intentarlo",Toast.LENGTH_SHORT).show();
        }*/
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
                    case "Identificar respuesta entre palabras":
                        Toast.makeText(v.getContext(), actividad, Toast.LENGTH_SHORT).show();
                        navController.navigate(R.id.fragmentIdentificarRespuestaPalabra, bundle);
                        //Toast.makeText(v.getContext(), "Layout Identificar entre palabras no existe", Toast.LENGTH_SHORT).show();
                        break;
                    case "Identificar respuesta entre imagenes":
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
    }
}
