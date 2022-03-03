package com.example.tolan.fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.tolan.ActivityHomeUser;
import com.example.tolan.R;
import com.example.tolan.clases.ClssConvertirTextoAVoz;
import com.example.tolan.clases.ClssStaticGrupo;
import com.example.tolan.clases.ClssValidations;
import com.example.tolan.clases.ClssVolleySingleton;
import com.example.tolan.models.ModelUser;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FrgLogin extends Fragment {

    ProgressBar progressBar;
    //static ClssConvertirTextoAVoz tts;
    private Button btnLogin, register;
    private TextView forgetPass, txtIni;
    private TextInputEditText user, password;
    private TextInputLayout Lusuario, Lclave;
    private ClssValidations validate;
    private String Merror = "Campo obligatorio";
    private Fragment fragment;
    //private RequestQueue requestQueue;
    private String url, urlGrupo;
    JSONObject grupo;
    String docente;
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
        //tts = new ClssConvertirTextoAVoz();
        //tts.init(getContext());
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        try {
            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
            validate = new ClssValidations();
            progressBar = view.findViewById(R.id.progressBar);
            grupo = new JSONObject();
            Lusuario = view.findViewById(R.id.Lusuario);
            url = getString(R.string.urlBase) + "usuario/login";
            urlGrupo = getString(R.string.urlBase) + "grupo/";
            txtIni = view.findViewById(R.id.txtIni);
            txtIni.setOnClickListener(v -> ClssConvertirTextoAVoz.getIntancia(v.getContext()).reproduce(txtIni.getText().toString()));
            //tts.reproduce(txtIni.getText().toString()));
            user = view.findViewById(R.id.txtuser);
            validate.TextChanged(user, null, Lusuario, Merror);
            Lclave = view.findViewById(R.id.Lclave);
            password = view.findViewById(R.id.txtPass);
            validate.TextChanged(password, null, Lclave, Merror);
            btnLogin = view.findViewById(R.id.btnSigin);
            btnLogin.setOnClickListener(v -> Login());
            forgetPass = view.findViewById(R.id.forget);
            forgetPass.setOnClickListener(v -> Forget());
            register = view.findViewById(R.id.register);
            register.setOnClickListener(v -> RegisterUs());
        } catch (Exception e) {
        }
        return view;
    }

    private void Login() {
        //Ocultar teclado
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(password.getWindowToken(), 0);
        //tts.reproduce(btnLogin.getText().toString());
        ClssConvertirTextoAVoz.getIntancia(getContext()).reproduce(btnLogin.getText().toString());
        if (validate.Validar(user, null, Lusuario, Merror) & validate.Validar(password, null, Lclave, Merror)) {
            progressBar.setVisibility(View.VISIBLE);
            getUsuario();
        } else
            //tts.reproduce("Datos no válidos");
            ClssConvertirTextoAVoz.getIntancia(getContext()).reproduce("Datos no válidos");
    }

    private void getUsuario() {
        try {
            // Crear nueva cola de peticiones
            //requestQueue = Volley.newRequestQueue(getContext());
            //Parámetros a enviar a la API
            Map<String, String> parameters = new HashMap<>();
            parameters.put("username", user.getText().toString().trim());
            parameters.put("password", password.getText().toString().trim());
        /*user.setText("");
        password.setText("");*/
            JsonObjectRequest request_json = new JsonObjectRequest(url, new JSONObject(parameters),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response.length() > 1) {
                                    ModelUser user = new ModelUser();
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
                                        docente = ObjDatos.getString("nombres") + ObjDatos.getString("apellidos");
                                        Iniciar(user);
                                    } else
                                        Iniciar(user);
                                    //tts.reproduce("Inicio exitoso");
                                    ClssConvertirTextoAVoz.getIntancia(getContext()).reproduce("Inicio exitoso");
                                    progressBar.setVisibility(View.GONE);
                                } else {
                                    //tts.reproduce(response.get("message").toString());
                                    Toast.makeText(getContext(), response.get("message").toString(), Toast.LENGTH_SHORT).show();
                                    ClssConvertirTextoAVoz.getIntancia(getContext()).reproduce(response.get("message").toString());
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
                    Toast.makeText(getContext(), "Error de conexión con el servidor\nIntente nuevamente", Toast.LENGTH_SHORT).show();
                    //tts.reproduce("Error de conexión con el servidor. Intente nuevamente");
                    ClssConvertirTextoAVoz.getIntancia(getContext()).reproduce("Error de conexión con el servidor. Intente nuevamente");
                    progressBar.setVisibility(View.GONE);
                }
            });
            // Añadir petición a la cola
            //requestQueue.add(request_json);
            ClssVolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(request_json);
        } catch (Exception e) {}
    }

    private void ObtIdDocente(int idGrupo, ModelUser muser) {
        urlGrupo = urlGrupo + idGrupo;
        // Crear nueva cola de peticiones
        //requestQueue = Volley.newRequestQueue(getContext());
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
                            /*else
                                Toast.makeText(getContext(), response.get("message").toString(), Toast.LENGTH_LONG).show();*/
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
        //requestQueue.add(request_json);
        ClssVolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(request_json);
    }

    private void Iniciar(ModelUser muser) throws JSONException {
        Bundle b = new Bundle();
        if (muser.getTipousuario().trim().equals("AD")) {
            fragment = new FrgMenuAdmin();
            Toast.makeText(getContext(), "Bienvenido Admin", Toast.LENGTH_SHORT).show();
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //tts.reproduce("Bienvenido Admin");
                    ClssConvertirTextoAVoz.getIntancia(getContext()).reproduce("Bienvenido Admin");
                }
            }, 1500);
        } else if (muser.getTipousuario().trim().equals("DC")) {
            fragment = new FrgMenuDocente();
            ClssStaticGrupo.iddocente = idDocente;
            ClssStaticGrupo.docente = docente;
            Toast.makeText(getContext(), "Bienvenido " + ClssStaticGrupo.docente, Toast.LENGTH_SHORT).show();
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //tts.reproduce("Bienvenido " + ClssStaticGrupo.docente);
                    ClssConvertirTextoAVoz.getIntancia(getContext()).reproduce("Bienvenido " + ClssStaticGrupo.docente);
                }
            }, 1500);
        } else {
            fragment = new ActivityHomeUser();
            sendDataGroup(grupo);
            Toast.makeText(getContext(), "Bienvenido " + ClssStaticGrupo.estudiante, Toast.LENGTH_SHORT).show();
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //tts.reproduce("Bienvenido " + ClssStaticGrupo.estudiante);
                    ClssConvertirTextoAVoz.getIntancia(getContext()).reproduce("Bienvenido " + ClssStaticGrupo.estudiante);
                }
            }, 1500);
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
        //tts.reproduce(register.getText().toString());
        ClssConvertirTextoAVoz.getIntancia(getContext()).reproduce(register.getText().toString());
        fragment = new FrgRegisterUser();
        getFragmentManager().beginTransaction().replace(R.id.content, fragment).addToBackStack(null).commit();
    }

    @SuppressLint("ResourceAsColor")
    private void Forget() {
        forgetPass.setTextColor(Color.parseColor("#44cccc"));
        //tts.reproduce(forgetPass.getText().toString());
        ClssConvertirTextoAVoz.getIntancia(getContext()).reproduce(forgetPass.getText().toString());
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