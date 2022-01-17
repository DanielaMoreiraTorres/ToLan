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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ActivityRegister extends AppCompatActivity {

    private RequestQueue requestQueue;
    private String url = "https://db-bartolucci.herokuapp.com/usuario";
    private TextInputEditText nombre;
    private EditText apellido;
    private EditText telefono;
    private EditText email;
    private EditText usuario;
    private EditText clave;
    private EditText confirclave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

    }

    public void RegisterClick(View view){
        nombre = findViewById(R.id.nombres);
        apellido = findViewById(R.id.apellidos);
        telefono = findViewById(R.id.telefono);
        email = findViewById(R.id.email);
        usuario = findViewById(R.id.txtusuario);
        clave = findViewById(R.id.clave);
        confirclave = findViewById(R.id.confclave);
        if(clave.getText().toString().equals(confirclave.getText().toString()))
            createUsuario();
    }

    public void createUsuario(){
        // Crear nueva cola de peticiones
        requestQueue= Volley.newRequestQueue(ActivityRegister.this);
        //Parámetros a enviar a la API
        Map<String, String>  parameters = new HashMap<>();
        parameters.put("usuario", usuario.getText().toString());
        parameters.put("clave", clave.getText().toString());
        parameters.put("tipousuario", "US");
        parameters.put("nombre", nombre.getText().toString());
        parameters.put("apellido", apellido.getText().toString());
        parameters.put("fechanacimiento", "2005-01-22");
        parameters.put("correo", email.getText().toString());
        parameters.put("telefono", telefono.getText().toString());
        parameters.put("direccion", "DIR");
        JsonObjectRequest request_json = new JsonObjectRequest(url, new JSONObject(parameters),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Toast.makeText(ActivityRegister.this,"Usuario Registrado",Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Toast.makeText(ActivityRegister.this,"Error de conexión",Toast.LENGTH_LONG).show();
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
        redirectLogin();
    }

    private void redirectLogin() {
        Intent intent = new Intent(ActivityRegister.this, ActivityLogin.class);
        startActivity(intent);
    }
}