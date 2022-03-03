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
import android.view.View;
import android.view.ViewGroup;
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
import com.example.tolan.clases.ClssConvertirTextoAVoz;
import com.example.tolan.clases.ClssStaticGrupo;
import com.example.tolan.clases.ClssVolleySingleton;
import com.example.tolan.models.ModelEstudent;
import com.example.tolan.models.ModelHistorial;
import com.example.tolan.models.ModelUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Frg_Historial#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Frg_Historial extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Frg_Historial() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Frg_Historial.
     */
    // TODO: Rename and change types and number of parameters
    public static Frg_Historial newInstance(String param1, String param2) {
        Frg_Historial fragment = new Frg_Historial();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_historial, container, false);
    }

    private TextView titulo;
    private Toolbar toolbar;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        titulo = view.findViewById(R.id.titulo);
        //titulo.setOnClickListener(v -> ClssConvertirTextoAVoz.getIntancia(v.getContext()).reproduce(titulo.getText().toString()));
        toolbar = view.findViewById(R.id.toolbar);
        setHasOptionsMenu(true);
        ((AppCompatActivity) this.getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) this.getActivity()).getSupportActionBar().setTitle("");
        CargaData(view);
    }

    ArrayList<ModelHistorial>  modelHistorial;
    RecyclerView rcvhist;
    public void CargaData(View v) {
        rcvhist = (RecyclerView) v.findViewById(R.id.rcvHist_Estd);
        modelHistorial = new ArrayList<ModelHistorial>();
        String url = "https://db-bartolucci.herokuapp.com/historial";
        JSONObject param = new JSONObject();
        //param.put("idEstudiante", ClssStaticGrupo.idestudiante);
        JsonArrayRequest request_json = new JsonArrayRequest(Request.Method.GET,url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            if (response.length() > 1) {
                                modelHistorial = parseJson(response);
                                LinearLayoutManager llm = new LinearLayoutManager(getContext());
                                llm.setOrientation(LinearLayoutManager.VERTICAL);
                                rcvhist.setLayoutManager(llm);
                                adpHist_Estudent adapter = new adpHist_Estudent(getContext(),modelHistorial);
                                rcvhist.setAdapter(adapter);
                            } else
                                Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            //Toast.makeText(getContext(), "Error de conexión", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                ClssConvertirTextoAVoz.getIntancia(v.getContext()).reproduce("Error de conexión con el servidor");
            }
        });

        // Añadir petición a la cola
        ClssVolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(request_json);
    }
    public ArrayList<ModelHistorial> parseJson(JSONArray jsonArray) {
        // Variables locales
        ArrayList<ModelHistorial> Hist = new ArrayList();
        ModelHistorial mdh;
        try {
            // Obtener el array del objeto
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject objeto = jsonArray.getJSONObject(i);
                    JSONArray act = objeto.getJSONArray("actividadesCompletas");
                    for (int j = 0; j < act.length(); j++) {
                        if (act.length() > 0) {
                            JSONObject hist_item = act.getJSONObject(j);
                            mdh = new ModelHistorial(
                                    hist_item.getInt("id"),
                                    hist_item.getInt("idactividad"),
                                    hist_item.getString("nombre"),
                                    hist_item.getInt("recompensa"));
                            Hist.add(mdh);
                        }
                    }
                } catch (JSONException e) {
                    int e1 = Log.e("Resu", "Error de parsing: " + e.getMessage());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Hist;
    }

}