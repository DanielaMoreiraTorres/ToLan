package com.example.tolan.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.tolan.R;
import com.example.tolan.clases.ClssGifImageView;


public class Diag_Frg_AyudaEspecial extends DialogFragment {


    Activity actividad;
    Button btn_comprobar;
    TextView txt_titulo_original;
    ImageView img_original;
    String respuesta, url, frg_actividad;
    Context mcontext;


    Bitmap response;

    public Diag_Frg_AyudaEspecial(Bitmap response, String respuesta, String url, String frg_actividad) {
        this.response = response;
        this.respuesta = respuesta;
        this.url = url;
        this.frg_actividad = frg_actividad;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return crearDialogoAyudaEspecial();
    }

    private AlertDialog crearDialogoAyudaEspecial() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View v = layoutInflater.inflate(R.layout.fragment_diag_ayuda_especial, null);
        builder.setView(v);
        btn_comprobar = v.findViewById(R.id.btn_comprobar);
        txt_titulo_original = v.findViewById(R.id.txt_titulo_original);
        txt_titulo_original.setText(respuesta);

        img_original = v.findViewById(R.id.img_original);
        img_original.setImageBitmap(response);

        //Glide.with(getContext()).load(url).into(img_respuesta);

        //vid_ayudaEspecial = v.findViewById(R.id.vid_ayudaEspecial);
        //LoadVideo();
        ClssGifImageView gifImageView = (ClssGifImageView) v.findViewById(R.id.gif_ayudaEspecial);
        gifImageView.setGifImageResource(R.drawable.gif_senias);

        //txt_respuesta.setText(respuesta);

        btn_comprobar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return builder.create();
    }


    /*
        VideoView vid_ayudaEspecial;
        public void LoadVideo() {

            try {
                MediaController mediaController = new MediaController(getActivity());
                mediaController.setAnchorView(vid_ayudaEspecial);

                vid_ayudaEspecial.setMediaController(mediaController);
                //vid_ayudaEspecial.setVideoPath("https://videocdn.bodybuilding.com/video/mp4/62000/62792m.mp4");
                vid_ayudaEspecial.setVideoURI(Uri.parse("https://www.youtube.com/watch?v=S-cx-IiDUHg&t=4s.mp4"));// use methods to set url
                vid_ayudaEspecial.requestFocus();
                vid_ayudaEspecial.start();
                mediaController.show();
            } catch (Exception ex) {
                System.out.println("Player error :" + ex.getMessage());
            }
            //vid_ayudaEspecial.start();
        }
    */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            this.actividad = (Activity) context;
        } else {
            throw new RuntimeException(context.toString() + "must implement OnFragmentInteractionListener");
        }
    }
}