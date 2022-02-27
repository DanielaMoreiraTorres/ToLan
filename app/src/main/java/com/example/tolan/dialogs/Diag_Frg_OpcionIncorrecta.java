package com.example.tolan.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tolan.R;


public class Diag_Frg_OpcionIncorrecta extends DialogFragment {


    Activity actividad;
    Button btn_comprobar;
    TextView txt_respuesta;
    ImageView img_respuesta;
    String respuesta, url;

    public Diag_Frg_OpcionIncorrecta(String respuesta,String url) {
        // Required empty public constructor
        this.respuesta=respuesta;
        this.url=url;
    }
    public Diag_Frg_OpcionIncorrecta(){

    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return crearDialogoError();
    }

    private AlertDialog crearDialogoError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View v = layoutInflater.inflate(R.layout.fragment_diag__frg__opcion_incorrecta, null);
        builder.setView(v);
        btn_comprobar = v.findViewById(R.id.btn_comprobar);
        //txt_respuesta= v.findViewById(R.id.txt_respuesta);
        img_respuesta= v.findViewById(R.id.img_respuesta);
        Glide.with(getContext()).load(url).into(img_respuesta);

        //txt_respuesta.setText(respuesta);

        btn_comprobar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof Activity){
            this.actividad=(Activity) context;
        }else{
            throw new RuntimeException(context.toString()+"must implement OnFragmentInteractionListener");
        }
    }
}