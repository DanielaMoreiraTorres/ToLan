package com.example.tolan.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.tolan.ActivityHomeUser;
import com.example.tolan.R;
import com.example.tolan.clases.ClssConvertirTextoAVoz;
import com.example.tolan.clases.ClssStaticGrupo;
import com.example.tolan.clases.ClssValidations;
import com.example.tolan.models.ModelUser;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FrgLogin extends Fragment {

    static ClssConvertirTextoAVoz tts;
    private Button btnLogin, register;
    private TextView forgetPass, txtIni;
    private TextInputEditText user, password;
    private TextInputLayout Lusuario, Lclave;
    private ClssValidations validate;
    private String Merror= "Campo obligatorio";
    private Fragment fragment;
    private RequestQueue requestQueue;
    private String url, urlGrupo;
    JSONObject grupo;
    int idDocente = 0;

    public FrgLogin() {
        // Required empty public constructor
    }

    public static FrgLogin newInstance(String param1, String param2) {
        FrgLogin fragment = new FrgLogin();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tts = new ClssConvertirTextoAVoz();
        tts.init(getContext());
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        validate = new ClssValidations();
        grupo = new JSONObject();
        Lusuario = view.findViewById(R.id.Lusuario);
        url = getString(R.string.urlBase) + "usuario/login";
        urlGrupo = getString(R.string.urlBase) + "grupo/";
        txtIni = view.findViewById(R.id.txtIni);
        txtIni.setOnClickListener(v -> tts.reproduce(txtIni.getText().toString()));
        user = view.findViewById(R.id.txtuser);
        validate.TextChanged(user,null,Lusuario,Merror);
        Lclave = view.findViewById(R.id.Lclave);
        password = view.findViewById(R.id.txtPass);
        validate.TextChanged(password,null,Lclave,Merror);
        btnLogin = view.findViewById(R.id.btnSigin);
        btnLogin.setOnClickListener(v -> Login());
        forgetPass = view.findViewById(R.id.forget);
        forgetPass.setOnClickListener(v -> Forget());
        register = view.findViewById(R.id.register);
        register.setOnClickListener(v -> RegisterUs());
        return view;
    }

    private void Login(){
        tts.reproduce(btnLogin.getText().toString());
        if(validate.Validar(user,null,Lusuario,Merror) & validate.Validar(password,null,Lclave,Merror))
            getUsuario();
        else
            tts.reproduce("Datos no válidos");
    }

    private void getUsuario(){
        // Crear nueva cola de peticiones
        requestQueue= Volley.newRequestQueue(getContext());
        //Parámetros a enviar a la API
        Map<String, String> parameters = new HashMap<>();
        parameters.put("username", user.getText().toString().trim());
        parameters.put("password", password.getText().toString().trim());
        JsonObjectRequest request_json = new JsonObjectRequest(url, new JSONObject(parameters),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.length() > 1){
                                tts.reproduce("Inicio exitoso");
                                /*user.setText("");
                                password.setText("");*/
                                ModelUser user = new ModelUser();
                                user.setUsuario(response.getString("usuario").trim());
                                user.setClave(response.getString("clave").trim());
                                user.setTipousuario(response.getString("tipousuario").trim());
                                user.setActivo(response.getBoolean("activo"));
                                if(user.getTipousuario().equals("ES")){
                                    JSONObject ObjDatos = (JSONObject) response.get("estudiante");
                                    user.setEstudiante(ObjDatos);
                                    user.setGrupo((JSONArray) ObjDatos.get("grupo"));
                                    int idGrupo = (int) user.getGrupo().getJSONObject(0).get("id");
                                    ObtIdDocente(idGrupo, user);
                                }
                                else if(user.getTipousuario().equals("DC")){
                                    JSONObject ObjDatos = (JSONObject) response.get("docente");
                                    user.setDocente(ObjDatos);
                                    idDocente = ObjDatos.getInt("id");
                                    Iniciar(user);
                                }
                                else
                                    Iniciar(user);
                            }
                            else{
                                tts.reproduce(response.get("message").toString());
                                Toast.makeText(getContext(),response.get("message").toString(),Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            //Toast.makeText(getContext(),e.toString(),Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                tts.reproduce("Error de conexión con el servidor");
            }
        });
        // Añadir petición a la cola
        requestQueue.add(request_json);
    }

    private void ObtIdDocente(int idGrupo, ModelUser muser){
        urlGrupo = urlGrupo + idGrupo;
        // Crear nueva cola de peticiones
        requestQueue= Volley.newRequestQueue(getContext());
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
                            else
                                Toast.makeText(getContext(),response.get("message").toString(),Toast.LENGTH_LONG).show();
                        }catch (Exception e) {
                            Toast.makeText(getContext(),"El usuario no tiene docente a cargo asignado",Toast.LENGTH_LONG).show();
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
        requestQueue.add(request_json);
    }

    private void Iniciar(ModelUser muser) throws JSONException {
        Bundle b = new Bundle();
        if(muser.getTipousuario().trim().equals("AD"))
            fragment = new FrgMenuAdmin();
        else if(muser.getTipousuario().trim().equals("DC")){
            fragment = new FrgMenuDocente();
            tts.reproduce("Bienvenido "+ ClssStaticGrupo.docente);
        }
        else{
            fragment = new ActivityHomeUser();
            sendDataGroup(grupo);
            tts.reproduce("Bienvenido "+ ClssStaticGrupo.estudiante);
            //b.putString("grupo", grupo.toString());
        }
        //Creamos la información a pasar entre fragments
        b.putString("user", muser.getUsuario().trim());
        b.putString("tipousuario", muser.getTipousuario().trim());
        //Añadimos la información e iniciamos el nuevo fragment
        fragment.setArguments(b);
        getFragmentManager().beginTransaction().replace(R.id.content, fragment).addToBackStack(null).commit();
    }

    private void RegisterUs() {
        fragment = new FrgRegisterUser();
        tts.reproduce(register.getText().toString());
        getFragmentManager().beginTransaction().replace(R.id.content, fragment).addToBackStack(null).commit();
    }

    @SuppressLint("ResourceAsColor")
    private void Forget() {
        forgetPass.setTextColor(Color.parseColor("#44cccc"));
        tts.reproduce(forgetPass.getText().toString());
        fragment = new FrgRecoveryPassword();
        getFragmentManager().beginTransaction().replace(R.id.content, fragment).addToBackStack(null).commit();
    }

    public void sendDataGroup(JSONObject grupo) throws JSONException {
        ClssStaticGrupo.id = grupo.getInt("id");
        ClssStaticGrupo.iddocente = grupo.getInt("iddocente");
        ClssStaticGrupo.docente = grupo.getString("docente").trim();
        ClssStaticGrupo.idestudiante = grupo.getInt("idestudiante");
        ClssStaticGrupo.estudiante = grupo.getString("estudiante").trim();
        ClssStaticGrupo.fecha = grupo.getString("fecha").trim();
        ClssStaticGrupo.activo = grupo.getBoolean("activo");
    }
}