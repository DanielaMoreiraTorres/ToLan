package com.example.tolan.fragments;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tolan.R;
import com.example.tolan.adapters.AdpRecycler_SeleccionarParesImagenImagen;
import com.example.tolan.adapters.AdpRecycler_SeleccionarParesTextoImagen;
import com.example.tolan.clases.ClssNavegacionActividades;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.RandomAccess;
import java.util.Set;
import java.util.stream.IntStream;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Frg_SeleccionarParesImagenImagen#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Frg_SeleccionarParesImagenImagen extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Frg_SeleccionarParesImagenImagen() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Frg_SeleccionarParesImagenImagen.
     */
    // TODO: Rename and change types and number of parameters
    public static Frg_SeleccionarParesImagenImagen newInstance(String param1, String param2) {
        Frg_SeleccionarParesImagenImagen fragment = new Frg_SeleccionarParesImagenImagen();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_seleccionar_pares_imagen_imagen, container, false);
    }


    JSONArray jsonActivities;


    ArrayList<String> listElements;
    RecyclerView rcv_datosSeleccionarPares;
    TextView txt_enunciado;
    LinearLayout ry_state;

    ScrollView mScrollView;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        ry_state = view.findViewById(R.id.ry_state);
        mScrollView = view.findViewById(R.id.mScrollView);
        //ry_state.setVisibility(View.GONE);

        rcv_datosSeleccionarPares = view.findViewById(R.id.rcv_datosSeleccionarParesImagen);

        txt_enunciado = view.findViewById(R.id.txt_enunciadoImagen);


        listElements = new ArrayList<>();
        try {
            String lst_Activities = getArguments().getString("activities");
            jsonActivities = new JSONArray(lst_Activities);
            //Seleccionamos el elemanto cero que corresponde a esta actividad
            JSONObject item = jsonActivities.getJSONObject(0);
            JSONArray contenido = item.getJSONArray("contenido");
            for (int i = 0; i < contenido.length(); i++) {
                JSONObject item_contenido = contenido.getJSONObject(i);
                boolean isenunciado = item_contenido.getBoolean("enunciado");
                if (isenunciado) {
                    txt_enunciado.setText(item_contenido.getString("descripcion"));
                    //Toast.makeText(getContext()," El item : ["+item_contenido.getString("descripcion")+"] es un enunciado",Toast.LENGTH_LONG).show();
                } else {
                    //listElements.add(item_contenido.getString("descripcion"));
                    JSONArray multimedia_contenido = item_contenido.getJSONArray("multimedia");
                    for (int j = 0; j < multimedia_contenido.length(); j++) {
                        JSONObject item_multimedia_contenido = multimedia_contenido.getJSONObject(j);
                        listElements.add(item_multimedia_contenido.getString("url"));
                        System.out.println("ok");
                    }
                }

            }


            //Genero un vector con numeros aleatorios
            getShuffleNumbers(listElements);
            AdpRecycler_SeleccionarParesImagenImagen adpRecycler_seleccionarParesImagenImagen = new AdpRecycler_SeleccionarParesImagenImagen(getContext(), listElements, numerosAleatorios, ry_state, mScrollView);
            rcv_datosSeleccionarPares.setAdapter(adpRecycler_seleccionarParesImagenImagen);


        } catch (JSONException e) {
            e.printStackTrace();
        }


        view.findViewById(R.id.btn_comprobar_actividadesImagen).setOnClickListener(this);
    }

    int[] numerosAleatorios;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void getShuffleNumbers(ArrayList<String> list) {

        //Algoritmo de Fisher Rates
        numerosAleatorios = IntStream.rangeClosed(0, list.size() - 1).toArray();
        //desordenando los elementos
        Random r = new Random();
        for (int i = numerosAleatorios.length; i > 0; i--) {
            int posicion = r.nextInt(i);
            int tmp = numerosAleatorios[i - 1];
            numerosAleatorios[i - 1] = numerosAleatorios[posicion];
            numerosAleatorios[posicion] = tmp;
        }
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