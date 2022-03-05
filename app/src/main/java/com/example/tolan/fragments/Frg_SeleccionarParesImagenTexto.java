package com.example.tolan.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tolan.R;


import com.example.tolan.adapters.AdpRecycler_SeleccionarParesTextoImagen;
import com.example.tolan.clases.ClssConvertirTextoAVoz;
import com.example.tolan.clases.ClssNavegacionActividades;
import com.example.tolan.models.ModelUser;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Frg_SeleccionarParesImagenTexto#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Frg_SeleccionarParesImagenTexto extends Fragment implements View.OnClickListener {

    private Toolbar toolbar;
    static TextToSpeech textToSpeech;
    //ClssConvertirTextoAVoz tts;
    private TextView titulo;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Frg_SeleccionarParesImagenTexto() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Frg_SeleccionarParesImagenTexto.
     */
    // TODO: Rename and change types and number of parameters
    public static Frg_SeleccionarParesImagenTexto newInstance(String param1, String param2) {
        Frg_SeleccionarParesImagenTexto fragment = new Frg_SeleccionarParesImagenTexto();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        textToSpeech = new TextToSpeech(getContext(), i -> reproducirAudio(i, titulo.getText().toString()));
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public void reproducirAudio(int i, String mensaje) {
        if (i != TextToSpeech.ERROR) {
            textToSpeech.setLanguage(Locale.getDefault());
            textToSpeech.speak(mensaje, TextToSpeech.QUEUE_FLUSH, null);
        }
        //tts = new ClssConvertirTextoAVoz();
        //tts.init(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_seleccionar_pares_imagen_texto, container, false);
    }


    JSONArray jsonActivities;


    ArrayList<String> listRutasMultimedia, listItemsMultimedia;
    RecyclerView rcv_datosSeleccionarPares;
    TextView txt_enunciado;

    Map<String, String> map_DatosEmparejados = new HashMap<>();
    CardView cardview_imagen;
    LinearLayout ry_state;

    int idActividad;

    Map<String, String> map_MultimediaExtra = new HashMap<>();
    String urlInicial;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        titulo = view.findViewById(R.id.txtMenu);
        titulo.setOnClickListener(v -> ClssConvertirTextoAVoz.getIntancia(v.getContext()).reproduce(titulo.getText().toString()));
        //tts.reproduce(titulo.getText().toString()));

        toolbar = view.findViewById(R.id.toolbar);
        setHasOptionsMenu(true);
        ((AppCompatActivity) this.getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) this.getActivity()).getSupportActionBar().setTitle("");

        rcv_datosSeleccionarPares = view.findViewById(R.id.rcv_datosSeleccionarPares);

        txt_enunciado = view.findViewById(R.id.txt_enunciado);
        ry_state = view.findViewById(R.id.ry_state);

        //Centrar los elementos
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getContext());
        layoutManager.setJustifyContent(JustifyContent.SPACE_AROUND);
        layoutManager.setAlignItems(AlignItems.CENTER);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        rcv_datosSeleccionarPares.setLayoutManager(layoutManager);


        listItemsMultimedia = new ArrayList<>();
        listRutasMultimedia = new ArrayList<>();
        try {
            String lst_Activities = getArguments().getString("activities");
            jsonActivities = new JSONArray(lst_Activities);
            //Seleccionamos el elemanto cero que corresponde a esta actividad
            JSONObject item = jsonActivities.getJSONObject(0);
            idActividad = item.getInt("id");
            JSONArray contenido = item.getJSONArray("contenido");
            for (int i = 0; i < contenido.length(); i++) {
                JSONObject item_contenido = contenido.getJSONObject(i);
                boolean isenunciado = item_contenido.getBoolean("enunciado");
                if (isenunciado) {
                    //Elimino los que son de tipo enunciado
                    //contenido.remove(i);
                    txt_enunciado.setText(item_contenido.getString("descripcion"));
                    txt_enunciado.setOnClickListener(v -> ClssConvertirTextoAVoz.getIntancia(v.getContext()).reproduce(txt_enunciado.getText().toString()));
                    //tts.reproduce(txt_enunciado.getText().toString()));
                } else {
                    listItemsMultimedia.add(item_contenido.getString("descripcion"));
                    JSONArray multimedia_contenido = item_contenido.getJSONArray("multimedia");
                    for (int j = 0; j < multimedia_contenido.length(); j++) {
                        JSONObject item_multimedia_contenido = multimedia_contenido.getJSONObject(j);

                        if (item_multimedia_contenido.getBoolean("inicial")) {
                            listRutasMultimedia.add(item_multimedia_contenido.getString("url"));
                            urlInicial=item_multimedia_contenido.getString("url");
                        } else {
                            Toast.makeText(view.getContext(), item_multimedia_contenido.getString("descripcion"), Toast.LENGTH_LONG).show();
                            map_MultimediaExtra.put(urlInicial, item_multimedia_contenido.getString("url"));
                        }
                    }
                }
            }

            //prepararJson(contenido);

            for (int i = 0; i < listRutasMultimedia.size(); i++) {
                map_DatosEmparejados.put(listRutasMultimedia.get(i), listItemsMultimedia.get(i));
            }
            Collections.shuffle(listRutasMultimedia);
            Collections.shuffle(listItemsMultimedia);
            AdpRecycler_SeleccionarParesTextoImagen adpRecycler_seleccionarPares = new AdpRecycler_SeleccionarParesTextoImagen(getContext(), listItemsMultimedia, listRutasMultimedia, map_DatosEmparejados, this, idActividad,map_MultimediaExtra);
            rcv_datosSeleccionarPares.setAdapter(adpRecycler_seleccionarPares);


        } catch (JSONException e) {
            //e.printStackTrace();
            System.out.println(e.getMessage());
        }


        view.findViewById(R.id.btn_comprobar_actividades).setOnClickListener(this);
    }


    List<JSONObject> lst_contenido = new ArrayList<>();
    List<JSONArray> lst_multimedia_contenido;

    public void prepararJson(JSONArray contenido) throws JSONException {


        lst_multimedia_contenido = new ArrayList<>();
        //Eliminar contenido de tipo enunciado
        for (int i = 0; i < contenido.length(); i++) {
            JSONObject item_contenido = contenido.getJSONObject(i);

            boolean isenunciado = item_contenido.getBoolean("enunciado");
            if (isenunciado) {
                //Elimino los que son de tipo enunciado
                contenido.remove(i);
            } else {
            }
        }
        lst_contenido = (List<JSONObject>) contenido;
        //Eliminar items multimedia
        for (int i = 0; i < contenido.length(); i++) {
            JSONObject item_contenido = contenido.getJSONObject(i);
            //lst_contenido.add(item_contenido);
            JSONArray multimedia_contenido = item_contenido.getJSONArray("multimedia");
            lst_multimedia_contenido.add(multimedia_contenido);
            item_contenido.remove("multimedia");
        }
        //Mezclo los elemetos de la lista para hacerlos aleatorios
        Collections.shuffle(lst_multimedia_contenido);


        System.out.println(" \n Contenido \n");
        System.out.println(contenido);

        System.out.println(" \n Multimedia \n");
        System.out.println(lst_multimedia_contenido);

    }


    MenuItem mr;

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_toolbar, menu);
        mr = menu.findItem(R.id.btnRecompensa);
        mr.setTitle(String.valueOf(ModelUser.stockcaritas));
    }


    NavController navController;

    @Override
    public void onClick(View v) {

        navController = Navigation.findNavController(v);
        //Eliminamos el item por el cual nos redirecccionamos aca
        jsonActivities.remove(0);
        ClssNavegacionActividades clssNavegacionActividades = new ClssNavegacionActividades(navController, jsonActivities, v);
        clssNavegacionActividades.navegar();

    }


}