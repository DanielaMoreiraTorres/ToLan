package com.example.tolan.clases;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;

public class ClssConvertirTextoAVoz implements TextToSpeech.OnInitListener {


    public static ClssConvertirTextoAVoz clssConvertirTextoAVoz;
    private TextToSpeech tts;
    private static Context contexto;
    public TextToSpeech.OnInitListener onInitListener;


    private ClssConvertirTextoAVoz(Context context) {
        this.contexto = context;
        onInitListener = this;
        tts = getTts();
    }

    public static synchronized ClssConvertirTextoAVoz getIntancia(Context context) {
        if (clssConvertirTextoAVoz == null) {
            clssConvertirTextoAVoz = new ClssConvertirTextoAVoz(context);
        }
        return clssConvertirTextoAVoz;
    }

    public TextToSpeech getTts() {
        if (tts == null) {
            try {
                tts = new TextToSpeech(contexto, onInitListener);
            } catch (Exception e) {
                Log.e("TTS", "Initilization Failed!");
            }
        }
        return tts;
    }


    public void reproduce(String texto) {
        try {
            tts.speak(texto, TextToSpeech.QUEUE_FLUSH, null);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }

    @Override
    public void onInit(int status) {
        Locale español = new Locale("es", "ES");
        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(español);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("error", "Este lenguaje no está permitido");
            }
        } else {
            tts.setLanguage(Locale.getDefault());
            Log.e("error", "Fallo al inicializar");
        }
    }
}