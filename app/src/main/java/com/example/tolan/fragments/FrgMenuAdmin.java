package com.example.tolan.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.tolan.R;
import com.example.tolan.clases.ClssConvertTextToSpeech;

public class FrgMenuAdmin extends Fragment {

    private Fragment fragment;
    Toolbar toolbar;
    private ImageView iconNiveles, iconSubNiveles, iconActividades, iconSkins, iconGrupos, iconHistorial, iconContenido;

    public FrgMenuAdmin() {
        // Required empty public constructor
    }

    public static FrgMenuAdmin newInstance(String param1, String param2) {
        FrgMenuAdmin fragment = new FrgMenuAdmin();
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
        View view = inflater.inflate(R.layout.fragment_menu_admin, container, false);
        try {
            toolbar = view.findViewById(R.id.toolbar);
            setHasOptionsMenu(true);
            ((AppCompatActivity) this.getActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity) this.getActivity()).getSupportActionBar().setTitle("");
            iconNiveles = view.findViewById(R.id.iconNiveles);
            iconNiveles.setOnClickListener(v -> {
                OptionsMenuAdmin(v);
            });
            iconSubNiveles = view.findViewById(R.id.iconSubNiveles);
            iconSubNiveles.setOnClickListener(v -> {
                OptionsMenuAdmin(v);
            });
            iconActividades = view.findViewById(R.id.iconActividades);
            iconActividades.setOnClickListener(v -> {
                OptionsMenuAdmin(v);
            });
            /*
            iconSkins = view.findViewById(R.id.iconSkins);
            iconSkins.setOnClickListener(v -> {
                OptionsMenuAdmin(v);
            });
            */
            iconGrupos = view.findViewById(R.id.iconGrupos);
            iconGrupos.setOnClickListener(v -> {
                OptionsMenuAdmin(v);
            });
            iconHistorial = view.findViewById(R.id.iconHistorial);
            iconHistorial.setOnClickListener(v -> {
                OptionsMenuAdmin(v);
            });
            iconContenido = view.findViewById(R.id.iconContenido);
            iconContenido.setOnClickListener(v -> {
                OptionsMenuAdmin(v);
            });
        } catch (Exception e) {
        }
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_toolbar, menu);
        MenuItem mc = menu.findItem(R.id.btnCaritas);
        mc.setVisible(false);
        MenuItem mr = menu.findItem(R.id.btnRecompensa);
        mr.setVisible(false);
        MenuItem mi = menu.findItem(R.id.btnMyInfo);
        mi.setVisible(false);
    }

    public void OptionsMenuAdmin(View view) {
        int tag = Integer.parseInt(view.getTag().toString());
        if (tag == 1) {
            fragment = new FrgLevel();
            ClssConvertTextToSpeech.getIntancia(getContext()).reproduce("Niveles");
            getFragmentManager().beginTransaction().replace(R.id.content, fragment).addToBackStack(null).commit();
        } else if (tag == 2) {
            fragment = new FrgSublevel();
            ClssConvertTextToSpeech.getIntancia(getContext()).reproduce("Subniveles");
            getFragmentManager().beginTransaction().replace(R.id.content, fragment).addToBackStack(null).commit();
        } else if (tag == 3) {
            fragment = new FrgActividad();
            ClssConvertTextToSpeech.getIntancia(getContext()).reproduce("Actividades");
            getFragmentManager().beginTransaction().replace(R.id.content, fragment).addToBackStack(null).commit();

        } else if (tag == 4) {
            //Esto corresponde a la seccion articulos
            //Intent intent = new Intent(getContext(), ActivitySkin.class);
            //ClssConvertTextToSpeech.getIntancia(getContext()).reproduce("Art??culos");
            //startActivity(intent);
        } else if (tag == 5) {
            fragment = new FrgGroup();
            ClssConvertTextToSpeech.getIntancia(getContext()).reproduce("Grupos");
            getFragmentManager().beginTransaction().replace(R.id.content, fragment).addToBackStack(null).commit();
        } else if (tag == 6) {
            fragment = new FrgHistorial();
            ClssConvertTextToSpeech.getIntancia(getContext()).reproduce("Historial de Actividades");
            getFragmentManager().beginTransaction().replace(R.id.content, fragment).addToBackStack(null).commit();

        } else if (tag == 7) {
            fragment = new FrgContent();
            ClssConvertTextToSpeech.getIntancia(getContext()).reproduce("Contenido de Actividades");
            getFragmentManager().beginTransaction().replace(R.id.content, fragment).addToBackStack(null).commit();
        }
    }
}