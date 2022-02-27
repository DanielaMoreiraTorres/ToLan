package com.example.tolan.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.tolan.R;
import com.example.tolan.clases.ClssConvertirTextoAVoz;

import org.imaginativeworld.whynotimagecarousel.CarouselItem;
import org.imaginativeworld.whynotimagecarousel.ImageCarousel;
import org.imaginativeworld.whynotimagecarousel.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FrgWelcome extends Fragment {

    private List list;
    private ImageCarousel carousel;
    static TextToSpeech textToSpeech;
    //ClssConvertirTextoAVoz tts;
    private Button btnIniciar, btnRegistrarse;
    private Fragment fragment;

    public FrgWelcome() {
        // Required empty public constructor
    }

    public static FrgWelcome newInstance(String param1, String param2) {
        FrgWelcome fragment = new FrgWelcome();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        textToSpeech = new TextToSpeech(getContext(), i -> reproducirAudio(i, getString(R.string.bienvenida)));
        if (getArguments() != null) {
        }
    }

    public void reproducirAudio(int i, String mensaje) {
        if (i != TextToSpeech.ERROR) {
            textToSpeech.setLanguage(Locale.getDefault());
            textToSpeech.speak(mensaje, TextToSpeech.QUEUE_FLUSH, null);
        }
        //tts = new ClssConvertirTextoAVoz();
        //tts.init(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_welcome, container, false);
        try {
            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
            carousel = view.findViewById(R.id.carousel);
            btnRegistrarse = view.findViewById(R.id.register);
            btnRegistrarse.setOnClickListener(v -> Register());
            btnIniciar = view.findViewById(R.id.login);
            btnIniciar.setOnClickListener(v -> Iniciar());
            list = new ArrayList();
            list.add(new CarouselItem(R.drawable.aprendizaje, getString(R.string.mensaje_bienvenida_1)));
            list.add(new CarouselItem(R.drawable.nino, getString(R.string.mensaje_bienvenida_2)));
            list.add(new CarouselItem(R.drawable.ninos, getString(R.string.mensaje_bienvenida_3)));
            carousel.addData(list);
            carousel.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onClick(int i, @NonNull CarouselItem carouselItem) {
                    //tts.reproduce(carouselItem.getCaption());
                    ClssConvertirTextoAVoz.getIntancia(getContext()).reproduce(carouselItem.getCaption());
                }

                @Override
                public void onLongClick(int i, @NonNull CarouselItem carouselItem) {
                }
            });
        } catch (Exception e) {
        }
        return view;
    }

    private void Register() {
        //tts.reproduce(getString(R.string.registrarse));
        ClssConvertirTextoAVoz.getIntancia(getContext()).reproduce(getString(R.string.registrarse));
        fragment = new FrgRegisterUser();
        getFragmentManager().beginTransaction().replace(R.id.content, fragment).addToBackStack(null).commit();
    }

    private void Iniciar() {
        //tts.reproduce(getString(R.string.iniciar_sesion));
        ClssConvertirTextoAVoz.getIntancia(getContext()).reproduce(getString(R.string.iniciar_sesion));
        fragment = new FrgLogin();
        getFragmentManager().beginTransaction().replace(R.id.content, fragment).addToBackStack(null).commit();
    }
}