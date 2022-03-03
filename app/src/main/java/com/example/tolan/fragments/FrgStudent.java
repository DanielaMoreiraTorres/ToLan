package com.example.tolan.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.tolan.ActivityEstudents;
import com.example.tolan.R;
import com.example.tolan.adapters.adpEstudent;
import com.example.tolan.clases.ClssConvertirTextoAVoz;
import com.example.tolan.clases.ClssStaticGrupo;
import com.example.tolan.clases.ClssVolleySingleton;
import com.example.tolan.models.ModelEstudent;
import com.example.tolan.models.ModelLevel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FrgStudent extends Fragment {

    private Toolbar toolbar;
    private Fragment fragment;
    private ProgressBar progressBar;
    private Bundle bundle;
    private SearchView searchView;
    private TextView lblTiTleE;
    private String url;
    private Integer in;
    private JsonObjectRequest jsArrayRequest;
    private RecyclerView estudianteRcl;
    private ArrayList<ModelEstudent> lstEstudents;

    public FrgStudent() {
        // Required empty public constructor
    }

    public static FrgStudent newInstance(String param1, String param2) {
        FrgStudent fragment = new FrgStudent();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            in = (Integer) getArguments().getSerializable("GrupoIDDocent");
        }
        else
            in = ClssStaticGrupo.iddocente;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_student, container, false);
        try {
            toolbar = view.findViewById(R.id.toolbar);
            setHasOptionsMenu(true);
            ((AppCompatActivity) this.getActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity) this.getActivity()).getSupportActionBar().setTitle("");
            url = getString(R.string.urlBase) + "grupo/byDocente?idDocente=";
            progressBar = view.findViewById(R.id.progressBar);
            lblTiTleE = view.findViewById(R.id.lblTiTleE);
            if(ClssStaticGrupo.iddocente != 0)
                lblTiTleE.setText("Mis estudiantes");
            lblTiTleE.setOnClickListener(v -> ClssConvertirTextoAVoz.getIntancia(v.getContext()).reproduce(lblTiTleE.getText().toString().trim()));
            estudianteRcl = (RecyclerView) view.findViewById(R.id.rcvEstudiantes);
            estudianteRcl.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            lstEstudents = new ArrayList<ModelEstudent>();
            getStudentByDocente();
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

    private void getStudentByDocente(){
        try {
            progressBar.setVisibility(View.VISIBLE);
            url = url + in;
            int e5 = Log.e("dataf", url);
            jsArrayRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            int e5 = Log.e("dataf", response.toString());
                            lstEstudents = parseJson(response);
                            if(lstEstudents.size() > 0) {
                                if (getContext() != null) {
                                    adpEstudent adapter = new adpEstudent(lstEstudents);
                                    estudianteRcl.setAdapter(adapter);
                                    adapter.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            int opcselec = estudianteRcl.getChildAdapterPosition(view);
                                            ClssConvertirTextoAVoz.getIntancia(getContext()).reproduce(lstEstudents.get(opcselec).getEstudiante());
                                        }
                                    });
                                }
                                progressBar.setVisibility(View.GONE);
                            }
                            else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getContext(),"No existen estudiantes asignados al docente",Toast.LENGTH_SHORT).show();
                                ClssConvertirTextoAVoz.clssConvertirTextoAVoz.reproduce("No existen estudiantes asignados al docente");
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            VolleyLog.e("Error: ", volleyError.getMessage());
                            Toast.makeText(getContext(), "No se ha podido establecer conexión con el servidor\nIntente nuevamente",Toast.LENGTH_LONG).show();
                            ClssConvertirTextoAVoz.getIntancia(getContext()).reproduce("No se ha podido establecer conexión con el servidor\nIntente nuevamente");
                            progressBar.setVisibility(View.GONE);
                        }
                    });
            ClssVolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(jsArrayRequest);
        } catch (Exception e) {}
    }

    private ArrayList<ModelEstudent> parseJson(JSONObject jsonArray) {
        // Variables locales
        ArrayList<ModelEstudent> Estudents = new ArrayList();
        try {
            // Obtener el array del objeto
            try {
                JSONArray estud = jsonArray.getJSONArray("estudiantes");
                for (int j=0; j < estud.length();j++)
                {
                    if(estud.length() > 0) {
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