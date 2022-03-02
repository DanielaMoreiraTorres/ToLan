package com.example.tolan.fragments;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.tolan.R;
import com.example.tolan.adapters.AdpSublevel;
import com.example.tolan.clases.ClssConvertirTextoAVoz;
import com.example.tolan.clases.ClssVolleySingleton;
import com.example.tolan.models.ModelLevel;
import com.example.tolan.models.ModelSublevel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class FrgSublevel extends Fragment implements AdapterView.OnItemSelectedListener,
SearchView.OnQueryTextListener {

    private Toolbar toolbar;
    private Fragment fragment;
    private ProgressBar progressBar;
    private FloatingActionButton fab;
    private SearchView searchView;
    private CardView cvSel;
    private RelativeLayout lySel;
    private TextView lblTiTleSubNiv;
    private JsonArrayRequest jsonArrayRequest;
    private String url, urlN;
    private Spinner cbbNiveles;
    ModelLevel levelsel = null;
    int niv = 0;
    private ArrayList<ModelLevel> levels;
    private ArrayList nivelesList = new ArrayList();
    private ArrayList<ModelSublevel> sublevels;
    private RecyclerView rcvSublevels;
    private AdpSublevel adpSublevel;
    private ModelSublevel sublevelSelected = new ModelSublevel();
    private Bundle bundle;

    public FrgSublevel() {
        // Required empty public constructor
    }

    public static FrgSublevel newInstance(String param1, String param2) {
        FrgSublevel fragment = new FrgSublevel();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            levelsel = (ModelLevel) getArguments().getSerializable("levelSelected");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sublevel, container, false);
        try{
            toolbar = view.findViewById(R.id.toolbar);
            setHasOptionsMenu(true);
            ((AppCompatActivity) this.getActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity) this.getActivity()).getSupportActionBar().setTitle("");
            url = getString(R.string.urlBase) + "subnivel";
            urlN = getString(R.string.urlBase) + "nivel";
            progressBar = view.findViewById(R.id.progressBar);
            levels = new ArrayList<>();
            nivelesList = new ArrayList();
            lblTiTleSubNiv = view.findViewById(R.id.lblTiTleSubNiv);
            lblTiTleSubNiv.setOnClickListener(v -> ClssConvertirTextoAVoz.getIntancia(v.getContext()).reproduce(lblTiTleSubNiv.getText().toString()));
            searchView = view.findViewById(R.id.busquedaSubniv);
            searchView.setOnQueryTextListener(this);
            cbbNiveles = (Spinner) view.findViewById(R.id.cbbNivel);
            cbbNiveles.setOnItemSelectedListener(this);
            llenarSpinner();
            rcvSublevels = view.findViewById(R.id.rcvSubnivel);
            rcvSublevels.setLayoutManager(new LinearLayoutManager(getContext()));
            fab = view.findViewById(R.id.addSubNivel);
            fab.setOnClickListener(v -> addSublevel());
            if (levelsel != null){
                niv = levelsel.getId();
                getS();
            }
        }
        catch (Exception e) { }
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

    public void llenarSpinner(){
        // Crear nueva cola de peticiones
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
                            if(getContext() != null) {
                                nivelesList.add(0, "Todos");
                                cbbNiveles.setAdapter(new ArrayAdapter<ModelLevel>(getContext(),
                                        android.R.layout.simple_spinner_dropdown_item, nivelesList));
                            }
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
        ClssVolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(jsonArrayRequest);
    }

    private void getS(){
        progressBar.setVisibility(View.VISIBLE);
        sublevels = new ArrayList<>();
        if (niv != 0)
            getSublevels(url+"/byNivel?idNivel="+niv);
        else
            getSublevels(url);
    }

    public void getSublevels(String urlR){
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
                                    return new Integer(s1.getId()).compareTo(new Integer(s2.getId()));
                                }
                            });
                            if(getContext() != null) {
                                adpSublevel = new AdpSublevel(getContext(), sublevels);
                                rcvSublevels.setAdapter(adpSublevel);
                                adpSublevel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        //int opcselec = rcvSublevels.getChildAdapterPosition(view);
                                        cvSel = (CardView) view;
                                        lySel = (RelativeLayout) cvSel.getParent();
                                        int opcselec = cvSel.getId();
                                        sublevelSelected = sublevels.get(opcselec);
                                        lySel.setBackgroundResource(R.drawable.borde);
                                        lySel.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#44CCCC")));
                                        Toast.makeText(getContext(), sublevelSelected.getNombre().trim(), Toast.LENGTH_SHORT);
                                        ClssConvertirTextoAVoz.getIntancia(getContext()).reproduce(sublevelSelected.getNombre());
                                        bundle = new Bundle();
                                        //bundle.putString("sublevelSelected", new Gson().toJson(sublevelSelected));
                                        bundle.putSerializable("sublevelSelected", sublevelSelected);
                                    /*fragment = new FrgSublevel();
                                    fragment.setArguments(bundle);
                                    final Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            getFragmentManager().beginTransaction().replace(R.id.content, fragment).addToBackStack(null).commit();
                                        }
                                    }, 750);*/
                                    }
                                });
                            }
                            progressBar.setVisibility(View.GONE);
                        }catch (JSONException e){
                            e.printStackTrace();
                            //Toast.makeText(getContext(),e.toString(),Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
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
                                            adpSublevel = new AdpSublevel(getContext(), sublevels);
                                            rcvSublevels.setAdapter(adpSublevel);
                                            Toast.makeText(getContext(),response.getString("message"),Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);
                                        } catch (Exception e) {
                                            //Toast.makeText(getContext(),e.toString(),Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                VolleyLog.e("Error: ", error.getMessage());
                                progressBar.setVisibility(View.GONE);
                            }
                        });
                        ClssVolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(request_json);
                    }
                });
        ClssVolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(jsonArrayRequest);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(levelsel != null){
            for (int c=0; c<cbbNiveles.getCount(); c++){
                if(cbbNiveles.getItemAtPosition(c).toString().trim().equals(levelsel.getNombre().trim())){
                    cbbNiveles.setSelection(c);
                    return;
                }
            }
        }
        else {
            ClssConvertirTextoAVoz.getIntancia(getContext()).reproduce(cbbNiveles.getItemAtPosition(i).toString().trim());
            if(i != 0)
                niv = levels.get(i-1).getId();
            else
                niv = 0;
            getS();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) { }

    private void addSublevel(){
        fragment = new FrgAddSublevel();
        ClssConvertirTextoAVoz.getIntancia(getContext()).reproduce("Agregar subnivel");
        getFragmentManager().beginTransaction().replace(R.id.content, fragment).addToBackStack(null).commit();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adpSublevel.filtrado(newText);
        return false;
    }
}