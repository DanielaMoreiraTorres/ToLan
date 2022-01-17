package com.example.tolan.clases;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;

public class ClssConvertirTextoAVoz {

    private static TextToSpeech tts = null;
    TextToSpeech.OnInitListener onInitListener;

    public void init(Context context){
        try {
            tts = new TextToSpeech(context, onInitListener = new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int i) {
                    if(i != TextToSpeech.ERROR){
                        tts.setLanguage(Locale.getDefault());
                    }
                }
            });
        }catch (Exception e){
            Log.e("TTS", "Initilization Failed!");
        }
    }

    public void reproduce(String texto) {
            tts.speak(texto,TextToSpeech.QUEUE_FLUSH,null);
    }

    public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }
}
