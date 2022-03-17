package com.example.tolan.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.bumptech.glide.Glide;
import com.example.tolan.R;
import com.example.tolan.clases.ClssStaticActivitiesComplete;
import com.example.tolan.clases.ClssStaticGroup;
import com.example.tolan.clases.ClssVolleySingleton;
import com.example.tolan.models.ModelRecyclerItemSubnivel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AdpRecycler_Child extends RecyclerView.Adapter<VHoldRecyclerChild_ItemSubnivel> {

    //List<String> items;
    private final Context mContext;
    private final List<ModelRecyclerItemSubnivel> mData;


    public AdpRecycler_Child(Context mContext, List<ModelRecyclerItemSubnivel> mData) {
        //this.items = items;
        this.mContext = mContext;
        this.mData = mData;
        //this.fragmentItemSubnivel=fragmentItemSubnivel;
    }

    @NonNull
    @Override
    public VHoldRecyclerChild_ItemSubnivel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.fragment_item_subnivel, parent, false);
        return new VHoldRecyclerChild_ItemSubnivel(view);
    }


    @Override
    public void onBindViewHolder(@NonNull VHoldRecyclerChild_ItemSubnivel holder, int position) {

        String url = "https://db-bartolucci.herokuapp.com/actividad/bySubnivelAndDocente?idSubnivel=" + mData.get(position).getId() + "&idDocente=" + String.valueOf(ClssStaticGroup.iddocente);
        cargarWebService(url, holder);

        //Cargamos la imagen a su ImageView
        Glide.with(mContext).load(mData.get(position).getImage()).into(holder.img_actividad);

        //Seteamos su titulo.- Ej. Vocales
        holder.txt_actividad.setText(mData.get(position).getTitulo());

        //Seteamos el id del Subnivel
        holder.txt_actividad.setId(mData.get(position).getId());


        //cargarImagenWebService(mData.get(position).getImage(), holder);


        //holder.itemTextView.setText(items.get(position));

        //
        //


        //forma antigua de pasar las actividades
        //holder.lstitem_Activities = mData.get(position).getRecyclerItemActividades();

       /* Glide.with(mContext)
                .load(mData.get(position).getImage())
                .into(holder.img_actividad);*/


        /*
        holder.cardView.setOnClickListener(v -> {

            //Llamado de las actividades de cada subnivel


            ///tomar el id de la actividad, nombre
            ///subdividir para cada item

            final NavController navController = Navigation.findNavController(holder.itemView);
            navController.navigate(R.id.fragmentPrueba);


            Toast.makeText(mContext, "item :[ " + String.valueOf(position) + " ]", Toast.LENGTH_LONG).show();

            //System.out.println("item :[ " + String.valueOf(holder.imgv_corona.getId()) + " ]");



            /*
            Intent intent = new Intent(mContext, Actividad_ReconocerFiguras.class);

            intent.putExtra("Title", mData.get(position).getTitulo());

            intent.putExtra("Thumbnail", mData.get(position).getImage());
            // start the activity
            try {
                mContext.startActivity(intent);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }


        });
        */

    }

    private void cargarImagenWebService(String rutaImagen, final VHoldRecyclerChild_ItemSubnivel holder) {
        ImageRequest imageRequest = new ImageRequest(rutaImagen, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                holder.img_actividad.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof NetworkError) {
                    Toast.makeText(mContext,
                            "Oops. Network Error! " + error.toString(),
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(mContext,
                            "Oops. Server Error! " + error.toString(),
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(mContext,
                            "Oops. Auth FailureError! " + error.toString(),
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(mContext,
                            "Oops. Parse Error! " + error.toString(),
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(mContext,
                            "Oops. NoConnection Error! " + error.toString(),
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof TimeoutError) {
                    Toast.makeText(mContext,
                            "Oops. Timeout error! " + error.toString(),
                            Toast.LENGTH_LONG).show();
                } else {
                    //Toast.makeText(mContext, "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
                    System.out.println("Error al cargar la imagen");
                }
            }
        });
        //request.add(imageRequest);
        ClssVolleySingleton.getIntanciaVolley(mContext).addToRequestQueue(imageRequest);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    /*
    class ViewHolder extends RecyclerView.ViewHolder {

        TextView txt_actividad;
        CircleImageView img_actividad;
        ImageView imgv_corona;
        CardView cardView;

        //TextView itemTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //itemTextView = itemView.findViewById(R.id.itemTextView);
            txt_actividad = (TextView) itemView.findViewById(R.id.txt_actividad);
            img_actividad = (CircleImageView) itemView.findViewById(R.id.img_actividad);
            imgv_corona = (ImageView) itemView.findViewById(R.id.imgv_corona);
            cardView = (CardView) itemView.findViewById(R.id.cardview_actividad);
        }
    }
    */


    // RequestQueue request;
    JsonArrayRequest jsonArrayRequest;

    private void cargarWebService(String url, VHoldRecyclerChild_ItemSubnivel holder) {
        jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                if (response.length() > 0) {
                    //List<Integer> indices = new ArrayList<>();
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject item_actividad = response.getJSONObject(i);
                            JSONArray historial = item_actividad.getJSONArray("historial");
                            for (int j = 0; j < historial.length(); j++) {
                                JSONObject item_historial = historial.getJSONObject(j);
                                int idEstudiante = item_historial.getInt("idEstudiante");
                                if (ClssStaticGroup.idestudiante == idEstudiante) {
                                    //indices.add(i);
                                    response.remove(i);
                                    i = -1;
                                    break;
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //System.out.println("Filtrado : " + response + " \n");
                    if (response.length() == 0) {
                        holder.ry_fondo_superior_actividad.setBackgroundResource(R.drawable.fondo_superior_completado);
                        //holder.cardView.setEnabled(false);
                    }
                    holder.lstitem_Activities = response;

                } else {

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        ClssVolleySingleton.getIntanciaVolley(mContext).
                addToRequestQueue(jsonArrayRequest);
    }

    public void removeItemsWithHistorial() {

    }
}
