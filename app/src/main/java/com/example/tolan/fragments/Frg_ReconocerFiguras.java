package com.example.tolan.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import com.example.tolan.clases.ClssConvertirTextoAVoz;
import com.example.tolan.clases.ClssNavegacionActividades;
import com.example.tolan.clases.ClssStaticGrupo;
import com.example.tolan.models.ModelContent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class Frg_ReconocerFiguras extends Fragment implements AdapterView.OnItemClickListener {

    JSONArray jsonActivities;
    NavController navController;
    static TextToSpeech textToSpeech;
    ClssConvertirTextoAVoz tts;
    private TextView titulo;
    private Toolbar toolbar;
    private ScrollView scrollView;
    private Button btnContinuar;
    private ListView lstLista, lstOptions;
    private ImageView img;
    private LinearLayout state;
    ModelContent modelContent;
    private JSONArray contenido;
    List<ModelContent> modelContentsEnun;
    ModelContent imgEnunciado;
    ArrayList<ModelContent> modelContentsOp;
    List<Integer> lstIds;
    ArrayList<ModelContent> respuestas;
    ArrayList<ModelContent> resp;
    //ArrayList<ModelContent> resp;
    private AdpEnunciado adpEnunciado;
    private AdpOptionReconocerImg adpOptionReconocerImg;
    ModelContent opSelected = new ModelContent();
    private RequestQueue requestQueue;
    private String url;
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
        textToSpeech = new TextToSpeech(getContext(),i -> reproducirAudio(i, titulo.getText().toString()));
        if (getArguments() != null) {
        }
    }

    public void reproducirAudio(int i, String mensaje){
        if(i!= TextToSpeech.ERROR){
            textToSpeech.setLanguage(Locale.getDefault());
            textToSpeech.speak(mensaje,TextToSpeech.QUEUE_FLUSH,null);
        }
        tts = new ClssConvertirTextoAVoz();
        tts.init(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reconocer_figuras, container, false);
        try {
            titulo = view.findViewById(R.id.titulo);
            titulo.setOnClickListener(v -> tts.reproduce(titulo.getText().toString()));
            toolbar = view.findViewById(R.id.toolbar);
            setHasOptionsMenu(true);
            ((AppCompatActivity)this.getActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity)this.getActivity()).getSupportActionBar().setTitle("");
            String lst_Activities = getArguments().getString("activities");
            jsonActivities = new JSONArray(lst_Activities);
            url = getString(R.string.urlBase) + "historial/completeActividad";
            scrollView = view.findViewById(R.id.scrollRF);
            btnContinuar = view.findViewById(R.id.btn_comprobar_actividadesRF);
            state = view.findViewById(R.id.state);
            state.setVisibility(View.GONE);
            img = view.findViewById(R.id.imgOp);
            lstLista = view.findViewById(R.id.lstEnunciado);
            lstOptions = view.findViewById(R.id.lstOpciones);
            contenido = jsonActivities.getJSONObject(0).getJSONArray("contenido");
            modelContentsEnun = new ArrayList<>();
            modelContentsOp = new ArrayList<>();
            respuestas = new ArrayList<>();
            resp = new ArrayList<>();
            modelContent = new ModelContent();
            MapContenido();
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
            else {
                Toast.makeText(getContext(), "La actividad no tiene contenido", Toast.LENGTH_SHORT).show();
                tts.reproduce("La actividad no tiene contenido");
                img.setVisibility(View.GONE);
                btnContinuar.setVisibility(View.VISIBLE);
                btnContinuar.setOnClickListener(v -> Navegacion(v));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_toolbar,menu);
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
                                    //Navegacion(v);
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

    private void AccionOk(){
        animar(false);
        scrollView.post(new Runnable() {
            public void run() {
                scrollView.scrollTo(0, scrollView.getTop());
            }
        });
        state.setVisibility(View.GONE);
        TextView txt = (TextView) state.getChildAt(2);
        tts.reproduce(txt.getText().toString());
    }

    private void animar(boolean mostrar) {
        AnimationSet set = new AnimationSet(true);
        Animation animation = null;
        if (mostrar) {
            animation = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 1.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f);
        } else {
            animation = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 1.0f);
        }
        //duración en milisegundos
        animation.setDuration(500);
        set.addAnimation(animation);
        LayoutAnimationController controller = new LayoutAnimationController(set, 0.25f);
        state.setLayoutAnimation(controller);
        state.startAnimation(animation);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        opSelected = ((ModelContent) adapterView.getItemAtPosition(i));
        tts.reproduce(opSelected.getDescripcion());
        if(respuestas.size() == 0){
            Toast.makeText(getContext(),"La actividad no tiene respuesta",Toast.LENGTH_SHORT).show();
        }
        else if(respuestas.size() == 1){
            if(opSelected.getRespuesta().equals(true)){
                //Toast.makeText(getContext(),"Respuesta correcta",Toast.LENGTH_SHORT).show();
                respuesta = true;
                animar(true);
                //Toast.makeText(getContext(),"Correcto",Toast.LENGTH_SHORT).show();
                scrollView.post(new Runnable() {
                    public void run() {
                        scrollView.scrollTo(0, scrollView.getBottom());
                    }
                });
                //Seteamos el background verde
                state.setBackgroundColor(Color.parseColor("#AAFAB1"));
                //Seteamos el texto de continuar y lo mostramos
                TextView txt = (TextView) state.getChildAt(0);
                txt.setText("¡Excelente!");
                txt.setTextColor(Color.parseColor("#048710"));
                txt.setVisibility(View.VISIBLE);
                tts.reproduce(txt.getText().toString());
                ImageView img = (ImageView) state.getChildAt(1);
                img.setImageResource(R.drawable.icon_valor);
                img.setColorFilter(Color.parseColor("#048710"));
                state.getChildAt(2).setVisibility(View.GONE);
                state.getChildAt(3).setVisibility(View.VISIBLE);
                state.getChildAt(3).setOnClickListener(vcont -> Navegacion(vcont));
                //Ubicamos el layout visible
                state.setVisibility(View.VISIBLE);
                CompleteActivity(view);
            }
            else{
                //Toast.makeText(getContext(),"Respuesta incorrecta",Toast.LENGTH_SHORT).show();
                respuesta = false;
                animar(true);
                scrollView.post(new Runnable() {
                    public void run() {
                        scrollView.scrollTo(0, scrollView.getBottom());
                    }
                });
                //Seteamos el backgroun rojo
                state.setBackgroundColor(Color.parseColor("#F7B9B9"));
                //Seteamos el texto de error y lo mostramos
                TextView txt = (TextView) state.getChildAt(0);
                txt.setText("¡Ups! ¡Fallaste!");
                txt.setTextColor(Color.parseColor("#C70039"));
                txt.setVisibility(View.VISIBLE);
                tts.reproduce(txt.getText().toString());
                ImageView img = (ImageView) state.getChildAt(1);
                img.setImageResource(R.drawable.sad);
                img.setColorFilter(Color.parseColor("#C70039"));
                //Ocultamos el boton comprobar
                state.getChildAt(3).setVisibility(View.GONE);
                //Seteamos evento click a boton OK
                state.getChildAt(2).setVisibility(View.VISIBLE);
                state.getChildAt(2).setOnClickListener(vok -> AccionOk());
                state.setVisibility(View.VISIBLE);
            }
        }
    }
}
