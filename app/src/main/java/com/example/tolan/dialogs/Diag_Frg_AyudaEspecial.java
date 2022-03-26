package com.example.tolan.dialogs;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.example.tolan.R;
import com.example.tolan.clases.ClssConvertTextToSpeech;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Locale;

public class Diag_Frg_AyudaEspecial extends DialogFragment {

    Activity actividad;
    FloatingActionButton close;
    TextView txt_titulo_original;
    ImageView img_original, img_ayudaEspecial, gif_ayudaEspecial;
    String respuesta, url_img_ayuda, frg_actividad;
    Context mcontext;
    List<String> lst_url_img_ayuda;
    String response;
    static TextToSpeech textToSpeech;

    public Diag_Frg_AyudaEspecial(String response, String respuesta, List<String> lst_url_img_ayuda, String frg_actividad) {
        this.response = response;
        this.respuesta = respuesta;
        this.lst_url_img_ayuda = lst_url_img_ayuda;
        this.frg_actividad = frg_actividad;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        textToSpeech = new TextToSpeech(getContext(), i -> reproducirAudio(i, respuesta));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diag_ayuda_especial, container,false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        close = view.findViewById(R.id.close);
        txt_titulo_original = view.findViewById(R.id.txt_titulo_original);
        txt_titulo_original.setText(respuesta);
        close.setOnClickListener(v -> Cerrar());
        img_original = view.findViewById(R.id.img_original);
        img_original.setOnClickListener(v -> ClssConvertTextToSpeech.getIntancia(getContext()).reproduce(respuesta));
        Glide.with(getContext())
                .load(response)
                .into(img_original);
        gif_ayudaEspecial = view.findViewById(R.id.gif_ayudaEspecial);
        Glide.with(getContext())
                .load(R.drawable.gif_senias)
                .into(gif_ayudaEspecial);
        if (lst_url_img_ayuda != null) {
            img_ayudaEspecial = view.findViewById(R.id.img_ayudaEspecial);
            if (lst_url_img_ayuda.size() > 1) {
                Glide.with(getContext())
                        .load(lst_url_img_ayuda.get(0))
                        .into(img_ayudaEspecial);
                Glide.with(getContext())
                        .load(lst_url_img_ayuda.get(1))
                        .into(gif_ayudaEspecial);
            } else if (lst_url_img_ayuda.size() == 1){
                Glide.with(getContext())
                        .load(lst_url_img_ayuda.get(0))
                        .into(img_ayudaEspecial);
            }
        }
        return view;
    }

    public void reproducirAudio(int i, String mensaje) {
        if (i != TextToSpeech.ERROR) {
            textToSpeech.setLanguage(Locale.getDefault());
            textToSpeech.speak(mensaje, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    private void Cerrar() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ClssConvertTextToSpeech.getIntancia(getContext()).reproduce("Cerrar");
                dismiss();
            }
        }, 100);
    }

    /*@NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return crearDialogoAyudaEspecial();
    }

    private AlertDialog crearDialogoAyudaEspecial() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View v = layoutInflater.inflate(R.layout.fragment_diag_ayuda_especial, null);
        builder.setView(v);
        close = v.findViewById(R.id.close);
        txt_titulo_original = v.findViewById(R.id.txt_titulo_original);
        txt_titulo_original.setText(respuesta);

        img_original = v.findViewById(R.id.img_original);
        Glide.with(getContext())
                .load(response)
                .into(img_original);
        //img_original.setImageBitmap(response);

        gif_ayudaEspecial = v.findViewById(R.id.gif_ayudaEspecial);
        Glide.with(getContext())
                .load(R.drawable.gif_senias)
                .into(gif_ayudaEspecial);

        if (lst_url_img_ayuda != null) {
            //Toast.makeText(getContext(), "Size :" + lst_url_img_ayuda.size(), Toast.LENGTH_SHORT).show();
            img_ayudaEspecial = v.findViewById(R.id.img_ayudaEspecial);

            if (lst_url_img_ayuda.size() > 1) {
                Glide.with(getContext())
                        .load(lst_url_img_ayuda.get(0))
                        .into(img_ayudaEspecial);
                Glide.with(getContext())
                        .load(lst_url_img_ayuda.get(1))
                        .into(gif_ayudaEspecial);
            } else {
                Glide.with(getContext())
                        .load(lst_url_img_ayuda.get(0))
                        .into(img_ayudaEspecial);
            }
        }

        //Glide.with(getContext()).load(url).into(img_respuesta);

        //vid_ayudaEspecial = v.findViewById(R.id.vid_ayudaEspecial);
        //LoadVideo();


        //txt_respuesta.setText(respuesta);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return builder.create();
    }

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