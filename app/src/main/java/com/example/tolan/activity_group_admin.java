package com.example.tolan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tolan.adapters.adpGrupo_Admin;
import com.example.tolan.models.ModelEstudent;
import com.example.tolan.models.ModelGroup;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class activity_group_admin extends AppCompatActivity {

    Toolbar toolbar;
    RequestQueue requestQueue;
    private static final String URL = "https://db-bartolucci.herokuapp.com/grupo";
    JsonArrayRequest jsArrayRequest;

    RecyclerView revistaRcl;
    ModelGroup modelGroup;
    Gson gson;
    ArrayList<ModelGroup> lstGrupos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_admin);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        revistaRcl = new RecyclerView(this);
        revistaRcl = (RecyclerView) findViewById(R.id.rcvGrupos);
        revistaRcl.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        lstGrupos = new ArrayList<ModelGroup>();
        String url = "https://db-bartolucci.herokuapp.com/grupo";
        requestQueue = Volley.newRequestQueue(this);
        gson = new Gson();
        jsArrayRequest = new JsonArrayRequest(Request.Method.GET, URL,null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        lstGrupos = parseJson(response);
                        adpGrupo_Admin adapter = new adpGrupo_Admin(lstGrupos);
                        revistaRcl.setAdapter(adapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        VolleyLog.e("Error: ", volleyError.getMessage());
                        System.out.println();
                        Toast.makeText(activity_group_admin.this, "No se ha podido establecer conexión con el servidor" +
                                " " + volleyError.toString(), Toast.LENGTH_LONG).show();
                        Log.d("ERROR: ", volleyError.toString());
                    }
                });
        requestQueue.add(jsArrayRequest);
    }


    public ArrayList<ModelGroup> parseJson(JSONArray jsonArray) {
        // Variables locales
        ArrayList<ModelGroup> Grupos = new ArrayList();
        ModelGroup grupo;

        try {
            // Obtener el array del objeto
            for (int i = 0; i < jsonArray.length(); i++) {
                List<ModelEstudent> estudentList = new ArrayList<>();
                try {
                    JSONObject objeto = jsonArray.getJSONObject(i);
                    JSONArray estud = objeto.getJSONArray("estudiantes");
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
                            int e3= Log.e("datag", String.valueOf(estud_item.getInt("id")));
                            estudentList.add(tup);
                            int e5= Log.e("dataf", String.valueOf(estudentList.size()));
                        }
                    }
                    grupo = new ModelGroup(
                            null,
                            objeto.getInt("iddocente"),
                            objeto.getString("docente"),
                            estudentList
                    );
                    Grupos.add(grupo);
                    int e2= Log.e("dataNollega",grupo.toString());

                } catch (JSONException e) {
                    int e1 = Log.e("Resu", "Error de parsing: " + e.getMessage());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Grupos;
    }
}

