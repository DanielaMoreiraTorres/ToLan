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
import com.example.tolan.ActivityLevel;
import com.example.tolan.ActivitySkin;
import com.example.tolan.ActivitySublevel;
import com.example.tolan.R;

public class FrgMenuAdmin extends Fragment {

    private Toolbar toolbar;
    private Fragment fragment;
    private ImageView iconNiveles, iconSubNiveles, iconActividades, iconSkins, iconGrupos, iconHistorial;

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
        View view = inflater.inflate(R.layout.fragment_frg_menu_admin, container, false);
        setHasOptionsMenu(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
        iconNiveles = view.findViewById(R.id.iconNiveles);
        iconNiveles.setOnClickListener(v->{OptionsMenuAdmin(v);});
        iconSubNiveles = view.findViewById(R.id.iconSubNiveles);
        iconSubNiveles.setOnClickListener(v->{OptionsMenuAdmin(v);});
        iconActividades = view.findViewById(R.id.iconActividades);
        iconActividades.setOnClickListener(v->{OptionsMenuAdmin(v);});
        iconSkins = view.findViewById(R.id.iconSkins);
        iconSkins.setOnClickListener(v->{OptionsMenuAdmin(v);});
        iconGrupos = view.findViewById(R.id.iconGrupos);
        iconGrupos.setOnClickListener(v->{OptionsMenuAdmin(v);});
        iconHistorial = view.findViewById(R.id.iconHistorial);
        iconHistorial.setOnClickListener(v->{OptionsMenuAdmin(v);});
        return view;
    }

    public void OptionsMenuAdmin(View view){
        int tag = Integer.parseInt(view.getTag().toString());
        if(tag == 1) {
            /*Intent intent = new Intent(getContext(), ActivityLevel.class);
            startActivity(intent);*/
            fragment = new FrgLevel();
            getFragmentManager().beginTransaction().replace(R.id.content, fragment).addToBackStack(null).commit();
        }
        else if(tag == 2){
            Intent intent = new Intent(getContext(), ActivitySublevel.class);
            startActivity(intent);
        }
        else if(tag == 3){

        }
        else if(tag == 4){
            Intent intent = new Intent(getContext(), ActivitySkin.class);
            startActivity(intent);
        }
        else if(tag == 5){

        }
        else if(tag == 6){

        }
    }
}