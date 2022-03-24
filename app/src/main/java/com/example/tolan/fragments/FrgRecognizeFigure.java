package com.example.tolan.fragments;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.example.tolan.R;
import com.example.tolan.adapters.AdpStatement;
import com.example.tolan.adapters.AdpOptionRecognizeImg;
import com.example.tolan.clases.ClssAnimation;
import com.example.tolan.clases.ClssConvertTextToSpeech;
import com.example.tolan.clases.ClssNavegacionActividades;
import com.example.tolan.clases.ClssStaticGroup;
import com.example.tolan.clases.ClssVolleySingleton;
import com.example.tolan.models.ModelContent;
import com.example.tolan.models.ModelUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class FrgRecognizeFigure extends Fragment implements View.OnClickListener {

    JSONArray jsonActivities;
    NavController navController;
    static TextToSpeech textToSpeech;
    MenuItem mr;
    private TextView titulo;
    private Toolbar toolbar;
    private ScrollView scrollView;
    private Button btnContinuar;
    private ListView lstLista;
    private RecyclerView rcvOption;
    private RelativeLayout rlSel;
    private ImageView img, imgAudio;
    private LinearLayout lySel, state;
    ModelContent modelContent;
    private JSONArray contenido;
    List<ModelContent> modelContentsEnun;
    ModelContent imgEnunciado;
    ArrayList<ModelContent> modelContentsOp;
    List<Integer> lstIds;
    ArrayList<ModelContent> respuestas;
    ArrayList<ModelContent> resp;
    private String[] msg_true = null;
    private AdpStatement adpEnunciado;
    private AdpOptionRecognizeImg adpOptionReconocerImg;
    ModelContent opSelected = new ModelContent();
    ModelContent enun = new ModelContent();
    private String url, mensaje = "";
    Boolean respuesta = false;

    public FrgRecognizeFigure() {
        // Required empty public constructor
    }

    public static FrgRecognizeFigure newInstance(String param1, String param2) {
        FrgRecognizeFigure fragment = new FrgRecognizeFigure();
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
        } catch (Exception e){}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recognize_figure, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            titulo = view.findViewById(R.id.titulo);
            titulo.setOnClickListener(v -> ClssConvertTextToSpeech.getIntancia(v.getContext()).reproduce(titulo.getText().toString()));
            //tts.reproduce(titulo.getText().toString()));
            toolbar = view.findViewById(R.id.toolbar);
            setHasOptionsMenu(true);
            ((AppCompatActivity) this.getActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity) this.getActivity()).getSupportActionBar().setTitle("");
            String lst_Activities = getArguments().getString("activities");
            jsonActivities = new JSONArray(lst_Activities);
            url = getString(R.string.urlBase) + "historial/completeActividad";
            scrollView = view.findViewById(R.id.scrollRF);
            btnContinuar = view.findViewById(R.id.btn_comprobar_actividadesRF);
            state = view.findViewById(R.id.state);
            state.setVisibility(View.GONE);
            img = view.findViewById(R.id.imgOp);
            imgAudio = view.findViewById(R.id.imgAudio);
            lstLista = view.findViewById(R.id.lstEnunciado);
            rcvOption = (RecyclerView) view.findViewById(R.id.rcvOption);
            rcvOption.setLayoutManager(new LinearLayoutManager(getContext()));
            contenido = jsonActivities.getJSONObject(0).getJSONArray("contenido");
            modelContentsEnun = new ArrayList<>();
            modelContentsOp = new ArrayList<>();
            respuestas = new ArrayList<>();
            resp = new ArrayList<>();
            msg_true = getResources().getStringArray(R.array.msg_true);
            modelContent = new ModelContent();
            MapContenido();
            Collections.sort(modelContentsEnun, new Comparator<ModelContent>() {
                @Override
                public int compare(ModelContent e1, ModelContent e2) {
                    return new Integer(e1.getId()).compareTo(new Integer(e2.getId()));
                }
            });
            if (modelContentsEnun.size() > 0 & modelContentsOp.size() > 0 & respuestas.size() > 0) {
                adpEnunciado = new AdpStatement(getContext(), modelContentsEnun);
                lstLista.setAdapter(adpEnunciado);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ReproduceEnunciado();
                    }
                }, 1200);
                imgAudio.setOnClickListener(v -> ReproduceEnunciado());
                Glide.with(getContext())
                        .load(imgEnunciado.getMultimedia().getJSONObject(0).getString("url"))
                        .into(img);
                adpOptionReconocerImg = new AdpOptionRecognizeImg(getContext(), modelContentsOp);
                rcvOption.setAdapter(adpOptionReconocerImg);
                adpOptionReconocerImg.setOnClickListener(this);
            } else {
                Toast.makeText(getContext(), "La actividad no tiene contenido", Toast.LENGTH_SHORT).show();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ClssConvertTextToSpeech.getIntancia(getContext()).reproduce("La actividad no tiene contenido");
                        //tts.reproduce("La actividad no tiene contenido");
                    }
                }, 1000);
                img.setVisibility(View.GONE);
                state.getLayoutParams().height = LinearLayout.LayoutParams.MATCH_PARENT;
                state.setGravity(Gravity.BOTTOM);
                state.setVisibility(View.VISIBLE);
                state.setBackgroundColor(Color.WHITE);
                state.getChildAt(0).setVisibility(View.GONE);
                state.getChildAt(1).setVisibility(View.GONE);
                state.getChildAt(2).setVisibility(View.GONE);
                state.getChildAt(3).setVisibility(View.VISIBLE);
                state.getChildAt(3).setBackgroundColor(Color.parseColor("#44CCCC"));
                btnContinuar.setOnClickListener(v -> Navegacion(v));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
                if (contenido.getJSONObject(i).get("enunciado").equals(true)) {
                    if (((JSONArray) contenido.getJSONObject(i).get("multimedia")).length() > 0)
                        imgEnunciado = modelContent;
                    else
                        modelContentsEnun.add(modelContent);
                } else {
                    modelContentsOp.add(modelContent);
                    if (contenido.getJSONObject(i).get("respuesta").equals(true))
                        respuestas.add(modelContent);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void ReproduceEnunciado(){
        int count = lstLista.getCount();
        for(int c = 0; c < count; c++){
            enun = ((ModelContent) lstLista.getItemAtPosition(c));
            mensaje = mensaje + " " + enun.getDescripcion();
        }
        ClssConvertTextToSpeech.getIntancia(getContext()).reproduce(mensaje);
        mensaje = "";
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_toolbar, menu);
        mr = menu.findItem(R.id.btnRecompensa);
        mr.setTitle(String.valueOf(ModelUser.stockcaritas));
    }

    private String generarAleatorio() {
        Random random = new Random();
        String r = msg_true[random.nextInt(msg_true.length)];
        return r;
    }

    private void Navegacion(View v) {
        try {
            navController = Navigation.findNavController(v);
            //Eliminamos el item por el cual nos redirecccionamos aca
            jsonActivities.remove(0);
            ClssNavegacionActividades clssNavegacionActividades = new ClssNavegacionActividades(navController, jsonActivities, v);
            clssNavegacionActividades.navegar();
        } catch (Exception ex) {}
    }

    private void AccionOk() {
        try {
            animar(false);
            adpOptionReconocerImg.setOnClickListener(this);
            lySel.setBackgroundResource(R.drawable.borde);
            lySel.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
            scrollView.post(new Runnable() {
                public void run() {
                    scrollView.scrollTo(0, scrollView.getTop());
                }
            });
            state.setVisibility(View.GONE);
            TextView txt = (TextView) state.getChildAt(2);
            ClssConvertTextToSpeech.getIntancia(getContext()).reproduce(txt.getText().toString());
        } catch (Exception e) {}
    }

    private void animar(boolean mostrar) {
        try {
            /*AnimationSet set = new AnimationSet(true);
            Animation animation = null;*/
            if (mostrar) {
                state.setLayoutAnimation(ClssAnimation.getInstanciaAnimation().getLayoutAnimationController());
                state.startAnimation(ClssAnimation.getInstanciaAnimation().getAnimationDown());
                /*animation = new TranslateAnimation(
                        Animation.RELATIVE_TO_SELF, 0.0f,
                        Animation.RELATIVE_TO_SELF, 0.0f,
                        Animation.RELATIVE_TO_SELF, 1.0f,
                        Animation.RELATIVE_TO_SELF, 0.0f);*/
            } else {
                state.setLayoutAnimation(ClssAnimation.getInstanciaAnimation().getLayoutAnimationController());
                state.startAnimation(ClssAnimation.getInstanciaAnimation().getAnimationUp());
                /*animation = new TranslateAnimation(
                        Animation.RELATIVE_TO_SELF, 0.0f,
                        Animation.RELATIVE_TO_SELF, 0.0f,
                        Animation.RELATIVE_TO_SELF, 0.0f,
                        Animation.RELATIVE_TO_SELF, 1.0f);*/
            }
            //duración en milisegundos
            /*animation.setDuration(500);
            set.addAnimation(animation);
            LayoutAnimationController controller = new LayoutAnimationController(set, 0.25f);
            state.setLayoutAnimation(controller);
            state.startAnimation(animation);*/
        } catch (Exception e) {}
    }

    private void CompleteActivity(View v) {
        try {
            //Parámetros a enviar a la API
            JSONObject param = new JSONObject();
            param.put("idEstudiante", ClssStaticGroup.idestudiante);
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
                                    //Actualice el itemMenú creado
                                    mr.setTitle(String.valueOf(ModelUser.stockcaritas));
                                    //Toast.makeText(getContext(), "Actividad exitosa", Toast.LENGTH_LONG).show();
                                } //else
                                    //Toast.makeText(getContext(), response.get("message").toString(), Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                //Toast.makeText(getContext(), "Error de conexión [" + e.getMessage() + "]", Toast.LENGTH_LONG).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.e("Error: ", error.getMessage());
                    //Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
            ClssVolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(request_json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*@Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        try {
            opSelected = ((ModelContent) adapterView.getItemAtPosition(i));
            lstOptions.setClickable(false);
            lstOptions.setOnItemClickListener(null);
            Toast.makeText(getContext(),opSelected.getDescripcion().trim(),Toast.LENGTH_SHORT);
            ClssConvertTextToSpeech.getIntancia(getContext()).reproduce(opSelected.getDescripcion());
            if (respuestas.size() == 1) {
                if (opSelected.getRespuesta().equals(true)) {
                    lstOptions.getChildAt(i).setBackgroundResource(R.drawable.borde);
                    lstOptions.getChildAt(i).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#44CCCC")));
                    respuesta = true;
                    animar(true);
                    scrollView.post(new Runnable() {
                        public void run() {
                            scrollView.scrollTo(0, scrollView.getBottom());
                        }
                    });
                    //Seteamos el background verde
                    state.setBackgroundColor(Color.parseColor("#AAFAB1"));
                    //Seteamos el texto de continuar y lo mostramos
                    TextView txt = (TextView) state.getChildAt(0);
                    txt.setText(generarAleatorio());
                    txt.setTextColor(Color.parseColor("#048710"));
                    txt.setVisibility(View.VISIBLE);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ClssConvertTextToSpeech.getIntancia(getContext()).reproduce(txt.getText().toString());
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
                    lstOptions.getChildAt(i).setBackgroundResource(R.drawable.borde);
                    lstOptions.getChildAt(i).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#C70039")));
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
                    txt.setText("¡Ups! Vuelve a intentarlo");
                    txt.setTextColor(Color.parseColor("#C70039"));
                    txt.setVisibility(View.VISIBLE);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ClssConvertTextToSpeech.getIntancia(getContext()).reproduce(txt.getText().toString());
                        }
                    }, 1000);
                    ImageView img = (ImageView) state.getChildAt(1);
                    img.setImageResource(R.drawable.sad);
                    img.setColorFilter(Color.parseColor("#C70039"));
                    //Ocultamos el boton comprobar
                    state.getChildAt(3).setVisibility(View.GONE);
                    //Seteamos evento click a boton OK
                    state.getChildAt(2).setVisibility(View.VISIBLE);
                    state.getChildAt(2).setOnClickListener(vok -> AccionOk(i));
                    state.setVisibility(View.VISIBLE);
                }
            } else {
            }
        } catch (Exception e){
            lstOptions.setOnItemClickListener(this);
        }
    }*/


    @Override
    public void onClick(View view) {
        try {
            //int opcselec = rcvOptions.getChildAdapterPosition(view);
            rlSel = (RelativeLayout) view;
            lySel = (LinearLayout) rlSel.getParent();
            int opcselec = rlSel.getId();
            opSelected = modelContentsOp.get(opcselec);
            adpOptionReconocerImg.setOnClickListener(null);
            Toast.makeText(getContext(), opSelected.getDescripcion().trim(), Toast.LENGTH_SHORT);
            ClssConvertTextToSpeech.getIntancia(getContext()).reproduce(opSelected.getDescripcion());
            if (respuestas.size() == 1) {
                if (opSelected.getRespuesta().equals(true)) {
                    lySel.setBackgroundResource(R.drawable.borde);
                    lySel.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#44CCCC")));
                    /*rcvOptions.getChildAt(opcselec).setBackgroundResource(R.drawable.borde);
                    rcvOptions.getChildAt(opcselec).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#44CCCC")));*/
                    respuesta = true;
                    state.setVisibility(View.VISIBLE);
                    scrollView.post(new Runnable() {
                        public void run() {
                            scrollView.scrollTo(0, scrollView.getBottom());
                        }
                    });
                    //Seteamos el background verde
                    state.setBackgroundColor(Color.parseColor("#AAFAB1"));
                    //Seteamos el texto de continuar y lo mostramos
                    TextView txt = (TextView) state.getChildAt(0);
                    txt.setText(generarAleatorio());
                    txt.setTextColor(Color.parseColor("#048710"));
                    txt.setVisibility(View.VISIBLE);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ClssConvertTextToSpeech.getIntancia(getContext()).reproduce(txt.getText().toString());
                        }
                    }, 1000);
                    ImageView img = (ImageView) state.getChildAt(1);
                    img.setImageResource(R.drawable.icon_valor);
                    img.setColorFilter(Color.parseColor("#048710"));

                    state.getChildAt(3).setVisibility(View.VISIBLE);
                    state.getChildAt(2).setVisibility(View.GONE);
                    state.getChildAt(3).setOnClickListener(vcont -> Navegacion(vcont));
                    //Ubicamos el layout visible
                    state.setVisibility(View.VISIBLE);
                    animar(true);
                    CompleteActivity(view);
                } else {
                    lySel.setBackgroundResource(R.drawable.borde);
                    lySel.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#C70039")));
                    /*rcvOptions.getChildAt(opcselec).setBackgroundResource(R.drawable.borde);
                    rcvOptions.getChildAt(opcselec).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#C70039")));*/
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
                    txt.setText("¡Ups! Vuelve a intentarlo");
                    txt.setTextColor(Color.parseColor("#C70039"));
                    txt.setVisibility(View.VISIBLE);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ClssConvertTextToSpeech.getIntancia(getContext()).reproduce(txt.getText().toString());
                        }
                    }, 1000);
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
            } else if (respuestas.size() > 1) {
                resp.add(opSelected);
            }
        } catch (Exception e) {
            //Toast.makeText(getContext(),e.toString(),Toast.LENGTH_SHORT).show();
            adpOptionReconocerImg.setOnClickListener(this);
        }
    }
}
