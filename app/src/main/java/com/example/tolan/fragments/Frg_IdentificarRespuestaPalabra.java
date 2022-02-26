package com.example.tolan.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
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
import com.example.tolan.adapters.AdpOptionIdentifyTxt;
import com.example.tolan.clases.ClssConvertirTextoAVoz;
import com.example.tolan.clases.ClssNavegacionActividades;
import com.example.tolan.clases.ClssStaticGrupo;
import com.example.tolan.clases.ClssVolleySingleton;
import com.example.tolan.models.ModelContent;
import com.example.tolan.models.ModelUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Frg_IdentificarRespuestaPalabra extends Fragment {

    JSONArray jsonActivities;
    NavController navController;
    private Toolbar toolbar;
    static TextToSpeech textToSpeech;
    ClssConvertirTextoAVoz tts;
    private TextView titulo;
    private ScrollView scrollView;
    private Button btnContinuar;
    ModelContent modelContent;
    private ListView lstLista;
    private RecyclerView rcvOptions;
    private LinearLayout state;
    private JSONArray contenido;
    List<ModelContent> modelContentsEnun;
    ArrayList<ModelContent> modelContentsOp;
    ArrayList<ModelContent> respuestas;
    ArrayList<ModelContent> resp;
    private AdpEnunciado adpEnunciado;
    private AdpOptionIdentifyTxt adpOptiosIdentifyTxt;
    ModelContent opSelected = new ModelContent();
    //private RequestQueue requestQueue;
    private String url;
    Boolean respuesta = false;

    public Frg_IdentificarRespuestaPalabra() {
        // Required empty public constructor
    }

    public static Frg_IdentificarRespuestaPalabra newInstance(String param1, String param2) {
        Frg_IdentificarRespuestaPalabra fragment = new Frg_IdentificarRespuestaPalabra();
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
        return inflater.inflate(R.layout.fragment_identificar_respuesta_palabra, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
            scrollView = view.findViewById(R.id.scrollRT);
            btnContinuar = view.findViewById(R.id.btn_comprobar_actividadesRT);
            state = view.findViewById(R.id.state);
            state.setVisibility(View.GONE);
            lstLista = view.findViewById(R.id.lstEnunciado);
            rcvOptions = (RecyclerView) view.findViewById(R.id.rcvTxt);
            rcvOptions.setLayoutManager(new GridLayoutManager(getContext(),2));
            contenido = jsonActivities.getJSONObject(0).getJSONArray("contenido");
            modelContentsEnun = new ArrayList<>();
            modelContentsOp = new ArrayList<>();
            respuestas = new ArrayList<>();
            resp = new ArrayList<>();
            modelContent = new ModelContent();
            modelContent.MapContenido(contenido,modelContentsEnun,modelContentsOp,respuestas);
            if(modelContentsEnun.size() > 0 & modelContentsOp.size() >0) {
                RespuestasOk();
            }
            else {
                tts = new ClssConvertirTextoAVoz();
                tts.init(getContext());
                Toast.makeText(getContext(), "La actividad no tiene contenido", Toast.LENGTH_SHORT).show();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tts.reproduce("La actividad no tiene contenido");
                    }
                }, 1000);
                state.setVisibility(View.VISIBLE);
                state.setBackgroundColor(Color.WHITE);
                state.getChildAt(0).setVisibility(View.GONE);
                state.getChildAt(1).setVisibility(View.GONE);
                state.getChildAt(2).setVisibility(View.GONE);
                state.getChildAt(3).setVisibility(View.VISIBLE);
                btnContinuar.setOnClickListener(v -> Navegacion(v));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    MenuItem mr;
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_toolbar,menu);
        mr = menu.findItem(R.id.btnRecompensa);
        //mr.setTitle(String.valueOf(ModelUser.stockcaritas));
    }

    private void RespuestasOk(){
        adpEnunciado = new AdpEnunciado(getContext(), modelContentsEnun);
        lstLista.setAdapter(adpEnunciado);
        adpOptiosIdentifyTxt = new AdpOptionIdentifyTxt(getContext(), modelContentsOp);
        rcvOptions.setAdapter(adpOptiosIdentifyTxt);
        adpOptiosIdentifyTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int opcselec = rcvOptions.getChildAdapterPosition(view);
                opSelected = modelContentsOp.get(opcselec);
                tts.reproduce(opSelected.getDescripcion());
                if (respuestas.size() == 0) {
                    Toast.makeText(getContext(), "La actividad no tiene respuesta", Toast.LENGTH_SHORT).show();
                } else if (respuestas.size() == 1) {
                    if (opSelected.getRespuesta().equals(true)) {
                        //Toast.makeText(getContext(),"Respuesta correcta",Toast.LENGTH_SHORT).show();
                        rcvOptions.getChildAt(opcselec).setBackgroundColor(Color.parseColor("#44cccc"));
                        respuesta = true;
                        state.setVisibility(View.VISIBLE);
                        scrollView.post(new Runnable() {
                            public void run() {
                                scrollView.scrollTo(0, scrollView.getBottom());
                            }
                        });
                        animar(true);
                        //Seteamos el background verde
                        state.setBackgroundColor(Color.parseColor("#AAFAB1"));
                        //Seteamos el texto de continuar y lo mostramos
                        TextView txt = (TextView) state.getChildAt(0);
                        txt.setText("¡Excelente!");
                        txt.setTextColor(Color.parseColor("#048710"));
                        txt.setVisibility(View.VISIBLE);
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                tts.reproduce(txt.getText().toString());
                            }
                        }, 1000);
                        ImageView img = (ImageView) state.getChildAt(1);
                        img.setImageResource(R.drawable.icon_valor);
                        img.setColorFilter(Color.parseColor("#048710"));
                        state.getChildAt(2).setVisibility(View.GONE);
                        state.getChildAt(3).setVisibility(View.VISIBLE);
                        state.getChildAt(3).setOnClickListener(vcont -> Navegacion(vcont));
                        //Ubicamos el layout visible
                        state.setVisibility(View.VISIBLE);
                        CompleteActivity(view);
                    } else {
                        rcvOptions.getChildAt(opcselec).setBackgroundColor(Color.parseColor("#C70039"));
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
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                tts.reproduce(txt.getText().toString());
                            }
                        }, 1000);
                        ImageView img = (ImageView) state.getChildAt(1);
                        img.setImageResource(R.drawable.sad);
                        img.setColorFilter(Color.parseColor("#C70039"));
                        //Ocultamos el boton comprobar
                        state.getChildAt(3).setVisibility(View.GONE);
                        //Seteamos evento click a boton OK
                        state.getChildAt(2).setVisibility(View.VISIBLE);
                        state.getChildAt(2).setOnClickListener(vok -> AccionOk(opcselec));
                        state.setVisibility(View.VISIBLE);
                    }
                } else if (respuestas.size() > 1) {
                    resp.add(opSelected);
                }
                //Toast.makeText(getContext(), opSelected.getDescripcion(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void Navegacion(View v){
        navController = Navigation.findNavController(v);
        //Eliminamos el item por el cual nos redirecccionamos aca
        jsonActivities.remove(0);
        ClssNavegacionActividades clssNavegacionActividades= new ClssNavegacionActividades(navController,jsonActivities,v);
        clssNavegacionActividades.navegar();
    }

    private void AccionOk(int op){
        animar(false);
        rcvOptions.getChildAt(op).setBackgroundColor(Color.parseColor("#ffffff"));
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

    private void CompleteActivity(View v){
        try {
            // Crear nueva cola de peticiones
            //requestQueue = Volley.newRequestQueue(getContext());
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
                                    int recompensa=response.getInt("recompensaganada");
                                    ModelUser.stockcaritas+=recompensa;

                                    //Actualice el itemMenú creado
                                    mr.setTitle(String.valueOf(ModelUser.stockcaritas));
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
            //requestQueue.add(request_json);
            ClssVolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(request_json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}