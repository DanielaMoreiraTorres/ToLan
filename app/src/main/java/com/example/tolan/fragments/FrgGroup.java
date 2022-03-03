package com.example.tolan.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.tolan.R;
import com.example.tolan.adapters.adpGrupo_Admin;
import com.example.tolan.clases.ClssConvertirTextoAVoz;
import com.example.tolan.clases.ClssVolleySingleton;
import com.example.tolan.models.ModelEstudent;
import com.example.tolan.models.ModelGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FrgGroup extends Fragment {

    private Toolbar toolbar;
    private Fragment fragment;
    private ProgressBar progressBar;
    private Bundle bundle;
    private SearchView searchView;
    private TextView lblTiTleG;
    //private static final String URL = "https://db-bartolucci.herokuapp.com/grupo";
    private String url;
    private JsonArrayRequest jsArrayRequest;
    private RecyclerView grupoRcl;
    private ArrayList<ModelGroup> lstGrupos;

    public FrgGroup() {
        // Required empty public constructor
    }

    public static FrgGroup newInstance(String param1, String param2) {
        FrgGroup fragment = new FrgGroup();
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
        View view = inflater.inflate(R.layout.fragment_group, container, false);
        try {
            toolbar = view.findViewById(R.id.toolbar);
            setHasOptionsMenu(true);
            ((AppCompatActivity) this.getActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity) this.getActivity()).getSupportActionBar().setTitle("");
            url = getString(R.string.urlBase) + "grupo";
            progressBar = view.findViewById(R.id.progressBar);
            lblTiTleG = view.findViewById(R.id.lblTiTleG);
            lblTiTleG.setOnClickListener(v -> ClssConvertirTextoAVoz.getIntancia(v.getContext()).reproduce(lblTiTleG.getText().toString().trim()));
            grupoRcl = (RecyclerView) view.findViewById(R.id.rcvGrupos);
            grupoRcl.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            lstGrupos = new ArrayList<ModelGroup>();
            getGroups();
        } catch (Exception e) {}
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_toolbar, menu);
        MenuItem mc = menu.findItem(R.id.btnCaritas);
        mc.setVisible(false);
        MenuItem mr = menu.findItem(R.id.btnRecompensa);
        mr.setVisible(false);
    }

    private void getGroups(){
        try {
            progressBar.setVisibility(View.VISIBLE);
            jsArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            if(response.length() > 0) {
                                lstGrupos = parseJson(response);
                                adpGrupo_Admin adapter = new adpGrupo_Admin(lstGrupos);
                                adapter.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        int opcselec = grupoRcl.getChildAdapterPosition(view);
                                        ClssConvertirTextoAVoz.getIntancia(getContext()).
                                                reproduce("Grupo de docente: " + lstGrupos.get(opcselec).getDocente());
                                        fragment = new FrgStudent();
                                        bundle = new Bundle();
                                        bundle.putInt("GrupoIDDocent", lstGrupos.get(opcselec).getIddocente());
                                        fragment.setArguments(bundle);
                                        final Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                getFragmentManager().beginTransaction().replace(R.id.content, fragment).addToBackStack(null).commit();
                                            }
                                        }, 750);
                                /*Intent intent = new Intent(activity_group_admin.this, ActivityEstudents.class);
                                intent.putExtra("GrupoIDDocent", lstGrupos.get(opcselec).getIddocente());
                                int e4= Log.e("dataNollega",lstGrupos.get(opcselec).getIddocente().toString());
                                startActivity(intent);*/
                                    }
                                });
                                if (getContext() != null)
                                    grupoRcl.setAdapter(adapter);
                                progressBar.setVisibility(View.GONE);
                            }
                            else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getContext(),"No existen grupos registrados",Toast.LENGTH_SHORT).show();
                                ClssConvertirTextoAVoz.clssConvertirTextoAVoz.reproduce("No existen grupos registrados");
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            VolleyLog.e("Error: ", volleyError.getMessage());
                            Toast.makeText(getContext(), "No se ha podido establecer conexión con el servidor\n Intente nuevamente", Toast.LENGTH_SHORT);
                            ClssConvertirTextoAVoz.getIntancia(getContext()).reproduce("No se ha podido establecer conexión con el servidor\nIntente nuevamente");
                            progressBar.setVisibility(View.GONE);
                        /*Toast.makeText(getContext(), "No se ha podido establecer conexión con el servidor" +
                                " " + volleyError.toString(), Toast.LENGTH_LONG).show();*/
                        }
                    });
            ClssVolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(jsArrayRequest);
        } catch (Exception e) {}
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
                            estudentList.add(tup);
                        }
                    }
                    grupo = new ModelGroup(
                            null,
                            objeto.getInt("iddocente"),
                            objeto.getString("docente"),
                            estudentList
                    );
                    Grupos.add(grupo);
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