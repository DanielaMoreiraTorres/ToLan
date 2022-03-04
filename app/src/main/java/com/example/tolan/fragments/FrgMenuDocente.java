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
import android.widget.TextView;

import com.example.tolan.R;
import com.example.tolan.clases.ClssConvertirTextoAVoz;

public class FrgMenuDocente extends Fragment {

    private Toolbar toolbar;
    private Fragment fragment;
    private TextView txtMenu;
    private ImageView iconActividades, iconUsuarios, iconGrupos, iconHistorial;
    private Bundle bundle;

    public FrgMenuDocente() {
        // Required empty public constructor
    }

    public static FrgMenuDocente newInstance(String param1, String param2) {
        FrgMenuDocente fragment = new FrgMenuDocente();
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
        View view = inflater.inflate(R.layout.fragment_menu_docente, container, false);
        try {
            toolbar = view.findViewById(R.id.toolbar);
            setHasOptionsMenu(true);
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("");
            txtMenu = view.findViewById(R.id.txtMenu);
            txtMenu.setOnClickListener(v -> ClssConvertirTextoAVoz.getIntancia(v.getContext()).reproduce(txtMenu.getText().toString()));
            iconActividades = view.findViewById(R.id.iconActividades);
            iconActividades.setOnClickListener(v -> {
                OptionsMenuDocente(v);
            });
            iconUsuarios = view.findViewById(R.id.iconUsuarios);
            iconUsuarios.setOnClickListener(v -> {
                OptionsMenuDocente(v);
            });
            iconGrupos = view.findViewById(R.id.iconGrupos);
            iconGrupos.setOnClickListener(v -> {
                OptionsMenuDocente(v);
            });
            iconHistorial = view.findViewById(R.id.iconHistorial);
            iconHistorial.setOnClickListener(v -> {
                OptionsMenuDocente(v);
            });
        } catch (Exception e) {}
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_toolbar, menu);
        MenuItem mc = menu.findItem(R.id.btnCaritas);
        mc.setVisible(false);
        MenuItem mr = menu.findItem(R.id.btnRecompensa);
        mr.setVisible(false);
    }

    public void OptionsMenuDocente(View view){
        int tag = Integer.parseInt(view.getTag().toString());
        if(tag == 1) {
            ClssConvertirTextoAVoz.getIntancia(getContext()).reproduce("Actividades");
        }
        else if(tag == 2){
            fragment = new FrgRegisterUser();
            bundle = new Bundle();
            bundle.putBoolean("registrar", true);
            fragment.setArguments(bundle);
            ClssConvertirTextoAVoz.getIntancia(getContext()).reproduce("Registrar estudiante");
            getFragmentManager().beginTransaction().replace(R.id.content, fragment).addToBackStack(null).commit();
        }
        else if(tag == 3){
            fragment = new FrgStudent();
            ClssConvertirTextoAVoz.getIntancia(getContext()).reproduce("Mis estudiantes");
            getFragmentManager().beginTransaction().replace(R.id.content, fragment).addToBackStack(null).commit();
        }
        else if(tag == 4){
            fragment = new Frg_Historial();
            ClssConvertirTextoAVoz.getIntancia(getContext()).reproduce("Historial");
            getFragmentManager().beginTransaction().replace(R.id.content, fragment).addToBackStack(null).commit();
        }
    }
}