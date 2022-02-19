package com.example.tolan.fragments;

import android.os.Bundle;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FrgWelcome extends Fragment {

    private List list;
    private ImageCarousel carousel;
    static TextToSpeech textToSpeech;
    ClssConvertirTextoAVoz tts;
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
        textToSpeech = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i!= TextToSpeech.ERROR){
                    textToSpeech.setLanguage(Locale.getDefault());
                    textToSpeech.speak(getString(R.string.bienvenida),TextToSpeech.QUEUE_FLUSH,null);
                }
            }
        });
        tts = new ClssConvertirTextoAVoz();
        tts.init(getContext());
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_welcome, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        carousel = view.findViewById(R.id.carousel);
        btnRegistrarse = view.findViewById(R.id.register);
        btnRegistrarse.setOnClickListener(v -> Register());
        btnIniciar = view.findViewById(R.id.login);
        btnIniciar.setOnClickListener(v -> Iniciar());
        list = new ArrayList();
        list.add(new CarouselItem(R.drawable.aprendizaje,getString(R.string.mensaje_bienvenida_1)));
        list.add(new CarouselItem(R.drawable.nino,getString(R.string.mensaje_bienvenida_2)));
        list.add(new CarouselItem(R.drawable.ninos,getString(R.string.mensaje_bienvenida_3)));
        carousel.addData(list);
        return view;
    }

    private void Register() {
        fragment = new FrgRegisterUser();
        getFragmentManager().beginTransaction().replace(R.id.content, fragment).addToBackStack(null).commit();
    }

    private void Iniciar() {
        fragment = new FrgLogin();
        getFragmentManager().beginTransaction().replace(R.id.content, fragment).addToBackStack(null).commit();
    }
}