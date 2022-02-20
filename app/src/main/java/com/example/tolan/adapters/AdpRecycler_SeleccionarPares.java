package com.example.tolan.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.bumptech.glide.Glide;
import com.example.tolan.R;
import com.example.tolan.clases.ClssVolleySingleton;
import com.example.tolan.dialogs.Diag_Frg_OpcionIncorrecta;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class AdpRecycler_SeleccionarPares extends RecyclerView.Adapter<txtHolder> {

    private final Context mContext;
    private final ArrayList<String> listElements, listRutas;
    Map<String, String> map_DatosEmparejados;
    Fragment fragment;

    public AdpRecycler_SeleccionarPares(Context mContext, ArrayList<String> listElements, ArrayList<String> listRutas, Map<String, String> map_DatosEmparejados, Fragment fragment) {
        this.mContext = mContext;
        this.listElements = listElements;
        this.listRutas = listRutas;
        this.map_DatosEmparejados = map_DatosEmparejados;
        this.fragment = fragment;
    }


    @NonNull
    @Override
    public txtHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_text_seleccionar_pares_imagen_texto, parent, false);
        return new txtHolder(view);
    }


    private static int nElementsSelectedRight = 0;
    private static CardView cardview_selectedRight = null;
    private static RelativeLayout ryl_fondo_bordes_texto_selectedRight = null;
    private static TextView txt_selected_Right;


    private static int nElementsSelectedLeft = 0;
    private static CardView cardview_selectedLeft = null;
    private static RelativeLayout ryl_fondo_bordes_imagen_selectedLeft = null;
    private static ImageView img_selectedLeft;

    private static int nOptionsSelected = 0;


    @Override
    public void onBindViewHolder(@NonNull txtHolder holder, @SuppressLint("RecyclerView") int position) {

        cargarImagenWebService(listRutas.get(position), holder, position);
        //Glide.with(mContext)
        //      .load(listRutas.get(position))
        //    .into(holder.imagen);

        holder.texto.setText(listElements.get(position));
        holder.texto.setId(position);


        holder.cardview_texto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //No ha seleccionado nada aun
                if (nElementsSelectedRight == 0) {


                    //Pintamos su borde respectivo
                    pintarBorde(holder.ryl_fondo_bordes_texto, "seleccionado");
                    //holder.ryl_fondo_bordes_texto.setBackgroundResource(R.drawable.fondo_bordes_imagen_complete);
                    nElementsSelectedRight = 1;

                    //Almancenamos en pila el elemento seleccionado por si cambiamos de opcion
                    cardview_selectedRight = holder.cardview_texto;
                    txt_selected_Right = holder.texto;
                    ryl_fondo_bordes_texto_selectedRight = holder.ryl_fondo_bordes_texto;

                    //Incrementamos en 1 debido a que hay un item seleccioando
                    nOptionsSelected++;


                } else {
                    //Ya deberia estar en pila un elemento de la derecha seleccionado

                    if (cardview_selectedRight != null) {
                        //Es verdad ya hay un elemento en pila seleccionado, entonces procedemos a deseleccionarlo
                        if (cardview_selectedRight.equals(holder.cardview_texto)) {
                            //Antes de deseleccionarlo verificamos si selecciono el mismo elemento
                            Toast.makeText(mContext, "Haz seleccionado el mismo elemento varias veces", Toast.LENGTH_LONG).show();
                            //Como se cumple procedemos a deseleccionarlo y vaciar la pila de valores
                            pintarBorde(holder.ryl_fondo_bordes_texto, "deseleccionado");
                            //holder.ryl_fondo_bordes_texto.setBackgroundResource(R.drawable.fondo_bordes_imagen);
                            nElementsSelectedRight = 0;
                            cardview_selectedRight = null;
                            txt_selected_Right = null;
                            ryl_fondo_bordes_texto_selectedRight = null;

                            //Decrementamos en 1 debido a que hay un item deseleccioando
                            nOptionsSelected--;

                        } else {

                            pintarBorde(ryl_fondo_bordes_texto_selectedRight, "deseleccionado");
                            //ryl_fondo_bordes_texto_selectedRight.setBackgroundResource(R.drawable.fondo_bordes_imagen);
                            //Y seleccionamos el nuevo elemento

                            pintarBorde(holder.ryl_fondo_bordes_texto, "seleccionado");
                            //holder.ryl_fondo_bordes_texto.setBackgroundResource(R.drawable.fondo_bordes_imagen_complete);
                            nElementsSelectedRight = 1;
                            cardview_selectedRight = holder.cardview_texto;
                            txt_selected_Right = holder.texto;
                            ryl_fondo_bordes_texto_selectedRight = holder.ryl_fondo_bordes_texto;

                            //Seteamos en 1 debido a que hay un nuevo item seleccioando
                            nOptionsSelected = 1;
                        }
                    }
                }

                if (nOptionsSelected == 2) {


                    comprobarSimilitud(listRutas.get(img_selectedLeft.getId()), listElements.get(txt_selected_Right.getId()));

                }
            }
        });

        holder.cardview_imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //No ha seleccionado nada aun
                if (nElementsSelectedLeft == 0) {


                    //Pintamos su borde respectivo
                    pintarBorde(holder.ryl_fondo_bordes_imagen, "seleccionado");

                    nElementsSelectedLeft = 1;

                    //Almancenamos en pila el elemento seleccionado por si cambiamos de opcion
                    cardview_selectedLeft = holder.cardview_imagen;
                    img_selectedLeft = holder.imagen;
                    ryl_fondo_bordes_imagen_selectedLeft = holder.ryl_fondo_bordes_imagen;

                    //Incrementamos en 1 debido a que hay un item seleccioando
                    nOptionsSelected++;


                } else {
                    //Ya deberia estar en pila un elemento de la derecha seleccionado

                    if (cardview_selectedLeft != null) {
                        //Es verdad ya hay un elemento en pila seleccionado, entonces procedemos a deseleccionarlo
                        if (cardview_selectedLeft.equals(holder.cardview_imagen)) {
                            //Antes de deseleccionarlo verificamos si selecciono el mismo elemento
                            Toast.makeText(mContext, "Haz seleccionado el mismo elemento varias veces", Toast.LENGTH_LONG).show();
                            //Como se cumple procedemos a deseleccionarlo y vaciar la pila de valores
                            pintarBorde(holder.ryl_fondo_bordes_imagen, "deseleccionado");

                            nElementsSelectedLeft = 0;
                            cardview_selectedLeft = null;
                            img_selectedLeft = null;
                            ryl_fondo_bordes_imagen_selectedLeft = null;

                            //Decrementamos en 1 debido a que hay un item deseleccioando
                            nOptionsSelected--;

                        } else {

                            pintarBorde(ryl_fondo_bordes_imagen_selectedLeft, "deseleccionado");

                            //Y seleccionamos el nuevo elemento

                            pintarBorde(holder.ryl_fondo_bordes_imagen, "seleccionado");

                            nElementsSelectedLeft = 1;
                            cardview_selectedLeft = holder.cardview_imagen;
                            img_selectedLeft = holder.imagen;
                            ryl_fondo_bordes_imagen_selectedLeft = holder.ryl_fondo_bordes_imagen;

                            //Seteamos en 1 debido a que hay un nuevo item seleccioando
                            nOptionsSelected = 1;
                        }
                    }
                }

                if (nOptionsSelected == 2) {
                    //Toast.makeText(mContext, "Haz seleccionado opciones de la derecha e izquierda respectivamente",
                    //      Toast.LENGTH_LONG).show();
                    //comprobarSimilitud(listRutas.get(position), listElements.get(position));
                    comprobarSimilitud(listRutas.get(img_selectedLeft.getId()), listElements.get(txt_selected_Right.getId()));

                }
            }
        });


    }

    public void pintarBorde(RelativeLayout ryl, String estado) {
        switch (estado) {
            case "seleccionado":
                ryl.setBackgroundResource(R.drawable.fondo_bordes_imagen_complete);
                break;
            case "deseleccionado":
                ryl.setBackgroundResource(R.drawable.fondo_bordes_imagen);
                break;
            case "error":
                ryl.setBackgroundResource(R.drawable.fondo_bordes_imagen_error);
                break;
        }
    }

    public void disabled_Opciones(boolean estado) {
        if (estado) {
            nOptionsSelected = 0;
            cardview_selectedRight.setEnabled(false);
            ryl_fondo_bordes_texto_selectedRight.setBackgroundResource(R.color.plomo);

            cardview_selectedLeft.setEnabled(false);
            ryl_fondo_bordes_imagen_selectedLeft.setBackgroundResource(R.color.plomo);


            //Quitar ambos elemento seteados de la pila
            nElementsSelectedRight = 0;
            cardview_selectedRight = null;
            ryl_fondo_bordes_texto_selectedRight = null;
            txt_selected_Right = null;

            nElementsSelectedLeft = 0;
            cardview_selectedLeft = null;
            ryl_fondo_bordes_imagen_selectedLeft = null;
            img_selectedLeft = null;
        } else {
            pintarBorde(ryl_fondo_bordes_texto_selectedRight, "error");
            pintarBorde(ryl_fondo_bordes_imagen_selectedLeft, "error");


            //pintarBorde(ryl_fondo_bordes_texto_selectedRight, "deseleccionado");
            //pintarBorde(ryl_fondo_bordes_imagen_selectedLeft, "deseleccionado");

            nOptionsSelected = 0;
            //cardview_selectedRight.setEnabled(false);


            //ryl_fondo_bordes_texto_selectedRight.setBackgroundResource(R.color.plomo);

            //cardview_selectedLeft.setEnabled(false);
            //ryl_fondo_bordes_imagen_selectedLeft.setBackgroundResource(R.color.plomo);


            //Quitar ambos elemento seteados de la pila
            nElementsSelectedRight = 0;
            cardview_selectedRight = null;
            ryl_fondo_bordes_texto_selectedRight = null;
            txt_selected_Right = null;

            nElementsSelectedLeft = 0;
            cardview_selectedLeft = null;
            ryl_fondo_bordes_imagen_selectedLeft = null;
            img_selectedLeft = null;

        }

    }

    Diag_Frg_OpcionIncorrecta diag_frg_opcionIncorrecta = null;

    public void comprobarSimilitud(String id, String elemento) {
        if (elemento.equals(map_DatosEmparejados.get(id))) {
            //Toast.makeText(mContext, "Imagen No. : " + id + " Elemento :" + elemento,
            //      Toast.LENGTH_LONG).show();
            disabled_Opciones(true);
        } else {
            Toast.makeText(mContext, "Elementos incorrectos",
                    Toast.LENGTH_LONG).show();


            diag_frg_opcionIncorrecta = new Diag_Frg_OpcionIncorrecta(map_DatosEmparejados.get(id), id);
            diag_frg_opcionIncorrecta.show(fragment.getParentFragmentManager(), "kol");


            disabled_Opciones(false);
        }


    }

    @Override
    public int getItemCount() {
        return listElements.size();
    }

    private void cargarImagenWebService(String rutaImagen, final txtHolder holder, int pos) {
        ImageRequest imageRequest = new ImageRequest(rutaImagen, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                holder.imagen.setImageBitmap(response);
                holder.imagen.setId(pos);
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

                    Toast.makeText(mContext, "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
                }
                //
            }
        });
        //request.add(imageRequest);
        ClssVolleySingleton.getIntanciaVolley(mContext).addToRequestQueue(imageRequest);
    }


}

class txtHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    TextView texto;
    CardView cardview_texto;

    ImageView imagen;
    CardView cardview_imagen;
    RelativeLayout ryl_fondo_bordes_imagen, ryl_fondo_bordes_texto;


    public txtHolder(@NonNull View itemView) {
        super(itemView);

        //itemTextView = itemView.findViewById(R.id.itemTextView);
        texto = (TextView) itemView.findViewById(R.id.texto);
        cardview_texto = (CardView) itemView.findViewById(R.id.cardview_texto);

        imagen = (ImageView) itemView.findViewById(R.id.imagen);
        cardview_imagen = (CardView) itemView.findViewById(R.id.cardview_imagen);
        //cardview_imagen.setOnClickListener(this);
        ryl_fondo_bordes_imagen = (RelativeLayout) itemView.findViewById(R.id.ryl_fondo_bordes_imagen);
        ryl_fondo_bordes_texto = (RelativeLayout) itemView.findViewById(R.id.ryl_fondo_bordes_texto);
    }


    private long mLastClickTime = 0;
    private static boolean isSelectedOption = false;

    @Override
    public void onClick(View v) {

        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        if (!isSelectedOption) {
            ryl_fondo_bordes_imagen.setBackgroundResource(R.drawable.fondo_bordes_imagen_complete);
            isSelectedOption = true;
        } else {
            ryl_fondo_bordes_imagen.setBackgroundResource(R.drawable.fondo_bordes_imagen);
            isSelectedOption = false;
        }


    }
}
