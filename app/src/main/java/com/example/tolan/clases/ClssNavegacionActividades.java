package com.example.tolan.clases;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.example.tolan.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ClssNavegacionActividades {

    NavController navController;
    JSONArray jsonActivities;
    View v;

    public ClssNavegacionActividades(NavController navController, JSONArray jsonActivities, View v) {
        this.navController = navController;
        this.jsonActivities = jsonActivities;
        this.v = v;
    }

    public void navegar() {
        //Inicio Navegación

        Bundle bundle;
        String actividad;


        //Avanzar hacia la siguiente actividad
        try {
            if (jsonActivities.length() > 0) {
                //Pasamos al siguiente fragmento
                JSONObject activity = jsonActivities.getJSONObject(0);
                //Tomamos nuestra activiad del objeto
                actividad = activity.getString("nombre");
                bundle = new Bundle();
                bundle.putString("activities", jsonActivities.toString());
                switch (actividad) {
                    //El case nos permitira redireccionar hacia el Layout correspondiente para navegar hacia el
                    case "Reconocer figuras":
                        //Toast.makeText(v.getContext(), "La actividad [" +actividad+"] no esta configurada aun", Toast.LENGTH_SHORT).show();
                        //Toast.makeText(v.getContext(), actividad, Toast.LENGTH_SHORT).show();
                        navController.navigate(R.id.fragmentReconocerFiguras, bundle);
                        break;
                    case "Ordenar secuencias":
                        //Toast.makeText(v.getContext(), actividad, Toast.LENGTH_SHORT).show();
                        //navController.navigate(R.id.fragmentOrdenarSecuenciasImagenes, bundle);
                        break;
                    case "Identificar entre palabras":
                        //Toast.makeText(v.getContext(), actividad, Toast.LENGTH_SHORT).show();
                        navController.navigate(R.id.fragmentIdentificarRespuestaPalabra, bundle);
                        //Toast.makeText(v.getContext(), "Layout Identificar entre palabras no existe", Toast.LENGTH_SHORT).show();
                        break;
                    case "Identificar entre imágenes":
                        //Toast.makeText(v.getContext(), actividad, Toast.LENGTH_SHORT).show();
                        navController.navigate(R.id.fragmentIdentificarRespuestaImagen, bundle);
                        //Toast.makeText(v.getContext(), "Layout Identificar entre palabras no existe", Toast.LENGTH_SHORT).show();
                        break;
                    case "Armar rompecabezas":
                        //Toast.makeText(v.getContext(), "Layout Armar rompecabezass no existe", Toast.LENGTH_SHORT).show();
                        break;
                    case "Seleccionar pares imagen con texto":
                        //Toast.makeText(v.getContext(), actividad, Toast.LENGTH_SHORT).show();
                        navController.navigate(R.id.fragmentSeleccionarParesImagenTexto, bundle);
                        //Toast.makeText(v.getContext(), "Layout Seleccionar pares. - Imagen-Texto no existe", Toast.LENGTH_SHORT).show();
                        break;
                    case "Seleccionar pares imagen con imagen":
                        //Toast.makeText(v.getContext(), actividad, Toast.LENGTH_SHORT).show();
                        navController.navigate(R.id.fragmentSeleccionarParesImagenImagen, bundle);
                        //Toast.makeText(v.getContext(), "Layout Seleccionar pares. - Imagen-Texto no existe", Toast.LENGTH_SHORT).show();
                        break;
                    case "Grafomotricidad":
                        //Toast.makeText(v.getContext(), actividad, Toast.LENGTH_SHORT).show();
                        //navController.navigate(R.id.fragmentGrafomotricidad, bundle);
                        //Toast.makeText(v.getContext(), "Layout Grafomotricidad no existe", Toast.LENGTH_SHORT).show();
                        break;
                    case "Arrastrar y soltar texto":
                        //Toast.makeText(v.getContext(), actividad, Toast.LENGTH_SHORT).show();
                        navController.navigate(R.id.fragmentArrastrarSoltar, bundle);
                        break;
                    default:
                        //Toast.makeText(v.getContext(), "Revise que la actividad :" + actividad + " esté configurada por favor", Toast.LENGTH_SHORT).show();
                        System.out.println("Revise que la actividad :" + actividad + " esté configurada por favor");
                        break;
                }
            } else {
                ClssConvertirTextoAVoz.getIntancia(v.getContext()).reproduce("Redirigiendo al menú principal");
                Toast.makeText(v.getContext(), "Redirigiendo al menú principal..", Toast.LENGTH_SHORT).show();
                //Volvemos al fragmento principal eliminando los recursos en pila
                navController.navigate(R.id.inicioFragment, null, new NavOptions.Builder()
                        .setPopUpTo(R.id.inicioFragment, true)
                        .build());
            }
        } catch (JSONException ex) {
            //Toast.makeText(v.getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
            System.out.println(ex.getMessage());
        }
        //Fin Navegación
    }
}
