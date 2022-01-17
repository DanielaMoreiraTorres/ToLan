package com.example.tolan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;

import com.example.tolan.clases.ClssConvertirTextoAVoz;

import org.imaginativeworld.whynotimagecarousel.CarouselItem;
import org.imaginativeworld.whynotimagecarousel.ImageCarousel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ActivityWelcome extends AppCompatActivity {
    private List list;
    ImageCarousel carousel;
    static TextToSpeech textToSpeech;
    ClssConvertirTextoAVoz tts;
    Button btnIniciar;
    Button btnRegistrarse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i!= TextToSpeech.ERROR){
                    textToSpeech.setLanguage(Locale.getDefault());
                    textToSpeech.speak(getString(R.string.bienvenida),TextToSpeech.QUEUE_FLUSH,null);
                }
            }
        });
        tts = new ClssConvertirTextoAVoz();
        tts.init(this);
        carousel = findViewById(R.id.carousel);
        list = new ArrayList();
        list.add(new CarouselItem(R.drawable.aprendizaje,getString(R.string.mensaje_bienvenida_1)));
        list.add(new CarouselItem(R.drawable.nino,getString(R.string.mensaje_bienvenida_2)));
        list.add(new CarouselItem(R.drawable.ninos,getString(R.string.mensaje_bienvenida_3)));
        carousel.addData(list);
    }

    public void LoginClick(View v) {
        Intent intent = new Intent(this, ActivityLogin.class);
        startActivity(intent);
    }

    public void RegisterUser(View v) {
        Intent intent = new Intent(this, ActivityRegister.class);
        startActivity(intent);
    }

}