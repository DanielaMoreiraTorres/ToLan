package com.example.tolan.clases;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.ProgressBar;

public class ClssPreferences {

    public static ClssPreferences clssPreferences;
    private Context context;
    private String PREFS_KEY = "mispreferencias";
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;

    public ClssPreferences(Context context) {
        this.context = context;
        settings = context.getSharedPreferences(PREFS_KEY, MODE_PRIVATE);
        editor = settings.edit();
    }

    public static synchronized ClssPreferences getIntancia(Context context) {
        if (clssPreferences == null) {
            clssPreferences = new ClssPreferences(context);
        }
        return clssPreferences;
    }

    public void guardarValor(String keyPref, String valor) {
        editor.putString(keyPref, valor);
        editor.commit();
    }

    public String leerValor(String keyPref) {
        return  settings.getString(keyPref, "");
    }

    public void  resetValor() {
        editor.clear();
        editor.commit();
    }
}
