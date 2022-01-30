package com.example.tolan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ActivityLogin extends AppCompatActivity {

    FloatingActionButton btnLogin;
    private RequestQueue requestQueue;
    private String url = "https://db-bartolucci.herokuapp.com/usuario/login";
    static String tipousuario;
    private EditText user;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btnSigin);
        btnLogin.setOnClickListener(v -> Login());
    }

    public void Login(){
        user = findViewById(R.id.txtuser);
        password = findViewById(R.id.txtPass);
        if(!user.getText().toString().isEmpty() & !password.getText().toString().isEmpty())
                getUsuario();
        else
            Toast.makeText(ActivityLogin.this,"Usuario y/o clave incorrectos",Toast.LENGTH_LONG).show();
    }

    public void getUsuario(){
        // Crear nueva cola de peticiones
        requestQueue= Volley.newRequestQueue(ActivityLogin.this);
        //Parámetros a enviar a la API
        Map<String, String> parameters = new HashMap<>();
        parameters.put("username", user.getText().toString());
        parameters.put("password", password.getText().toString());
        JsonObjectRequest request_json = new JsonObjectRequest(url, new JSONObject(parameters),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            tipousuario = response.get("tipousuario").toString();
                            Iniciar(tipousuario);
                        } catch (Exception e) {
                            Toast.makeText(ActivityLogin.this,"Usuario y/o clave incorrectos",Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });
        // Añadir petición a la cola
        requestQueue.add(request_json);
    }

    public void Iniciar(String tipouser){
        Intent intent;
        if(tipouser.equals("AD"))
            intent = new Intent(ActivityLogin.this, ActivityMenuAdmin.class);
        else
            intent = new Intent(ActivityLogin.this, ActivityLogin.class);
        //Creamos la información a pasar entre actividades
        Bundle b = new Bundle();
        b.putString("user", user.getText().toString());
        b.putString("password", password.getText().toString());
        //Añadimos la información al intent
        intent.putExtras(b);
        // Iniciamos la nueva actividad
        startActivity(intent);
    }

    public void RegisterUs(View v) {
        Intent intent = new Intent(this, ActivityRegisterUser.class);
        startActivity(intent);
    }
}