package com.example.tolan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.tolan.adapters.adpEstudent;
import com.example.tolan.adapters.adpGrupo_Admin;
import com.example.tolan.models.ModelEstudent;
import com.example.tolan.models.ModelGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ActivityEstudents extends AppCompatActivity {

    Toolbar toolbar;
    RequestQueue requestQueue;
    String URL = "https://db-bartolucci.herokuapp.com/grupo/byDocente?idDocente=";
    JsonObjectRequest jsArrayRequest;
    RecyclerView revistaRcl;
    ArrayList<ModelEstudent> lstEstudents;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estudents);
        Bundle b = this.getIntent().getExtras();
        Integer in = (Integer) getIntent().getIntExtra("GrupoIDDocent",0);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        revistaRcl = new RecyclerView(this);
        revistaRcl = (RecyclerView) findViewById(R.id.rcvEstudiantes);
        revistaRcl.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        lstEstudents = new ArrayList<ModelEstudent>();
        requestQueue = Volley.newRequestQueue(this);
        URL=URL+in;
        int e5= Log.e("dataf", URL);
        jsArrayRequest = new JsonObjectRequest(Request.Method.GET, URL,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        int e5= Log.e("dataf", response.toString());
                        lstEstudents = parseJson(response);
                        adpEstudent adapter = new adpEstudent(lstEstudents);
                        revistaRcl.setAdapter(adapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        VolleyLog.e("Error: ", volleyError.getMessage());
                        System.out.println();
                        Toast.makeText(ActivityEstudents.this, "No se ha podido establecer conexión con el servidor" +
                                " " + volleyError.toString(), Toast.LENGTH_LONG).show();
                        Log.d("ERROR: ", volleyError.toString());
                    }
                });
        requestQueue.add(jsArrayRequest);
    }


    public ArrayList<ModelEstudent> parseJson(JSONObject jsonArray) {
        // Variables locales
        ArrayList<ModelEstudent> Estudents = new ArrayList();
        try {
            // Obtener el array del objeto
                try {
                    JSONArray estud = jsonArray.getJSONArray("estudiantes");
                    for (int j=0; j < estud.length();j++)
                    {
                        if(estud.length()>0) {
                            JSONObject estud_item = estud.getJSONObject(j);
                            ModelEstudent tup = new ModelEstudent(
                                    estud_item.getInt("id"),
                                    estud_item.getInt("idestudiante"),
                                    estud_item.getString("estudiante"),
                                    estud_item.getString("fecha"),
                                    estud_item.getBoolean("activo"));
                            Estudents.add(tup);
                        }
                    }

                } catch (JSONException e) {
                    int e1 = Log.e("Resu", "Error de parsing: " + e.getMessage());
                }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Estudents;
    }
}