package com.example.tolan.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.example.tolan.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Frg_ArrastrarSoltar#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Frg_ArrastrarSoltar extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Frg_ArrastrarSoltar() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TercerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Frg_ArrastrarSoltar newInstance(String param1, String param2) {
        Frg_ArrastrarSoltar fragment = new Frg_ArrastrarSoltar();
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
        return inflater.inflate(R.layout.fragment_arrastrar_soltar, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final NavController navController = Navigation.findNavController(view);


        view.findViewById(R.id.btn_comprobar_actividades).setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                //Referencia al action
                //navController.navigate(R.id.action_tercerFragment_to_inicioFragment);

                navController.navigate(R.id.inicioFragment,null, new NavOptions.Builder()
                        .setPopUpTo(R.id.inicioFragment, true)
                        .build());

  /*
                NavOptions navOptions = new NavOptions.Builder()
                        .setPopUpTo(R.id.inicioFragment, true)
                        .build();
                navController.navigate(homeDestination, navOptions);
*/


            }
        });
    }
}