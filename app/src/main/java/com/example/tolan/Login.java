package com.example.tolan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class Login extends AppCompatActivity {
    private RequestQueue requestQueue;
    private StringRequest peticion;
    private String url = "https://db-bartolucci.herokuapp.com/usuario/login?user=";
    static String tipousuario;
    private EditText user;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginn);
    }

    public void Login(View view){
        user = findViewById(R.id.txtuser);
        password = findViewById(R.id.txtPass);
        if(!user.getText().equals("") & !password.getText().equals(""))
                getUsuario();
        else
            Toast.makeText(Login.this,"Usuario o clave incorrectos",Toast.LENGTH_LONG).show();
    }

    public void getUsuario(){
        // Crear nueva cola de peticiones
        requestQueue= Volley.newRequestQueue(Login.this);
        peticion = new StringRequest(Request.Method.GET, url + user.getText() +
                "&password=" + password.getText(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Convierte el string en json
                try {
                    JSONObject obj = new JSONObject(response);
                    tipousuario = obj.get("tipousuario").toString();
                    Iniciar(tipousuario);
                } catch (Throwable t) { }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Login.this,error.toString(), Toast.LENGTH_LONG ).show();
            }
        });
        // Añadir petición a la cola
        requestQueue.add(peticion);
    }

    public void Iniciar(String tipouser){
        Intent intent;
        if(tipouser.equals("AD"))
            intent = new Intent(Login.this, MenuAdmin.class);
        else
            intent = new Intent(Login.this, Login.class);
        //Creamos la información a pasar entre actividades
        Bundle b = new Bundle();
        b.putString("user", user.getText().toString());
        b.putString("password", password.getText().toString());
        //Añadimos la información al intent
        intent.putExtras(b);
        // Iniciamos la nueva actividad
        startActivity(intent);
    }
}