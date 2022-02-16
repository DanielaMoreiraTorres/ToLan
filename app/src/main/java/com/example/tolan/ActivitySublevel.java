package com.example.tolan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.tolan.adapters.AdpSublevel;
import com.example.tolan.models.ModelLevel;
import com.example.tolan.models.ModelSublevel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ActivitySublevel extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Toolbar toolbar;
    FloatingActionButton fab;
    private RequestQueue requestQueue;
    private JsonArrayRequest jsonArrayRequest;
    private String urlN = "https://db-bartolucci.herokuapp.com/nivel";
    private String url = "https://db-bartolucci.herokuapp.com/subnivel";
    Spinner cbbNiveles;
    int niv = 0;
    ArrayList<ModelLevel> levels;
    ArrayList nivelesList = new ArrayList();
    ArrayList<ModelSublevel> sublevels;
    private RecyclerView rcvSublevels;
    AdpSublevel adpSublevel;
    ModelSublevel sublevelSelected = new ModelSublevel();
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sublevel);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        levels = new ArrayList<>();
        nivelesList = new ArrayList();
        llenarSpinner();
        cbbNiveles = (Spinner) findViewById(R.id.cbbNivel);
        cbbNiveles.setOnItemSelectedListener(this);
        rcvSublevels = findViewById(R.id.rcvSubnivel);
        rcvSublevels.setLayoutManager(new LinearLayoutManager(this));

        fab = findViewById(R.id.addSubNivel);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivitySublevel.this, ActivityAddLevel.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.btnNotifi) {

        }
        if(id == R.id.btnLogIn) {
            Intent intent = new Intent(this, ActivityLogin.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }
        if(id == R.id.btnContacts) {
            Intent intent = new Intent(this, ActivityContact.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void llenarSpinner(){
        // Crear nueva cola de peticiones
        requestQueue = Volley.newRequestQueue(this);
        jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlN, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ModelLevel level = null;
                        try {
                            for (int i=0;i<response.length();i++){
                                level = new ModelLevel();
                                JSONObject objLevel = response.getJSONObject(i);
                                level.setId(objLevel.getInt("id"));
                                level.setNombre(objLevel.getString("nombre"));
                                level.setDescripcion(objLevel.getString("descripcion"));
                                level.setPrioridad(objLevel.getInt("prioridad"));
                                level.setPublicid(objLevel.getString("publicid"));
                                level.setUrl(objLevel.getString("url"));
                                level.setActivo(objLevel.getBoolean("activo"));
                                levels.add(level);
                                nivelesList.add(level.getNombre());
                            }
                            nivelesList.add(0,"Todos");
                            cbbNiveles.setAdapter(new ArrayAdapter<ModelLevel>(ActivitySublevel.this,
                                    android.R.layout.simple_spinner_dropdown_item,nivelesList));
                        }catch (JSONException e){
                            e.printStackTrace();
                            Toast.makeText(ActivitySublevel.this,e.toString(),Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e("Error: ", error.getMessage());
                    }
                });
        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(i != 0)
            niv = levels.get(i-1).getId();
        else
            niv = 0;
        getS();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        niv = 0;
        getS();
    }

    private void getS(){
        sublevels = new ArrayList<>();
        if (niv != 0)
            getSublevels(url+"/byNivel?idNivel="+niv);
        else
            getSublevels(url);
    }

    public void getSublevels(String urlR){
        // Crear nueva cola de peticiones
        requestQueue = Volley.newRequestQueue(this);
        jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlR, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ModelSublevel sublevel = null;
                        try {
                            for (int i=0;i<response.length();i++){
                                sublevel = new ModelSublevel();
                                JSONObject objSublevel = response.getJSONObject(i);
                                sublevel.setId(objSublevel.getInt("id"));
                                sublevel.setNombre(objSublevel.getString("nombre"));
                                sublevel.setDescripcion(objSublevel.getString("descripcion"));
                                sublevel.setNumactividades(objSublevel.getInt("numactividades"));
                                sublevel.setPrioridad(objSublevel.getInt("prioridad"));
                                sublevel.setPublicid(objSublevel.getString("publicid"));
                                sublevel.setUrl(objSublevel.getString("url"));
                                sublevel.setActivo(objSublevel.getBoolean("activo"));
                                sublevels.add(sublevel);
                            }
                            Collections.sort(sublevels, new Comparator<ModelSublevel>() {
                                @Override
                                public int compare(ModelSublevel s1, ModelSublevel s2) {
                                    return new Integer(s1.getPrioridad()).compareTo(new Integer(s2.getPrioridad()));
                                }
                            });
                            adpSublevel = new AdpSublevel(ActivitySublevel.this, sublevels);
                            rcvSublevels.setAdapter(adpSublevel);
                            adpSublevel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    int opcselec = rcvSublevels.getChildAdapterPosition(view);
                                    sublevelSelected = sublevels.get(opcselec);
                                    bundle = new Bundle();
                                    bundle.putString("levelSelected", new Gson().toJson(sublevelSelected));
                                    Intent intent = new Intent(ActivitySublevel.this, ActivityAddLevel.class);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            });
                        }catch (JSONException e){
                            e.printStackTrace();
                            Toast.makeText(ActivitySublevel.this,e.toString(),Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        JsonObjectRequest request_json = new JsonObjectRequest(urlR, null,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            adpSublevel = new AdpSublevel(ActivitySublevel.this, sublevels);
                                            rcvSublevels.setAdapter(adpSublevel);
                                            Toast.makeText(ActivitySublevel.this,response.getString("message"),Toast.LENGTH_LONG).show();
                                        } catch (Exception e) {
                                            Toast.makeText(ActivitySublevel.this,e.toString(),Toast.LENGTH_LONG).show();                                        }
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
                });
        requestQueue.add(jsonArrayRequest);
    }
}