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
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ActivityRegisterUser extends AppCompatActivity {

    private RequestQueue requestQueue;
    private String url = "https://db-bartolucci.herokuapp.com/usuario";
    private TextInputEditText nombre;
    private EditText apellido;
    private EditText telefono;
    private EditText email;
    private EditText fechaNac;
    private EditText usuario;
    private EditText clave;
    private EditText confirclave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
    }

    public void RegisterClick(View view) throws JSONException {
        nombre = findViewById(R.id.nombres);
        apellido = findViewById(R.id.apellidos);
        telefono = findViewById(R.id.telefono);
        email = findViewById(R.id.email);
        fechaNac = findViewById(R.id.txtFechaNac);
        usuario = findViewById(R.id.txtusuario);
        clave = findViewById(R.id.clave);
        confirclave = findViewById(R.id.confclave);
        if(clave.getText().toString().equals(confirclave.getText().toString()))
            createUsuario();
    }

    public void createUsuario() throws JSONException {
        // Crear nueva cola de peticiones
        requestQueue= Volley.newRequestQueue(ActivityRegisterUser.this);
        //Parámetros a enviar a la API
        JSONObject param = new JSONObject();
        JSONObject selectedDocente = new JSONObject();
        selectedDocente.put("id",1);
        selectedDocente.put("nombres",1);
        selectedDocente.put("apellidos",1);
        param.put("usuario", usuario.getText().toString());
        param.put("clave", clave.getText().toString());
        param.put("isDocente", false);
        param.put("selectedDocente", selectedDocente);
        param.put("nombres", nombre.getText().toString());
        param.put("apellidos", apellido.getText().toString());
        param.put("telefono", telefono.getText().toString());
        param.put("correo", email.getText().toString());
        param.put("fechanacimiento", fechaNac.getText().toString());
        /*
        Map<String, String>  parameters = new HashMap<>();
        parameters.put("usuario", usuario.getText().toString());
        parameters.put("clave", clave.getText().toString());
        parameters.put("isDocente", "false");
        parameters.put("nombre", nombre.getText().toString());
        parameters.put("apellido", apellido.getText().toString());
        parameters.put("fechanacimiento", fechaNac.getText().toString());
        parameters.put("correo", email.getText().toString());
        parameters.put("telefono", telefono.getText().toString());
         */
        JsonObjectRequest request_json = new JsonObjectRequest(url, param,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Toast.makeText(ActivityRegisterUser.this,"Usuario Registrado",Toast.LENGTH_LONG).show();
                            redirectLogin();
                        } catch (Exception e) {
                            Toast.makeText(ActivityRegisterUser.this,"Error de conexión",Toast.LENGTH_LONG).show();
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

    private void redirectLogin() {
        Intent intent = new Intent(ActivityRegisterUser.this, ActivityLogin.class);
        startActivity(intent);
    }
}