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
import com.example.tolan.adapters.AdpHistorial;
import com.example.tolan.clases.ClssConvertTextToSpeech;
import com.example.tolan.clases.ClssStaticGroup;
import com.example.tolan.clases.ClssVolleySingleton;
import com.example.tolan.models.ModelHistorial;
import com.example.tolan.models.ModelHistorial_Cabz;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FrgHistorial extends Fragment {

    private ProgressBar progressBar;
    private TextView titulo;
    private Toolbar toolbar;
    private ArrayList<ModelHistorial_Cabz>  modelHistorial;
    private RecyclerView rcvhist;
    private String url;

    public FrgHistorial() {
        // Required empty public constructor
    }

    public static FrgHistorial newInstance(String param1, String param2) {
        FrgHistorial fragment = new FrgHistorial();
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
            titulo.setOnClickListener(v -> ClssConvertTextToSpeech.getIntancia(v.getContext()).reproduce(titulo.getText().toString()));
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
        Integer i = ClssStaticGroup.iddocente;
        JsonArrayRequest request_json;
        if(i>0)
        {
            url+="/byDocente?idDocente="+i.toString();
        }
        progressBar.setVisibility(View.VISIBLE);
            request_json = new JsonArrayRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            try {
                                if (response.length() >= 1) {
                                    if(getContext() != null) {
                                        modelHistorial = parseJson(response);
                                        LinearLayoutManager llm = new LinearLayoutManager(getContext());
                                        llm.setOrientation(LinearLayoutManager.VERTICAL);
                                        rcvhist.setLayoutManager(llm);
                                        AdpHistorial adapter = new AdpHistorial(modelHistorial);
                                        rcvhist.setAdapter(adapter);
                                    }
                                    progressBar.setVisibility(View.GONE);
                                } else {
                                    progressBar.setVisibility(View.GONE);
                                    //Toast.makeText(getContext(), "No existe historial registrado", Toast.LENGTH_SHORT).show();
                                    //ClssConvertTextToSpeech.clssConvertirTextoAVoz.reproduce("No existe historial registrado");
                                }
                            } catch (Exception e) {
                                //Toast.makeText(getContext(), "Error de conexi??n", Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.e("Error: ", error.getMessage());
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(getContext(), response.getString("message").trim(), Toast.LENGTH_SHORT);
                                        ClssConvertTextToSpeech.getIntancia(getContext()).reproduce(response.getString("message"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getContext(), "Error de conexi??n con el servidor\n Intente nuevamente", Toast.LENGTH_SHORT);
                                    ClssConvertTextToSpeech.getIntancia(getContext()).reproduce("Error de conexi??n con el servidor\nIntente nuevamente");
                                    progressBar.setVisibility(View.GONE);
                                }
                            });
                    ClssVolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(request);
                }
            });
            // A??adir petici??n a la cola
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
                                    hist_item.getString("descripcion"),
                                    hist_item.getString("fecha"),
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