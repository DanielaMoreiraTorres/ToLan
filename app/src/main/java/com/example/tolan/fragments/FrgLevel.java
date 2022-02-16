package com.example.tolan.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.tolan.ActivityAddLevel;
import com.example.tolan.ActivityContact;
import com.example.tolan.ActivityLevel;
import com.example.tolan.ActivitySublevel;
import com.example.tolan.R;
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

public class FrgLevel extends Fragment {

    private Toolbar toolbar;
    private Fragment fragment;
    private FloatingActionButton fab;
    private RecyclerView rcvLevels;
    private RequestQueue requestQueue;
    private JsonArrayRequest jsonArrayRequest;
    private String url = "https://db-bartolucci.herokuapp.com/nivel";
    ArrayList<ModelLevel> levels;
    AdpLevel adpLevel;
    ModelLevel levelSelected = new ModelLevel();
    Bundle bundle;

    public FrgLevel() {
        // Required empty public constructor
    }

    public static FrgLevel newInstance(String param1, String param2) {
        FrgLevel fragment = new FrgLevel();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_frg_level, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
        setHasOptionsMenu(true);
        levels = new ArrayList<>();
        //Vincular instancia del recyclerview
        rcvLevels = (RecyclerView) view.findViewById(R.id.rcvNiveles);
        //Definir la forma de la lista vertical
        rcvLevels.setLayoutManager(new LinearLayoutManager(getContext()));

        fab = view.findViewById(R.id.addNivel);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ActivityAddLevel.class);
                startActivity(intent);
            }
        });

        getLevels();
        return view;
    }

    public void getLevels(){
        // Crear nueva cola de peticiones
        requestQueue = Volley.newRequestQueue(getContext());
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
                            adpLevel = new AdpLevel(getContext(), levels);
                            rcvLevels.setAdapter(adpLevel);
                            adpLevel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    int opcselec = rcvLevels.getChildAdapterPosition(view);
                                    levelSelected = levels.get(opcselec);
                                    bundle = new Bundle();
                                    bundle.putString("levelSelected", new Gson().toJson(levelSelected));
                                    Intent intent = new Intent(getContext(), ActivitySublevel.class);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            });
                        }catch (JSONException e){
                            e.printStackTrace();
                            Toast.makeText(getContext(),e.toString(),Toast.LENGTH_LONG).show();
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