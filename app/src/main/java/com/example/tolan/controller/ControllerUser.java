package com.example.tolan.controller;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.tolan.ActivityHomeUser;
import com.example.tolan.R;
import com.example.tolan.clases.ClssConvertTextToSpeech;
import com.example.tolan.clases.ClssPreferences;
import com.example.tolan.clases.ClssStaticGroup;
import com.example.tolan.clases.ClssStaticUser;
import com.example.tolan.clases.ClssVolleySingleton;
import com.example.tolan.fragments.FrgMenuAdmin;
import com.example.tolan.fragments.FrgMenuTeacher;
import com.example.tolan.models.ModelUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ControllerUser {

    private Context context;
    private ProgressBar progressBar;
    private Fragment fragment;
    JSONObject grupo = new JSONObject();
    String docente, nombre;
    int idDocente = 0;

    public ControllerUser(Context context, ProgressBar progressBar) {
        this.context = context;
        this.progressBar = progressBar;
    }

    public void getUsuario(String usuario, String clave) {
        try {
            String url = context.getString(R.string.urlBase) + "usuario/login";
            //Parámetros a enviar a la API
            Map<String, String> parameters = new HashMap<>();
            parameters.put("username", usuario);
            parameters.put("password", clave);
            JsonObjectRequest request_json = new JsonObjectRequest(url, new JSONObject(parameters),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response.length() > 1) {
                                    ModelUser user = new ModelUser();
                                    user.setId(response.getInt("id"));
                                    user.setUsuario(response.getString("usuario").trim());
                                    user.setClave(response.getString("clave").trim());
                                    user.setTipousuario(response.getString("tipousuario").trim());
                                    user.setActivo(response.getBoolean("activo"));
                                    if (user.getTipousuario().equals("ES")) {
                                        JSONObject ObjDatos = (JSONObject) response.get("estudiante");
                                        user.setEstudiante(ObjDatos);
                                        //user.setStockcaritas(ObjDatos.getInt("stockcaritas"));
                                        ModelUser.stockcaritas = ObjDatos.getInt("stockcaritas");
                                        user.setGrupo((JSONArray) ObjDatos.get("grupo"));
                                        int idGrupo = (int) user.getGrupo().getJSONObject(0).get("id");
                                        ObtIdDocente(idGrupo, user);
                                    } else if (user.getTipousuario().equals("DC")) {
                                        JSONObject ObjDatos = (JSONObject) response.get("docente");
                                        user.setDocente(ObjDatos);
                                        idDocente = ObjDatos.getInt("id");
                                        docente = ObjDatos.getString("nombres").trim() + " " + ObjDatos.getString("apellidos").trim();
                                        ClssStaticUser.nombres = ObjDatos.getString("nombres").trim();
                                        ClssStaticUser.apellidos = ObjDatos.getString("apellidos").trim();
                                        Iniciar(user);
                                    } else
                                        Iniciar(user);
                                    //tts.reproduce("Inicio exitoso");
                                    ClssPreferences.getIntancia(context).guardarValor("user", usuario);
                                    ClssPreferences.getIntancia(context).guardarValor("password", clave);
                                    ClssConvertTextToSpeech.getIntancia(context).reproduce("Inicio exitoso");
                                    progressBar.setVisibility(View.GONE);
                                } else {
                                    //tts.reproduce(response.get("message").toString());
                                    Toast.makeText(context, response.get("message").toString(), Toast.LENGTH_SHORT).show();
                                    ClssConvertTextToSpeech.getIntancia(context).reproduce(response.get("message").toString());
                                    progressBar.setVisibility(View.GONE);
                                }

                            } catch (Exception e) {
                                //Toast.makeText(getContext(),e.toString(),Toast.LENGTH_LONG).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.e("Error: ", error.getMessage());
                    Toast.makeText(context, "Error de conexión con el servidor\nIntente nuevamente", Toast.LENGTH_SHORT).show();
                    //tts.reproduce("Error de conexión con el servidor. Intente nuevamente");
                    ClssConvertTextToSpeech.getIntancia(context).reproduce("Error de conexión con el servidor. Intente nuevamente");
                    progressBar.setVisibility(View.GONE);
                }
            });
            // Añadir petición a la cola
            //requestQueue.add(request_json);
            ClssVolleySingleton.getIntanciaVolley(context).addToRequestQueue(request_json);
        } catch (Exception e) {}
    }

    private void ObtIdDocente(int idGrupo, ModelUser muser) {
        String urlGrupo = context.getString(R.string.urlBase) + "grupo/" + idGrupo;
        JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.GET, urlGrupo, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //idDocente = response.getInt("iddocente");
                            if (response.length() > 1) {
                                grupo = response;
                                Iniciar(muser);
                            }
                        } catch (Exception e) {
                            //Toast.makeText(getContext(), "El usuario no tiene docente a cargo asignado", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e("Error: ", error.getMessage());
                    }
                }
        );
        // Añadir petición a la cola
        ClssVolleySingleton.getIntanciaVolley(context).addToRequestQueue(request_json);
    }

    private void Iniciar(ModelUser muser) throws JSONException {
        Bundle b = new Bundle();
        setDataUserLogin(muser);
        if (muser.getTipousuario().trim().equals("AD")) {
            fragment = new FrgMenuAdmin();
            Toast.makeText(context, "Bienvenido Admin", Toast.LENGTH_SHORT).show();
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //tts.reproduce("Bienvenido Admin");
                    ClssConvertTextToSpeech.getIntancia(context).reproduce("Bienvenido Admin");
                }
            }, 1500);
        } else {
            for (int i = 0; i < ClssStaticUser.nombres.length(); i++) {
                String str = ClssStaticUser.nombres.substring(i,i+1);
                if (str.equals(" ")) {
                    nombre = ClssStaticUser.nombres.substring(0,i);
                    break;
                }
            }
            if(nombre.length() == 0)
                nombre = ClssStaticUser.nombres;
            if (muser.getTipousuario().trim().equals("DC")) {
                fragment = new FrgMenuTeacher();
                ClssStaticGroup.iddocente = idDocente;
                ClssStaticGroup.docente = docente;
                Toast.makeText(context, "Bienvenido " + nombre, Toast.LENGTH_SHORT).show();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //tts.reproduce("Bienvenido " + ClssStaticGroup.docente);
                        ClssConvertTextToSpeech.getIntancia(context).reproduce("Bienvenido " + nombre);
                    }
                }, 1500);
            } else {
                fragment = new ActivityHomeUser();
                sendDataGroup(grupo);
                Toast.makeText(context, "Bienvenido " + nombre, Toast.LENGTH_SHORT).show();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ClssConvertTextToSpeech.getIntancia(context).reproduce("Bienvenido " + nombre);
                    }
                }, 1500);
                //b.putString("grupo", grupo.toString());
            }
        }
        //Creamos la información a pasar entre fragments
        b.putString("user", muser.getUsuario().trim());
        b.putString("tipousuario", muser.getTipousuario().trim());
        //Añadimos la información e iniciamos el nuevo fragment
        fragment.setArguments(b);
        FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.content, fragment).addToBackStack(null).commit();
    }

    public void setDataUserLogin(ModelUser datos) throws JSONException {
        ClssStaticUser.id = datos.getId();
        ClssStaticUser.usuario = datos.getUsuario().trim();
        ClssStaticUser.clave = datos.getClave().trim();
        ClssStaticUser.tipousuario = datos.getTipousuario();
        ClssStaticUser.activo = datos.isActivo();
        if(datos.getTipousuario().trim().equals("DC")) {
            ClssStaticUser.nombres = datos.getDocente().getString("nombres");
            ClssStaticUser.apellidos = datos.getDocente().getString("apellidos");
            ClssStaticUser.telefono = datos.getDocente().getString("telefono");
            ClssStaticUser.correo = datos.getDocente().getString("correo");
            ClssStaticUser.fechanacimiento = datos.getDocente().getString("fechanacimiento");
        }
        else if(datos.getTipousuario().trim().equals("ES")) {
            ClssStaticUser.nombres = datos.getEstudiante().getString("nombres");
            ClssStaticUser.apellidos = datos.getEstudiante().getString("apellidos");
            ClssStaticUser.telefono = datos.getEstudiante().getString("telefono");
            ClssStaticUser.correo = datos.getEstudiante().getString("correo");
            ClssStaticUser.fechanacimiento = datos.getEstudiante().getString("fechanacimiento");
        }
    }

    public void sendDataGroup(JSONObject grupo) throws JSONException {
        ClssStaticGroup.id = grupo.getInt("id");
        ClssStaticGroup.iddocente = grupo.getInt("iddocente");
        ClssStaticGroup.docente = grupo.getString("docente").trim();
        ClssStaticGroup.idestudiante = grupo.getInt("idestudiante");
        ClssStaticGroup.estudiante = grupo.getString("estudiante").trim();
        ClssStaticGroup.fecha = grupo.getString("fecha").trim();
        ClssStaticGroup.activo = grupo.getBoolean("activo");
    }
}
