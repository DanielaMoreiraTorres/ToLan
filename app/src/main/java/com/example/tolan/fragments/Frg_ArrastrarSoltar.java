package com.example.tolan.fragments;

import static android.content.ClipData.newPlainText;

import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.tolan.R;
import com.example.tolan.adapters.AdpEnunciado;
import com.example.tolan.adapters.AdpOptionArrastrarSoltarTxt;
import com.example.tolan.clases.ClssConvertirTextoAVoz;
import com.example.tolan.clases.ClssNavegacionActividades;
import com.example.tolan.clases.ClssStaticGrupo;
import com.example.tolan.models.ModelContent;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Frg_ArrastrarSoltar extends Fragment {

    JSONArray jsonActivities;
    NavController navController;
    private Toolbar toolbar;
    static TextToSpeech textToSpeech;
    ClssConvertirTextoAVoz tts;
    private TextView titulo;
    private ScrollView scrollView;
    private Button btnContinuar;
    private ListView lstLista;
    private RecyclerView rcvOptions;
    private LinearLayout state;
    ModelContent modelContent;
    private JSONArray contenido;
    List<ModelContent> modelContentsEnun;
    ArrayList<ModelContent> modelContentsOp;
    ArrayList<ModelContent> respuestas;
    ArrayList<ModelContent> resp;
    LinearLayout destino, dest;
    private AdpEnunciado adpEnunciado;
    private AdpOptionArrastrarSoltarTxt adpOptionArrastrarSoltarTxt;
    ModelContent opSelected = new ModelContent();
    private RequestQueue requestQueue;
    private String url;
    Boolean respuesta = false;
    String uno, dos;
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
        return inflater.inflate(R.layout.fragment_arrastrar_soltar, container, false);
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
            scrollView = view.findViewById(R.id.scrollAS);
            btnContinuar = view.findViewById(R.id.btn_comprobar_actividadesAS);
            state = view.findViewById(R.id.state);
            state.setVisibility(View.GONE);
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
                RespuestasOk();
            }
            else {
                //jsonActivities.remove(0);
                Toast.makeText(getContext(), "La actividad no tiene contenido", Toast.LENGTH_SHORT).show();
                tts.reproduce("La actividad no tiene contenido");
                btnContinuar.setVisibility(View.VISIBLE);
                btnContinuar.setOnClickListener(v -> Navegacion(v));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_toolbar,menu);
        //super.onCreateOptionsMenu(menu, inflater);
    }

    private void RespuestasOk(){
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
                //Toast.makeText(getContext(), opSelected.getDescripcion(), Toast.LENGTH_SHORT).show();
                tts.reproduce(opSelected.getDescripcion());
            }
        });
        adpOptionArrastrarSoltarTxt.setOnLongClickListener(v -> LongClickListener(v));
        destino.setOnDragListener((v, event) -> DragListener(v, event));
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
                        respuesta = true;
                        animar(true);
                        //Toast.makeText(getContext(),"Correcto",Toast.LENGTH_SHORT).show();
                        scrollView.post(new Runnable() {
                            public void run() {
                                scrollView.scrollTo(0, scrollView.getBottom());
                            }
                        });
                        cardView.removeView(textView);
                        dest = (LinearLayout) v;
                        dest.addView(textView);
                        textView.setVisibility(View.VISIBLE);
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
                        CompleteActivity(v);
                    }
                    else {
                        //Toast.makeText(getContext(),"Incorrecto",Toast.LENGTH_SHORT).show();
                        respuesta = false;
                        animar(true);
                        scrollView.post(new Runnable() {
                            public void run() {
                                scrollView.scrollTo(0, scrollView.getBottom());
                            }
                        });
                        linearLayout.setVisibility(View.VISIBLE);
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
                else
                    linearLayout.setVisibility(View.VISIBLE);
                break;
            default: break;
        }
        return true;
    }

    private void Navegacion(View v) {
        TextView txt = (TextView) state.getChildAt(3);
        tts.reproduce(txt.getText().toString());
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
                    //Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    tts.reproduce("Error de conexión con el servidor");
                }
            });
            // Añadir petición a la cola
            requestQueue.add(request_json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}