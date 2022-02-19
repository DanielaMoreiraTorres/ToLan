package com.example.tolan.fragments;

import android.content.Intent;
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

import com.example.tolan.ActivityContact;
import com.example.tolan.R;

public class FrgMenuDocente extends Fragment {

    private Toolbar toolbar;
    private Fragment fragment;
    private ImageView iconActividades, iconUsuarios, iconGrupos, iconHistorial;

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
        View view = inflater.inflate(R.layout.fragment_frg_menu_docente, container, false);
        toolbar = view.findViewById(R.id.toolbar);
        setHasOptionsMenu(true);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("");
        iconActividades = view.findViewById(R.id.iconActividades);
        iconActividades.setOnClickListener(v->{OptionsMenuDocente(v);});
        iconUsuarios = view.findViewById(R.id.iconUsuarios);
        iconUsuarios.setOnClickListener(v->{OptionsMenuDocente(v);});
        iconGrupos = view.findViewById(R.id.iconGrupos);
        iconGrupos.setOnClickListener(v->{OptionsMenuDocente(v);});
        iconHistorial = view.findViewById(R.id.iconHistorial);
        iconHistorial.setOnClickListener(v->{OptionsMenuDocente(v);});
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        ((AppCompatActivity)getActivity()).getMenuInflater().inflate(R.menu.menu_toolbar,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.btnNotifi) {

        }
        if(id == R.id.btnLogIn) {
            fragment = new FrgLogin();
            getFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
        }
        if(id == R.id.btnContacts) {
            Intent intent = new Intent(getContext(), ActivityContact.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void OptionsMenuDocente(View view){
        int tag = Integer.parseInt(view.getTag().toString());
        if(tag == 1) {

        }
        else if(tag == 2){
            fragment = new FrgRegisterUser();
            getFragmentManager().beginTransaction().replace(R.id.content, fragment).addToBackStack(null).commit();
        }
        else if(tag == 3){

        }
        else if(tag == 4){

        }
    }
}