package com.example.tolan.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
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
import com.example.tolan.R;
import com.example.tolan.clases.ClssVolleySingleton;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Random;

public class AdpRecycler_SeleccionarParesImagenImagen extends RecyclerView.Adapter<imgHolder> {


    private final Context mContext;
    private final ArrayList<String> listElements;
    int[] numerosAleatorios;
    LinearLayout ry_state;
    ScrollView mScrollView;


    public AdpRecycler_SeleccionarParesImagenImagen(Context mContext, ArrayList<String> listElements, int[] numerosAleatorios, LinearLayout ry_state, ScrollView mScrollView) {
        this.mContext = mContext;
        this.listElements = listElements;
        this.numerosAleatorios = numerosAleatorios;
        this.ry_state = ry_state;
        this.mScrollView = mScrollView;

    }


    @NonNull
    @Override
    public imgHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_image_seleccionar_pares_imagen_imagen, parent, false);
        return new imgHolder(view);
    }


    private static int idLeft = -1, idRight = -1;

    private boolean stateSelectedRight = false, stateSelectedLeft = false;

    private static RelativeLayout static_ryl_bordes_right = null, static_ryl_bordes_Left = null;
    private static CardView cardview_selectedLeft = null;
    private static CardView cardview_selectedRight = null;

    private static int nOptionsSelected = 0;


    private static boolean estadoAplicacion = false;

    @Override
    public void onBindViewHolder(@NonNull imgHolder holder, int position) {
        cargarImagenWebService(listElements.get(position), holder.img_Left, position);
        cargarImagenWebService(listElements.get(numerosAleatorios[position]), holder.img_Right, numerosAleatorios[position]);

        holder.cardview_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!estadoAplicacion) {
                    comprobarOpcionesLeft(holder);
                } else {
                    Toast.makeText(mContext, "Reproducir audio", Toast.LENGTH_LONG).show();
                }
            }
        });


        holder.cardview_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!estadoAplicacion) {
                    comprobarOpcionesRight(holder);
                } else {
                    Toast.makeText(mContext, "Reproducir audio", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void comprobarOpcionesLeft(imgHolder holder) {
        //Evaluar si es el primer clic o si hizo clic sobre el mismo elemento 2 veces
        if (idLeft != -1) {
            //Si se cumple quiere decir que dio clic sobre el mismo elemento
            if (stateSelectedLeft && idLeft == holder.img_Left.getId()) {
                //Reseteamos la pila
                idLeft = -1;
                //quitamos foco del elemento
                pintarBorde(holder.ryl_bordes_left, "deseleccionado");
                //seteamos en false ya que no hay nada seleccionado
                stateSelectedLeft = false;
                //Eliminamos lo que haya en la pila
                static_ryl_bordes_Left = null;
                cardview_selectedLeft = null;
                nOptionsSelected = 0;
            }
            //Entonces dio clic sobre otro elemento de la misma lista
            else {
                //Toast.makeText(mContext, "Cambiando posicion", Toast.LENGTH_SHORT).show();
                // //Quitamos el borde del viejo elemento
                pintarBorde(static_ryl_bordes_Left, "deseleccionado");
                //Lo eliminamos de la pila setenadole el nuevo valor
                static_ryl_bordes_Left = holder.ryl_bordes_left;
                cardview_selectedLeft = holder.cardview_left;
                //Asignamos el valor del nuevo elemento a la pila
                idLeft = holder.img_Left.getId();
                //Pintamos el borde del nuevo elemento
                pintarBorde(holder.ryl_bordes_left, "seleccionado");
                //seteamos en true ya que seleccionamos un elemento
                stateSelectedLeft = true;
                nOptionsSelected = 1;
            }

        }
        //Dio clic sobre otro elemento de la misma lista
        else {
            //Asignamos el valor del elemento que ha dado clic a la pila
            idLeft = holder.img_Left.getId();
            static_ryl_bordes_Left = holder.ryl_bordes_left;
            cardview_selectedLeft = holder.cardview_left;
            //Procedemos a marcar el foco de dicho elemento
            pintarBorde(holder.ryl_bordes_left, "seleccionado");
            //seteamos en true ya que seleccionamos un elemento
            stateSelectedLeft = true;
            nOptionsSelected++;
        }

        //Toast.makeText(mContext, "[ " + nOptionsSelected + " ]", Toast.LENGTH_SHORT).show();
        //Ya ha seleccionado elementos de izquierda y derecha respectivamente
        //Toast.makeText(mContext, " Haz seleccionado 2 elementos", Toast.LENGTH_SHORT).show();
        comprobarSimilitud();

    }

    public void comprobarOpcionesRight(imgHolder holder) {
        //Evaluar si es el primer clic o si hizo clic sobre el mismo elemento 2 veces
        if (idRight != -1) {
            //Si se cumple quiere decir que dio clic sobre el mismo elemento
            if (stateSelectedRight && idRight == holder.img_Right.getId()) {
                //Reseteamos la pila
                idRight = -1;
                //quitamos foco del elemento
                pintarBorde(holder.ryl_bordes_right, "deseleccionado");
                //seteamos en false ya que no hay nada seleccionado
                stateSelectedRight = false;
                //Eliminamos lo que haya en la pila
                static_ryl_bordes_right = null;
                cardview_selectedRight = null;
                nOptionsSelected = 0;
            }
            //Entonces dio clic sobre otro elemento de la misma lista
            else {
                //Toast.makeText(mContext, "Cambiando posicion", Toast.LENGTH_SHORT).show();
                // //Quitamos el borde del viejo elemento
                pintarBorde(static_ryl_bordes_right, "deseleccionado");
                //Lo eliminamos de la pila setenadole el nuevo valor
                static_ryl_bordes_right = holder.ryl_bordes_right;
                cardview_selectedRight = holder.cardview_right;
                //Asignamos el valor del nuevo elemento a la pila
                idRight = holder.img_Right.getId();
                //Pintamos el borde del nuevo elemento
                pintarBorde(holder.ryl_bordes_right, "seleccionado");
                //seteamos en true ya que seleccionamos un elemento
                stateSelectedRight = true;
                nOptionsSelected = 1;
            }

        }
        //Dio clic sobre otro elemento de la misma lista
        else {
            //Asignamos el valor del elemento que ha dado clic a la pila
            idRight = holder.img_Right.getId();
            static_ryl_bordes_right = holder.ryl_bordes_right;
            cardview_selectedRight = holder.cardview_right;
            //Procedemos a marcar el foco de dicho elemento
            pintarBorde(holder.ryl_bordes_right, "seleccionado");
            //seteamos en true ya que seleccionamos un elemento
            stateSelectedRight = true;
            nOptionsSelected++;
        }

        //Toast.makeText(mContext, "[ " + nOptionsSelected + " ]", Toast.LENGTH_SHORT).show();
        //Ya ha seleccionado elementos de izquierda y derecha respectivamente
        // Toast.makeText(mContext, " Haz seleccionado 2 elementos", Toast.LENGTH_SHORT).show();
        comprobarSimilitud();

    }


    @Override
    public int getItemCount() {
        return listElements.size();
    }

    private static int elementosCorrectos;

    public void comprobarSimilitud() {
        if (nOptionsSelected == 2) {
            if (idLeft == idRight) {

                resetAll("finalizado");
                elementosCorrectos++;
                //Quiere decir que no hay mas pares por registrar
                if (elementosCorrectos == listElements.size()) {
                    Toast.makeText(mContext, "No hay mas parejas", Toast.LENGTH_LONG).show();
                    elementosCorrectos = 0;

                    mScrollView.post(new Runnable() {
                        public void run() {
                            mScrollView.scrollTo(0, mScrollView.getBottom());
                        }
                    });

                    //Inicamos la animación de los componentes
                    animar(true);
                    //Seteamos el background verde
                    ry_state.setBackgroundColor(Color.parseColor("#AAFAB1"));
                    //Seteamos el texto de continuar y lo mostramos
                    TextView txt = (TextView) ry_state.getChildAt(0);
                    txt.setText("¡Excelente!");
                    txt.setTextColor(Color.parseColor("#048710"));

                    ry_state.getChildAt(2).setVisibility(View.GONE);
                    ry_state.getChildAt(3).setVisibility(View.VISIBLE);
                    //Ubicamos el layout visible
                    ry_state.setVisibility(View.VISIBLE);
                }
            } else {
                //Toast.makeText(mContext, "No son similares", Toast.LENGTH_LONG).show();
                resetAll("error");
                animar(true);


                mScrollView.post(new Runnable() {
                    public void run() {
                        mScrollView.scrollTo(0, mScrollView.getBottom());
                    }
                });
                //Seteamos el texto de error y lo mostramos
                TextView txt = (TextView) ry_state.getChildAt(0);
                txt.setText("¡Oppps! No son pares");
                txt.setTextColor(Color.parseColor("#C70039"));
                txt.setVisibility(View.VISIBLE);
                //Seteamos el backgroun rojo
                ry_state.setBackgroundColor(Color.parseColor("#F7B9B9"));
                //Ocultamos el boton comprobar
                ry_state.getChildAt(3).setVisibility(View.GONE);
                //Seteamos evento click a boton OK
                ry_state.getChildAt(2).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(mContext, "Haz presionado ok", Toast.LENGTH_LONG).show();
                        estadoAplicacion = false;
                        animar(false);

                        mScrollView.post(new Runnable() {
                            public void run() {
                                mScrollView.scrollTo(0, mScrollView.getTop());
                            }
                        });
                        ry_state.setVisibility(View.GONE);
                    }
                });
                estadoAplicacion = true;
                //Ubicamos el layout visible
                ry_state.setVisibility(View.VISIBLE);
            }
        }
    }

    private void animar(boolean mostrar) {
        AnimationSet set = new AnimationSet(true);
        Animation animation = null;
        if (mostrar) {

            animation = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 1.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f);
        } else {
            animation = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 1.0f);
        }
        //duración en milisegundos
        animation.setDuration(500);
        set.addAnimation(animation);
        LayoutAnimationController controller = new LayoutAnimationController(set, 0.25f);

        ry_state.setLayoutAnimation(controller);
        ry_state.startAnimation(animation);
    }


    public void resetAll(String pintar) {
        pintarBorde(static_ryl_bordes_Left, pintar);
        pintarBorde(static_ryl_bordes_right, pintar);
        nOptionsSelected = 0;
        idLeft = idRight = -1;
        static_ryl_bordes_Left = static_ryl_bordes_right = null;
        stateSelectedLeft = stateSelectedRight = false;
        if (pintar == "finalizado") {
            cardview_selectedLeft.setEnabled(false);
            cardview_selectedRight.setEnabled(false);
        }
        cardview_selectedLeft = cardview_selectedRight = null;


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
            case "finalizado":
                ryl.setBackgroundResource(R.color.plomo);
                break;
        }
    }

    private void cargarImagenWebService(String rutaImagen, ImageView img, int pos) {
        ImageRequest imageRequest = new ImageRequest(rutaImagen, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                img.setImageBitmap(response);
                img.setId(pos);
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


class imgHolder extends RecyclerView.ViewHolder {

    CardView cardview_left, cardview_right;

    ImageView img_Left, img_Right;
    RelativeLayout ryl_bordes_left, ryl_bordes_right;


    public imgHolder(@NonNull View itemView) {
        super(itemView);

        cardview_left = (CardView) itemView.findViewById(R.id.cardview_left);
        cardview_right = (CardView) itemView.findViewById(R.id.cardview_right);

        img_Left = (ImageView) itemView.findViewById(R.id.img_Left);
        img_Right = (ImageView) itemView.findViewById(R.id.img_Right);

        ryl_bordes_left = (RelativeLayout) itemView.findViewById(R.id.ryl_bordes_left);
        ryl_bordes_right = (RelativeLayout) itemView.findViewById(R.id.ryl_bordes_right);

    }
}

