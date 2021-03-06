package com.example.tolan.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.tolan.R;
import com.example.tolan.adapters.AdpActividad;
import com.example.tolan.clases.ClssConvertTextToSpeech;
import com.example.tolan.clases.ClssVolleySingleton;
import com.example.tolan.models.ModelActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FrgActividad#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FrgActividad extends Fragment implements Response.Listener<JSONArray>, Response.ErrorListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FrgActividad() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FrgActividad.
     */
    // TODO: Rename and change types and number of parameters
    public static FrgActividad newInstance(String param1, String param2) {
        FrgActividad fragment = new FrgActividad();
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
        return inflater.inflate(R.layout.fragment_actividades, container, false);
    }


    private AdpActividad adapter;
    private ArrayList<ModelActivity> courseModalArrayList;
    //ProgressBar progressBar;
    FloatingActionButton fabtn_agregarActividad;
    private Toolbar toolbar;


    private RecyclerView courseRV;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar = view.findViewById(R.id.toolbar);
        setHasOptionsMenu(true);
        ((AppCompatActivity) this.getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) this.getActivity()).getSupportActionBar().setTitle("");


        fabtn_agregarActividad = (FloatingActionButton) view.findViewById(R.id.fabtn_agregarActividad);
        fabtn_agregarActividad.setOnClickListener(this::onClick);

        // initializing our variables.
        courseRV = view.findViewById(R.id.RecyclerViewActividades);

        // calling method to
        // build recycler view.
        cargarWebService();
    }

    // RequestQueue request;
    JsonArrayRequest jsonArrayRequest;

    private void cargarWebService() {
        String url = getString(R.string.urlBase) + "actividad";
        jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, this, this);
        ClssVolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(jsonArrayRequest);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_toolbar, menu);
        MenuItem mc = menu.findItem(R.id.btnCaritas);
        mc.setVisible(false);
        MenuItem mr = menu.findItem(R.id.btnRecompensa);
        mr.setVisible(false);
    }

    private Fragment fragment;

    public void onClick(View v) {
        fragment = new FrgAddActividad("add");
        getFragmentManager().beginTransaction().replace(R.id.content, fragment).addToBackStack(null).commit();

        //Intent intent = new Intent(getApplicationContext(), RegistrarActividades_Activity.class);
        //startActivity(intent);

    }

    @Override
    public void onErrorResponse(VolleyError error) {

        if (error instanceof NetworkError) {
            Toast.makeText(getContext(),
                    "Oops. Network Error! " + error.toString(),
                    Toast.LENGTH_LONG).show();
        } else if (error instanceof ServerError) {
            Toast.makeText(getContext(),
                    "Oops. Server Error! " + error.toString(),
                    Toast.LENGTH_LONG).show();
        } else if (error instanceof AuthFailureError) {
            Toast.makeText(getContext(),
                    "Oops. Auth FailureError! " + error.toString(),
                    Toast.LENGTH_LONG).show();
        } else if (error instanceof ParseError) {
            Toast.makeText(getContext(),
                    "Oops. Parse Error! " + error.getMessage(),
                    Toast.LENGTH_LONG).show();
        } else if (error instanceof NoConnectionError) {
            Toast.makeText(getContext(),
                    "Oops. NoConnection Error! " + error.toString(),
                    Toast.LENGTH_LONG).show();
        } else if (error instanceof TimeoutError) {
            Toast.makeText(getContext(),
                    "Oops. Timeout error! " + error.toString(),
                    Toast.LENGTH_LONG).show();
        } else {

            Toast.makeText(getContext(),
                    "No se puede conectar " + error.toString(), Toast.LENGTH_LONG).show();
            //System.out.println();

            // Log.d("ERROR: ", error.toString());
        }

        ClssVolleySingleton.getIntanciaVolley(getContext()).getRequestQueue().stop();

        //progressBar.setVisibility(View.GONE);
    }


    @Override
    public void onResponse(JSONArray response) {
        courseModalArrayList = new ArrayList<>();
        try {

            if (response.length() > 0) {
                for (int i = 0; i < response.length(); i++) {
                    //Obtengo mi item de actividad del response
                    JSONObject activity_item = response.getJSONObject(i);
                    courseModalArrayList.add(new ModelActivity(
                            activity_item.getInt("id"),
                            activity_item.getInt("idSubnivel"),
                            activity_item.getString("subnivel"),
                            activity_item.getInt("idDocente"),
                            activity_item.getString("docente"),
                            activity_item.getString("nombre"),
                            activity_item.getString("descripcion"),
                            activity_item.getInt("recompensavalor"),
                            activity_item.getString("tipo"),
                            activity_item.getBoolean("activo")));
                }

                adapter = new AdpActividad(courseModalArrayList, this);
                LinearLayoutManager manager = new LinearLayoutManager(getContext());
                courseRV.setHasFixedSize(true);
                courseRV.setLayoutManager(manager);
                courseRV.setAdapter(adapter);
            } else {
                Toast.makeText(getContext(), "No hay registros de actividades ", Toast.LENGTH_LONG).show();
                ClssConvertTextToSpeech.getIntancia(getContext()).reproduce("No hay registros de actividades");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}