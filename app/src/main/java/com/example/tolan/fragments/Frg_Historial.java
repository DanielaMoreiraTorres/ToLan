package com.example.tolan.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.tolan.R;
import com.example.tolan.adapters.adpEstudent;
import com.example.tolan.adapters.adpHist_Estudent;
import com.example.tolan.adapters.adpHistorial;
import com.example.tolan.clases.ClssConvertirTextoAVoz;
import com.example.tolan.clases.ClssStaticGrupo;
import com.example.tolan.clases.ClssVolleySingleton;
import com.example.tolan.models.ModelEstudent;
import com.example.tolan.models.ModelGroup;
import com.example.tolan.models.ModelHistorial;
import com.example.tolan.models.ModelHistorial_Cabz;
import com.example.tolan.models.ModelUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Frg_Historial extends Fragment {

    private ProgressBar progressBar;
    private TextView titulo;
    private Toolbar toolbar;
    private ArrayList<ModelHistorial_Cabz>  modelHistorial;
    private RecyclerView rcvhist;
    private String url;

    public Frg_Historial() {
        // Required empty public constructor
    }

    public static Frg_Historial newInstance(String param1, String param2) {
        Frg_Historial fragment = new Frg_Historial();
        Bundle args = new Bundle();
        fragment.setArguments(args);
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
        return inflater.inflate(R.layout.fragment_historial, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            titulo = view.findViewById(R.id.lblTiTleH);
            titulo.setOnClickListener(v -> ClssConvertirTextoAVoz.getIntancia(v.getContext()).reproduce(titulo.getText().toString()));
            toolbar = view.findViewById(R.id.toolbar);
            setHasOptionsMenu(true);
            ((AppCompatActivity) this.getActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity) this.getActivity()).getSupportActionBar().setTitle("");
            url = getString(R.string.urlBase) + "historial";
            progressBar = view.findViewById(R.id.progressBar);
            rcvhist = (RecyclerView) view.findViewById(R.id.rcvHist_Estd);
            modelHistorial = new ArrayList<ModelHistorial_Cabz>();
            CargaData();
        } catch (Exception e) {}
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_toolbar, menu);
        MenuItem mc = menu.findItem(R.id.btnCaritas);
        mc.setVisible(false);
        MenuItem mr = menu.findItem(R.id.btnRecompensa);
        mr.setVisible(false);
    }

    public void CargaData() {
        //param.put("idEstudiante", ClssStaticGrupo.idestudiante);
        progressBar.setVisibility(View.VISIBLE);
        JsonArrayRequest request_json = new JsonArrayRequest(Request.Method.GET,url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            if (response.length() > 0) {
                                modelHistorial = parseJson(response);
                                LinearLayoutManager llm = new LinearLayoutManager(getContext());
                                llm.setOrientation(LinearLayoutManager.VERTICAL);
                                rcvhist.setLayoutManager(llm);
                                adpHistorial adapter = new adpHistorial(modelHistorial);
                                rcvhist.setAdapter(adapter);
                                progressBar.setVisibility(View.GONE);
                            }
                            else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getContext(),"No existe historial registrado",Toast.LENGTH_SHORT).show();
                                ClssConvertirTextoAVoz.clssConvertirTextoAVoz.reproduce("No existe historial registrado");
                            }
                        } catch (Exception e) {
                            //Toast.makeText(getContext(), "Error de conexión", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                Toast.makeText(getContext(), "Error de conexión con el servidor\n Intente nuevamente", Toast.LENGTH_SHORT);
                ClssConvertirTextoAVoz.getIntancia(getContext()).reproduce("Error de conexión con el servidor\nIntente nuevamente");
                progressBar.setVisibility(View.GONE);
            }
        });

        // Añadir petición a la cola
        ClssVolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(request_json);
    }
    public ArrayList<ModelHistorial_Cabz> parseJson(JSONArray jsonArray) {
        // Variables locales
        ArrayList<ModelHistorial_Cabz> Historial = new ArrayList();
        ModelHistorial_Cabz modelHis;
        try {
            // Obtener el array del objeto
            for (int i = 0; i < jsonArray.length(); i++) {
                List<ModelHistorial> actvList = new ArrayList<>();
                try {
                    JSONObject objeto = jsonArray.getJSONObject(i);
                    JSONArray act = objeto.getJSONArray("actividadesCompletas");
                    for (int j = 0; j < act.length(); j++) {
                        if (act.length() > 0) {
                            JSONObject hist_item = act.getJSONObject(j);
                            ModelHistorial mdht = new ModelHistorial(
                                    hist_item.getInt("id"),
                                    hist_item.getInt("idactividad"),
                                    hist_item.getString("nombre"),
                                    hist_item.getInt("recompensa"));
                            actvList.add(mdht);
                        }
                    }
                    modelHis = new ModelHistorial_Cabz(
                            objeto.getInt("idestudiante"),
                            objeto.getString("estudiante"),
                            actvList
                    );
                    Historial.add(modelHis);
                } catch (JSONException e) {
                    int e1 = Log.e("Resu", "Error de parsing: " + e.getMessage());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Historial;
    }

}