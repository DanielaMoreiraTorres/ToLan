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
import android.widget.Toast;
import com.example.tolan.R;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.tolan.adapters.AdpLevel;
import com.example.tolan.models.ModelLevel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ActivityLevel extends AppCompatActivity {

    private Toolbar toolbar;
    private FloatingActionButton fab;
    private RecyclerView rcvLevels;
    private RequestQueue requestQueue;
    private JsonArrayRequest jsonArrayRequest;
    private String url = "https://db-bartolucci.herokuapp.com/nivel";
    ArrayList<ModelLevel> levels;
    AdpLevel adpLevel;
    ModelLevel levelSelected = new ModelLevel();
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        levels = new ArrayList<>();
        //Vincular instancia del recyclerview
        rcvLevels = (RecyclerView) findViewById(R.id.rcvNiveles);
        //Definir la forma de la lista vertical
        rcvLevels.setLayoutManager(new LinearLayoutManager(ActivityLevel.this));

        fab = findViewById(R.id.addNivel);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityLevel.this, ActivityAddLevel.class);
                startActivity(intent);
            }
        });

        getLevels();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        /*if(id == R.id.btnMyInfo) {

        }*/
        if(id == R.id.btnLogIn) {
            /*Intent intent = new Intent(this, ActivityLogin.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);*/
        }
        if(id == R.id.btnContacts) {
            Intent intent = new Intent(this, ActivityContact.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void getLevels(){
        // Crear nueva cola de peticiones
        requestQueue = Volley.newRequestQueue(this);
        jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
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
                            }
                            Collections.sort(levels, new Comparator<ModelLevel>() {
                                @Override
                                public int compare(ModelLevel l1, ModelLevel l2) {
                                    return new Integer(l1.getPrioridad()).compareTo(new Integer(l2.getPrioridad()));
                                }
                            });
                            adpLevel = new AdpLevel(ActivityLevel.this, levels);
                            rcvLevels.setAdapter(adpLevel);
                            adpLevel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    int opcselec = rcvLevels.getChildAdapterPosition(view);
                                    levelSelected = levels.get(opcselec);
                                    bundle = new Bundle();
                                    bundle.putString("levelSelected", new Gson().toJson(levelSelected));
                                    Intent intent = new Intent(ActivityLevel.this, ActivitySublevel.class);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            });
                        }catch (JSONException e){
                            e.printStackTrace();
                            Toast.makeText(ActivityLevel.this,e.toString(),Toast.LENGTH_LONG).show();
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
}