package com.example.tolan.fragments;

import static android.content.ClipData.newPlainText;

import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.view.DragEvent;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.tolan.R;
import com.example.tolan.adapters.AdpEnunciado;
import com.example.tolan.adapters.AdpOptionArrastrarSoltarTxt;
import com.example.tolan.clases.ClssConvertirTextoAVoz;
import com.example.tolan.clases.ClssNavegacionActividades;
import com.example.tolan.clases.ClssStaticGrupo;
import com.example.tolan.clases.ClssVolleySingleton;
import com.example.tolan.models.ModelContent;
import com.example.tolan.models.ModelUser;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class Frg_ArrastrarSoltar extends Fragment {

    JSONArray jsonActivities;
    NavController navController;
    private Toolbar toolbar;
    static TextToSpeech textToSpeech;
    MenuItem mr;
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
    private String[] msg_true = null;
    LinearLayout destino, dest;
    private AdpEnunciado adpEnunciado;
    private AdpOptionArrastrarSoltarTxt adpOptionArrastrarSoltarTxt;
    ModelContent opSelected = new ModelContent();
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
        textToSpeech = new TextToSpeech(getContext(), i -> reproducirAudio(i, titulo.getText().toString()));
        if (getArguments() != null) {
        }
    }

    public void reproducirAudio(int i, String mensaje) {
        try {
            if (i != TextToSpeech.ERROR) {
                textToSpeech.setLanguage(Locale.getDefault());
                textToSpeech.speak(mensaje, TextToSpeech.QUEUE_FLUSH, null);
            }
        } catch (Exception e) {}
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
            titulo.setOnClickListener(v -> ClssConvertirTextoAVoz.getIntancia(v.getContext()).reproduce(titulo.getText().toString()));
            toolbar = view.findViewById(R.id.toolbar);
            setHasOptionsMenu(true);
            ((AppCompatActivity) this.getActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity) this.getActivity()).getSupportActionBar().setTitle("");
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
            rcvOptions.setLayoutManager(new GridLayoutManager(getContext(), 2));
            contenido = jsonActivities.getJSONObject(0).getJSONArray("contenido");
            modelContentsEnun = new ArrayList<>();
            modelContentsOp = new ArrayList<>();
            respuestas = new ArrayList<>();
            resp = new ArrayList<>();
            msg_true = getResources().getStringArray(R.array.msg_true);
            modelContent = new ModelContent();
            modelContent.MapContenido(contenido, modelContentsEnun, modelContentsOp, respuestas);
            if (modelContentsEnun.size() > 0 & modelContentsOp.size() > 0 & respuestas.size() > 0) {
                RespuestasOk();
            } else {
                //jsonActivities.remove(0);
                Toast.makeText(getContext(), "La actividad no tiene contenido", Toast.LENGTH_SHORT).show();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ClssConvertirTextoAVoz.getIntancia(getContext()).reproduce("La actividad no tiene contenido");
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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_toolbar, menu);
        mr = menu.findItem(R.id.btnRecompensa);
        mr.setTitle(String.valueOf(ModelUser.stockcaritas));
        //super.onCreateOptionsMenu(menu, inflater);
    }

    private void RespuestasOk() {
        try {
            if (respuestas.size() == 1)
                destino.setTag(respuestas.get(0).getDescripcion().trim());
            else
                destino.setTag("respuesta");
            adpEnunciado = new AdpEnunciado(getContext(), modelContentsEnun);
            lstLista.setAdapter(adpEnunciado);
            adpOptionArrastrarSoltarTxt = new AdpOptionArrastrarSoltarTxt(getContext(), modelContentsOp, respuestas);
            rcvOptions.setAdapter(adpOptionArrastrarSoltarTxt);
            adpOptionArrastrarSoltarTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int opcselec = rcvOptions.getChildAdapterPosition(view);
                    opSelected = modelContentsOp.get(opcselec);
                    Toast.makeText(getContext(), opSelected.getDescripcion(), Toast.LENGTH_SHORT).show();
                    //tts.reproduce(opSelected.getDescripcion());
                    ClssConvertirTextoAVoz.getIntancia(getContext()).reproduce(opSelected.getDescripcion());
                }
            });
            adpOptionArrastrarSoltarTxt.setOnLongClickListener(v -> LongClickListener(v));
            destino.setOnDragListener((v, event) -> DragListener(v, event));
        } catch (Exception e) {}
    }

    private boolean LongClickListener(View v) {
        try {
            LinearLayout linearLayout = (LinearLayout) v;
            MaterialCardView cardView = (MaterialCardView) linearLayout.getChildAt(0);
            View textView = cardView.getChildAt(0);
            ClipData data = newPlainText((CharSequence) textView.getTag(), "");
            View.DragShadowBuilder myShadow = new View.DragShadowBuilder(v);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                v.startDragAndDrop(data, myShadow, textView, 0);
            else
                v.startDrag(data, myShadow, textView, 0);
            v.setVisibility(View.INVISIBLE);
        } catch (Exception e) {}
        return true;
    }

    private boolean DragListener(View v, DragEvent event) {
        try {
            switch (event.getAction()) {
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
                    if (uno != null & dos != null) {
                        if (v.getTag().toString().equals(event.getClipDescription().getLabel())) {
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
                            txt.setText(generarAleatorio());
                            txt.setTextColor(Color.parseColor("#048710"));
                            txt.setVisibility(View.VISIBLE);
                            //tts.reproduce(txt.getText().toString());
                            ClssConvertirTextoAVoz.getIntancia(v.getContext()).reproduce(txt.getText().toString());
                            ImageView img = (ImageView) state.getChildAt(1);
                            img.setImageResource(R.drawable.icon_valor);
                            img.setColorFilter(Color.parseColor("#048710"));
                            state.getChildAt(2).setVisibility(View.GONE);
                            state.getChildAt(3).setVisibility(View.VISIBLE);
                            state.getChildAt(3).setOnClickListener(vcont -> Navegacion(vcont));
                            //Ubicamos el layout visible
                            state.setVisibility(View.VISIBLE);
                            CompleteActivity(v);
                        } else {
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
                            txt.setText("¡Incorrecto!\n¡Vuelve a intentarlo!");
                            txt.setTextColor(Color.parseColor("#C70039"));
                            txt.setVisibility(View.VISIBLE);
                            //tts.reproduce(txt.getText().toString());
                            ClssConvertirTextoAVoz.getIntancia(v.getContext()).reproduce(txt.getText().toString());
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
                    } else
                        linearLayout.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {}
        return true;
    }

    private String generarAleatorio() {
        Random random = new Random();
        String r = msg_true[random.nextInt(msg_true.length)];
        return r;
    }

    private void Navegacion(View v) {
        try {
            TextView txt = (TextView) state.getChildAt(3);
            ClssConvertirTextoAVoz.getIntancia(v.getContext()).reproduce(txt.getText().toString());
            navController = Navigation.findNavController(v);
            //Eliminamos el item por el cual nos redirecccionamos aca
            jsonActivities.remove(0);
            ClssNavegacionActividades clssNavegacionActividades = new ClssNavegacionActividades(navController, jsonActivities, v);
            clssNavegacionActividades.navegar();
        } catch (Exception e) {}
    }

    private void AccionOk() {
        try {
            animar(false);
            scrollView.post(new Runnable() {
                public void run() {
                    scrollView.scrollTo(0, scrollView.getTop());
                }
            });
            state.setVisibility(View.GONE);
            TextView txt = (TextView) state.getChildAt(2);
            //tts.reproduce(txt.getText().toString());
            ClssConvertirTextoAVoz.getIntancia(getContext()).reproduce(txt.getText().toString());
        } catch (Exception e) {}
    }

    private void animar(boolean mostrar) {
        try {
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
        } catch (Exception e) {}
    }

    private void CompleteActivity(View v) {
        try {
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
                                    int recompensa = response.getInt("recompensaganada");
                                    ModelUser.stockcaritas += recompensa;
                                    mr.setTitle(String.valueOf(ModelUser.stockcaritas));
                                    //Toast.makeText(getContext(), "Actividad exitosa", Toast.LENGTH_LONG).show();
                                } else
                                    Toast.makeText(getContext(), response.get("message").toString(), Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                //Toast.makeText(getContext(), "Error de conexión", Toast.LENGTH_LONG).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.e("Error: ", error.getMessage());
                    //Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    ClssConvertirTextoAVoz.getIntancia(v.getContext()).reproduce("Error de conexión con el servidor");
                }
            });
            // Añadir petición a la cola
            ClssVolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(request_json);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}