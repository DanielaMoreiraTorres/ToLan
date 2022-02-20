package com.example.tolan.fragments;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.graphics.drawable.AnimatedStateListDrawableCompat;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tolan.R;

import com.example.tolan.adapters.AdpRecycler_SeleccionarPares;
import com.example.tolan.dialogs.Diag_Frg_OpcionIncorrecta;
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
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Frg_SeleccionarParesImagenTexto#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Frg_SeleccionarParesImagenTexto extends Fragment implements View.OnClickListener {


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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        rcv_datosSeleccionarPares = view.findViewById(R.id.rcv_datosSeleccionarPares);

        txt_enunciado = view.findViewById(R.id.txt_enunciado);

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
            JSONArray contenido = item.getJSONArray("contenido");
            for (int i = 0; i < contenido.length(); i++) {
                JSONObject item_contenido = contenido.getJSONObject(i);
                boolean isenunciado = item_contenido.getBoolean("enunciado");
                if (isenunciado) {
                    txt_enunciado.setText(item_contenido.getString("descripcion"));
                    //Toast.makeText(getContext()," El item : ["+item_contenido.getString("descripcion")+"] es un enunciado",Toast.LENGTH_LONG).show();
                } else {
                    listItemsMultimedia.add(item_contenido.getString("descripcion"));
                    JSONArray multimedia_contenido = item_contenido.getJSONArray("multimedia");
                    for (int j = 0; j < multimedia_contenido.length(); j++) {
                        JSONObject item_multimedia_contenido = multimedia_contenido.getJSONObject(j);
                        listRutasMultimedia.add(item_multimedia_contenido.getString("url"));
                        System.out.println("ok");

                    }

                    //Toast.makeText(getContext(),"["+item_contenido.getString("descripcion")+"]",Toast.LENGTH_LONG).show();

                }

            }

            for (int i = 0; i < listRutasMultimedia.size(); i++) {
                map_DatosEmparejados.put(listRutasMultimedia.get(i), listItemsMultimedia.get(i));
            }
            Collections.shuffle(listRutasMultimedia);
            Collections.shuffle(listItemsMultimedia);
            AdpRecycler_SeleccionarPares adpRecycler_seleccionarPares = new AdpRecycler_SeleccionarPares(getContext(), listItemsMultimedia, listRutasMultimedia, map_DatosEmparejados,this);
            rcv_datosSeleccionarPares.setAdapter(adpRecycler_seleccionarPares);


        } catch (JSONException e) {
            e.printStackTrace();
        }


        view.findViewById(R.id.btn_comprobar_actividades).setOnClickListener(this);
    }


    NavController navController;

    @Override
    public void onClick(View v) {




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