package com.example.tolan;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    private RequestQueue requestQueue;
    private StringRequest peticion;
    private String url = "https://db-bartolucci.herokuapp.com/usuario/";
    EditText nombre;
    EditText apellido;
    EditText telefono;
    EditText email;
    EditText usuario;
    EditText clave;
    EditText confirclave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();
    }

    public void RegisterClick(View view){
        nombre = findViewById(R.id.nombres);
        apellido = findViewById(R.id.apellidos);
        telefono = findViewById(R.id.telefono);
        email = findViewById(R.id.email);
        usuario = findViewById(R.id.usuario);
        clave = findViewById(R.id.clave);
        confirclave = findViewById(R.id.confclave);
        createUsuario();
    }

    public void createUsuario(){
        // Crear nueva cola de peticiones
        requestQueue= Volley.newRequestQueue(Register.this);
        peticion = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(Register.this,"Usuario registrado", Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Register.this,error.toString(), Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  parameters = new HashMap<>();
                parameters.put("usuario", usuario.getText().toString());
                parameters.put("clave", clave.getText().toString());
                parameters.put("tipousuario", "");
                parameters.put("nombre", nombre.getText().toString());
                parameters.put("apellido", apellido.getText().toString());
                parameters.put("fechanacimiento", "");
                parameters.put("correo", email.getText().toString());
                parameters.put("telefono", telefono.getText().toString());
                parameters.put("direccion", "");
                parameters.put("stockcaritas", "0");
                return parameters;
            }
        };
        // Añadir petición a la cola
        requestQueue.add(peticion);
    }
}