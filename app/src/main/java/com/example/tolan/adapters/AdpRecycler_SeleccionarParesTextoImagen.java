package com.example.tolan.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.tolan.R;
import com.example.tolan.clases.ClssAnimation;
import com.example.tolan.clases.ClssConvertTextToSpeech;
import com.example.tolan.clases.ClssStaticGroup;
import com.example.tolan.clases.ClssVolleySingleton;
import com.example.tolan.dialogs.Diag_Frg_AyudaEspecial;
import com.example.tolan.models.ModelUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class AdpRecycler_SeleccionarParesTextoImagen extends RecyclerView.Adapter<txtHolder> {

    private final Context mContext;
    private final ArrayList<String> listElements, listRutas;
    Map<String, String> map_DatosEmparejados;
    Fragment fragment;
    int idActividad;

    Map<String, List<String>> map_MultimediaExtra;

    //private static ClssConvertTextToSpeech clssConvertirTextoAVoz;

    public AdpRecycler_SeleccionarParesTextoImagen(Context mContext, ArrayList<String> listElements, ArrayList<String> listRutas, Map<String, String> map_DatosEmparejados, Fragment fragment, int idActividad, Map<String, List<String>> map_MultimediaExtra) {
        this.mContext = mContext;
        this.listElements = listElements;
        this.listRutas = listRutas;
        this.map_DatosEmparejados = map_DatosEmparejados;
        this.fragment = fragment;
        this.idActividad = idActividad;
        this.map_MultimediaExtra = map_MultimediaExtra;

        //clssConvertirTextoAVoz = new ClssConvertTextToSpeech();
        //clssConvertirTextoAVoz.init(mContext);
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

    private static boolean estadoAplicacion = false;

    @Override
    public void onBindViewHolder(@NonNull txtHolder holder, @SuppressLint("RecyclerView") int position) {

        cargarImagenWebService(listRutas.get(position), holder, position);

        holder.imagen_ayuda_especial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(mContext, " Son :" + map_MultimediaExtra.get(rutaImagen), Toast.LENGTH_SHORT).show();
                //Toast.makeText(mContext, "Ayuda especial", Toast.LENGTH_LONG).show();
                String img = listRutas.get(position);
                Diag_Frg_AyudaEspecial diag_frg_ayudaEspecial = new Diag_Frg_AyudaEspecial(img, map_DatosEmparejados.get(img), map_MultimediaExtra.get(img), "Seleccionar pares imagen con texto");
                diag_frg_ayudaEspecial.show(fragment.getParentFragmentManager(), "Infromación de Ayuda Especial");
            }
        });

        //Glide.with(mContext)
        //      .load(listRutas.get(position))
        //    .into(holder.imagen);

        holder.texto.setText(listElements.get(position));
        holder.texto.setId(position);


        holder.cardview_texto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!estadoAplicacion) {
                    comprobar_cardview_texto(holder);
                } else {

                    //Toast.makeText(mContext, "Ok para continuar...", Toast.LENGTH_LONG).show();
                }
                ClssConvertTextToSpeech.getIntancia(mContext).reproduce(listElements.get(position));
                //ClssVolleySingleton.getIntanciaVolley(mContext).addToRequestQueue(request_json);

            }
        });

        holder.cardview_imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!estadoAplicacion) {
                    comprobar_cardview_imagen(holder);
                } else {
                    //Toast.makeText(mContext, "Ok para continuar...", Toast.LENGTH_LONG).show();
                }
                //clssConvertirTextoAVoz.reproduce("Imagen de " + listElements.get(position));
            }
        });


    }

    public void comprobar_cardview_texto(txtHolder holder) {
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

                    //Toast.makeText(mContext, "Haz seleccionado el mismo elemento varias veces", Toast.LENGTH_LONG).show();

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


    public void comprobar_cardview_imagen(txtHolder holder) {
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

                    //Toast.makeText(mContext, "Haz seleccionado el mismo elemento varias veces", Toast.LENGTH_LONG).show();

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

    //Cuadro de dialogo
    //Diag_Frg_OpcionIncorrecta diag_frg_opcionIncorrecta = null;

    private static int elementosCorrectos;


    String[] msg_true;

    private String generarAleatorio() {
        Random random = new Random();
        String r = msg_true[random.nextInt(msg_true.length)];
        return r;
    }

    public void comprobarSimilitud(String id, String elemento) {

        //Obtengo mi scrollview
        final ScrollView mScrollView = fragment.getView().findViewById(R.id.mScrollView);

        //Obtengo el layout de mi Fragment principal
        LinearLayout ry_state = fragment.getView().findViewById(R.id.ry_state);

        if (elemento.equals(map_DatosEmparejados.get(id))) {
            //Toast.makeText(mContext, "Imagen No. : " + id + " Elemento :" + elemento,
            //      Toast.LENGTH_LONG).show();
            elementosCorrectos++;
            if (elementosCorrectos == listElements.size()) {
                //Toast.makeText(mContext, "No hay mas parejas", Toast.LENGTH_LONG).show();

                msg_true = mContext.getResources().getStringArray(R.array.msg_true);
                String mensaje = generarAleatorio();
                //ClssConvertTextToSpeech.getIntancia(mContext).reproduce(mensaje);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //ClssConvertTextToSpeech.getIntancia(getContext()).reproduce("La actividad no tiene contenido");
                        //tts.reproduce("La actividad no tiene contenido");
                        ClssConvertTextToSpeech.getIntancia(mContext).reproduce(mensaje);
                    }
                }, 1000);


                elementosCorrectos = 0;
                mScrollView.post(new Runnable() {
                    public void run() {
                        mScrollView.scrollTo(0, mScrollView.getBottom());
                    }
                });

                //Inicamos la animación de los componentes
                animar(true, ry_state);

                //Seteamos el background verde
                ry_state.setBackgroundColor(Color.parseColor("#AAFAB1"));
                //Seteamos el texto de continuar y lo mostramos
                TextView txt = (TextView) ry_state.getChildAt(0);
                txt.setText(mensaje);
                txt.setTextColor(Color.parseColor("#048710"));

                ry_state.getChildAt(2).setVisibility(View.GONE);
                ry_state.getChildAt(3).setVisibility(View.VISIBLE);

                //Ubicamos el layout visible
                ry_state.setVisibility(View.VISIBLE);
                CompleteActivity();

            }

            disabled_Opciones(true);
        } else {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //ClssConvertTextToSpeech.getIntancia(getContext()).reproduce("La actividad no tiene contenido");
                    //tts.reproduce("La actividad no tiene contenido");
                    ClssConvertTextToSpeech.getIntancia(mContext).reproduce("¡Ups! Vuelve a intentarlo");
                }
            }, 1000);

            //Toast.makeText(mContext, "Elementos incorrectos",
            //        Toast.LENGTH_LONG).show();


            //Ehecutar animacion de despliegue arriba/abajo
            animar(true, ry_state);

            //Setear el scrollView abajo
            mScrollView.post(new Runnable() {
                public void run() {
                    mScrollView.scrollTo(0, mScrollView.getBottom());
                }
            });


            //Seteo el texto de error y el color de letra en rojo respectivo
            TextView txt = (TextView) ry_state.getChildAt(0);
            txt.setText("¡Ups! Vuelve a intentarlo");

            txt.setTextColor(Color.parseColor("#C70039"));
            //Setear color rojo de background
            ry_state.setBackgroundColor(Color.parseColor("#F7B9B9"));
            //El usuario se acaba de equivocar
            //Ocultar boton comprobar
            ry_state.getChildAt(3).setVisibility(View.GONE);
            //Evento click a boton OK
            ry_state.getChildAt(2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    estadoAplicacion = false;
                    //animar(false);
                    //mScrollView = fragment.getView().findViewById(R.id.mScrollView);
                    mScrollView.post(new Runnable() {
                        public void run() {
                            mScrollView.scrollTo(0, mScrollView.getTop());
                        }
                    });
                    ry_state.setVisibility(View.GONE);
                }
            });


            //Cuadro de dialogo
            //diag_frg_opcionIncorrecta = new Diag_Frg_OpcionIncorrecta(map_DatosEmparejados.get(id), id);
            //diag_frg_opcionIncorrecta.show(fragment.getParentFragmentManager(), "kol");

            estadoAplicacion = true;
            //Ubicamos el layout visible
            ry_state.setVisibility(View.VISIBLE);

            disabled_Opciones(false);
        }


    }

    private void CompleteActivity() {
        try {
            // Crear nueva cola de peticiones
            //RequestQueue requestQueue = Volley.newRequestQueue(mContext);
            //Parámetros a enviar a la API
            JSONObject param = new JSONObject();
            param.put("idEstudiante", ClssStaticGroup.idestudiante);
            param.put("idActividad", idActividad);
            param.put("statusRespuesta", true);
            param.put("idsContenido", new JSONObject());
            JsonObjectRequest request_json = new JsonObjectRequest(mContext.getString(R.string.urlBase) + "historial/completeActividad", param,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response.length() > 1) {
                                    ModelUser.stockcaritas += response.getInt("recompensaganada");
                                    //Toast.makeText(getContext(), "Actividad exitosa", Toast.LENGTH_LONG).show();
                                    //Navegacion(v);
                                } //else
                                //Toast.makeText(mContext, response.get("message").toString(), Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Toast.makeText(mContext, "Error de conexión", Toast.LENGTH_LONG).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.e("Error: ", error.getMessage());
                    Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
            // Añadir petición a la cola
            ClssVolleySingleton.getIntanciaVolley(mContext).addToRequestQueue(request_json);
        } catch (JSONException e) {
            e.printStackTrace();
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

    private void animar(boolean mostrar, LinearLayout ry_state) {
        //AnimationSet set = new AnimationSet(true);
        //Animation animation = null;
        if (mostrar) {
            ry_state.setLayoutAnimation(ClssAnimation.getInstanciaAnimation().getLayoutAnimationController());
            ry_state.startAnimation(ClssAnimation.getInstanciaAnimation().getAnimationDown());
            /*
            animation = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 1.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f);

            */
        } else {
            ry_state.setLayoutAnimation(ClssAnimation.getInstanciaAnimation().getLayoutAnimationController());
            ry_state.startAnimation(ClssAnimation.getInstanciaAnimation().getAnimationUp());
            /*
            animation = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 1.0f);

             */
        }
        //duración en milisegundos
        //animation.setDuration(500);
        //set.addAnimation(animation);
        //LayoutAnimationController controller = new LayoutAnimationController(set, 0.25f);

        //ry_state.setLayoutAnimation(controller);
        //ry_state.startAnimation(animation);
    }


}

class txtHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    TextView texto;
    CardView cardview_texto;

    ImageView imagen, imagen_ayuda_especial;
    CardView cardview_imagen;
    RelativeLayout ryl_fondo_bordes_imagen, ryl_fondo_bordes_texto;


    public txtHolder(@NonNull View itemView) {
        super(itemView);

        //itemTextView = itemView.findViewById(R.id.itemTextView);
        texto = (TextView) itemView.findViewById(R.id.texto);
        cardview_texto = (CardView) itemView.findViewById(R.id.cardview_texto);

        imagen = (ImageView) itemView.findViewById(R.id.imagen);
        imagen_ayuda_especial = (ImageView) itemView.findViewById(R.id.imagen_ayuda_especial);
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
