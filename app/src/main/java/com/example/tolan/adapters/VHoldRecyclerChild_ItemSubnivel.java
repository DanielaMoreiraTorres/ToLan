package com.example.tolan.adapters;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;


import com.example.tolan.R;
import com.example.tolan.models.ModelRecyclerItemActividad;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class VHoldRecyclerChild_ItemSubnivel extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView txt_actividad;
    CircleImageView img_actividad;
    ImageView imgv_corona;
    CardView cardView;
    JSONArray lstitem_Activities;

    public VHoldRecyclerChild_ItemSubnivel(@NonNull View itemView) {
        super(itemView);
        txt_actividad = (TextView) itemView.findViewById(R.id.txt_actividad);
        img_actividad = (CircleImageView) itemView.findViewById(R.id.img_actividad);
        imgv_corona = (ImageView) itemView.findViewById(R.id.imgv_corona);
        cardView = (CardView) itemView.findViewById(R.id.cardview_actividad);
        //this.lstitem_Activities=lstitem_Activities;
        cardView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        //Registrar en historial

        //Avanzar hacia la siguiente actividad
        try {
            if (lstitem_Activities != null) {

                JSONObject activity = lstitem_Activities.getJSONObject(0);

                //Tomamos nuestra activiad del objeto
                String actividad = activity.getString("nombre");
                ;
                //Consrutimos un objeto Bundle para pasarle la lista de actividades
                Bundle bundle = new Bundle();
                //Añadmimos al bundle la lista que pasaremos como parametro
                bundle.putString("activities", lstitem_Activities.toString());

                //Quitamos el elemento hacia el que nos vamos a dirigir
                lstitem_Activities.remove(0);


                //Inicializamos la variable de navegación hacia otros formularios
                NavController navController = Navigation.findNavController(v);

                //Construimos un switch para evaluar la X actividad que vaya a ser nuestro primero destino
                switch (actividad) {
                    //El case nos permitira redireccionar hacia el Layout correspondiente para navegar hacia el
                    case "Reconocer figuras":
                        //Toast.makeText(v.getContext(), "La actividad [" +actividad+"] no esta configurada aun", Toast.LENGTH_SHORT).show();
                        //Toast.makeText(v.getContext(), actividad, Toast.LENGTH_SHORT).show();

                        navController.popBackStack(R.id.fragmentReconocerFiguras, true);
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

                    case "Armar rompecabezass":
                        //Toast.makeText(v.getContext(), "La actividad [" +actividad+"] no esta configurada aun", Toast.LENGTH_SHORT).show();
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

                    default :
                        Toast.makeText(v.getContext(),"Revise que la actividad :"+ actividad +" esté configurada por favor", Toast.LENGTH_SHORT).show();
                        break;
                }

            } else {
                Toast.makeText(v.getContext(), "El subnivel no tiene actividades", Toast.LENGTH_SHORT).show();

            }
        } catch (JSONException ex) {
            Toast.makeText(v.getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

}