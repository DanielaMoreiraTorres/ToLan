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

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.tolan.R;
import com.example.tolan.adapters.AdpLevel;
import com.example.tolan.clases.ClssConvertirTextoAVoz;
import com.example.tolan.clases.ClssVolleySingleton;
import com.example.tolan.models.ModelLevel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class FrgLevel extends Fragment implements SearchView.OnQueryTextListener {

    private Toolbar toolbar;
    private Fragment fragment;
    private ProgressBar progressBar;
    private TextView lblTiTleNiv;
    private FloatingActionButton fab;
    private SearchView searchView;
    private RecyclerView rcvLevels;
    private CardView cvSel;
    private RelativeLayout lySel;
    private JsonArrayRequest jsonArrayRequest;
    private String url;
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
        View view = inflater.inflate(R.layout.fragment_level, container, false);
        try {
            toolbar = view.findViewById(R.id.toolbar);
            setHasOptionsMenu(true);
            ((AppCompatActivity) this.getActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity) this.getActivity()).getSupportActionBar().setTitle("");
            url = getString(R.string.urlBase) + "nivel";
            progressBar = view.findViewById(R.id.progressBar);
            levels = new ArrayList<>();
            lblTiTleNiv = view.findViewById(R.id.lblTiTleNiv);
            lblTiTleNiv.setOnClickListener(v -> ClssConvertirTextoAVoz.getIntancia(v.getContext()).reproduce(lblTiTleNiv.getText().toString()));
            searchView = view.findViewById(R.id.busquedaNiv);
            searchView.setOnQueryTextListener(this);
            rcvLevels = (RecyclerView) view.findViewById(R.id.rcvNiveles);
            //Definir la forma de la lista vertical
            rcvLevels.setLayoutManager(new LinearLayoutManager(getContext()));
            fab = view.findViewById(R.id.addNivel);
            fab.setOnClickListener(v -> addLevel());
            getLevels();
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

    public void getLevels(){
        try {
            progressBar.setVisibility(View.VISIBLE);
            jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            ModelLevel level = null;
                            try {
                                for (int i = 0; i < response.length(); i++) {
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
                                if(levels.size() > 0) {
                                    Collections.sort(levels, new Comparator<ModelLevel>() {
                                        @Override
                                        public int compare(ModelLevel l1, ModelLevel l2) {
                                            return new Integer(l1.getId()).compareTo(new Integer(l2.getId()));
                                        }
                                    });
                                    if (getContext() != null) {
                                        adpLevel = new AdpLevel(getContext(), levels);
                                        rcvLevels.setAdapter(adpLevel);
                                        adpLevel.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                //int opcselec = rcvLevels.getChildAdapterPosition(view);
                                                cvSel = (CardView) view;
                                                lySel = (RelativeLayout) cvSel.getParent();
                                                int opcselec = cvSel.getId();
                                                levelSelected = levels.get(opcselec);
                                                lySel.setBackgroundResource(R.drawable.borde);
                                                lySel.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#44CCCC")));
                                                Toast.makeText(getContext(), levelSelected.getNombre().trim(), Toast.LENGTH_SHORT);
                                                ClssConvertirTextoAVoz.getIntancia(getContext()).reproduce(levelSelected.getNombre());
                                                bundle = new Bundle();
                                                //bundle.putString("levelSelected", new Gson().toJson(levelSelected));
                                                bundle.putSerializable("levelSelected", levelSelected);
                                                fragment = new FrgSublevel();
                                                fragment.setArguments(bundle);
                                                final Handler handler = new Handler();
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        getFragmentManager().beginTransaction().replace(R.id.content, fragment).addToBackStack(null).commit();
                                                    }
                                                }, 750);
                                            }
                                        });
                                    }
                                    progressBar.setVisibility(View.GONE);
                                }
                                else {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(getContext(),"No existen niveles registrados",Toast.LENGTH_SHORT).show();
                                    ClssConvertirTextoAVoz.clssConvertirTextoAVoz.reproduce("No existen niveles registrados");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                //Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            VolleyLog.e("Error: ", error.getMessage());
                            /*Toast.makeText(getContext(), "Error de conexión con el servidor\n Intente nuevamente", Toast.LENGTH_SHORT);
                            ClssConvertirTextoAVoz.getIntancia(getContext()).reproduce("Error de conexión con el servidor\nIntente nuevamente");*/
                            progressBar.setVisibility(View.GONE);
                        }
                    });
            //requestQueue.add(jsonArrayRequest);
            ClssVolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(jsonArrayRequest);
        } catch (Exception e) {}
    }

    private void addLevel(){
        fragment = new FrgAddLevel();
        ClssConvertirTextoAVoz.getIntancia(getContext()).reproduce("Agregar nivel");
        getFragmentManager().beginTransaction().replace(R.id.content, fragment).addToBackStack(null).commit();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adpLevel.filtrado(newText);
        return false;
    }
}