package com.example.tolan.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

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
import com.example.tolan.adapters.AdpRecycler_Main;
import com.example.tolan.clases.ClssVolleySingleton;
import com.example.tolan.models.ModelRecyclerItemActividad;
import com.example.tolan.models.ModelRecyclerItemNivel;
import com.example.tolan.models.ModelRecyclerItemSubnivel;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Frg_HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Frg_HomeFragment extends Fragment implements Response.Listener<JSONArray>, Response.ErrorListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Frg_HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InicioFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Frg_HomeFragment newInstance(String param1, String param2) {
        Frg_HomeFragment fragment = new Frg_HomeFragment();
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
        return inflater.inflate(R.layout.fragment_inicio, container, false);
    }


    ProgressBar progressBar;


    private static final String TAG = "MainActivity";


    private RecyclerView courseRV;


    //RecyclerView padre.- Contenedor de los items
    RecyclerView mainRecyclerView;
    List<ModelRecyclerItemNivel> sectionList;


    // RequestQueue request;
    JsonArrayRequest jsonArrayRequest;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //final NavController navController = Navigation.findNavController(view);

        /*
        view.findViewById(R.id.fragmentInicio).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //navController.navigate(R.id.action_inicioFragment_to_fragmentPrueba);
                navController.navigate(R.id.fragmentPrueba);

                //navController.getPreviousBackStackEntry().getDestination().getArguments().clear();
            }
        });
        */
        progressBar = view.findViewById(R.id.progressBar);
        sectionList = new ArrayList<>();
        mainRecyclerView = view.findViewById(R.id.mainRecyclerView);
        //mainRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //mainRecyclerView.setHasFixedSize(true);

        cargarWebService();
    }

    private void cargarWebService() {
        String url = "https://db-bartolucci.herokuapp.com/nivel";
        jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, this, this);
        // request.add(jsonObjectRequest);

        /*
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        jsonArrayRequest.setRetryPolicy(new RetryPolicy() { @Override public int getCurrentTimeout() { return 50000; } @Override public int getCurrentRetryCount() { return 50000; } @Override public void retry(VolleyError error) throws VolleyError { } });
*/
        ClssVolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(jsonArrayRequest);

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
                    "Oops. Parse Error! " + error.toString(),
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
        progressBar.setVisibility(View.GONE);
    }


    @Override
    public void onResponse(JSONArray response) {
        //nivelList= new ArrayList<>();

        try {
            for (int i = 0; i < response.length(); i++) {

                JSONObject nivel_item = response.getJSONObject(i);
                //nivelList.add(new Nivel(nivel_item.get("id")));
                List<ModelRecyclerItemSubnivel> section = new ArrayList<>();
                ///Construir arrayList de Subniveles
                JSONArray subniveles = nivel_item.getJSONArray("subnivel");
                //List<JSONArray> lst_subniveles = new ArrayList<>();
                List<ModelRecyclerItemActividad> modelRecyclerItemActividades = new ArrayList<>();
                for (int j = 0; j < subniveles.length(); j++) {
                    JSONObject subnivel_item = subniveles.getJSONObject(j);
                    //System.out.println("-------------------------------------");
                    JSONArray item_subnivel = (JSONArray) subnivel_item.get("actividad");

                    /*
                    if (item_subnivel.length() > 0) {
                        //lst_subniveles.add(item_subnivel);
                        for (int k = 0; k < item_subnivel.length(); k++) {
                            JSONObject element = item_subnivel.getJSONObject(k);
                            modelRecyclerItemActividades.add(new ModelRecyclerItemActividad(element.getInt("id"), element.getString("nombre")));
                        }
                    }*/

                    //System.out.println("-------------------------------------");

                    section.add(new ModelRecyclerItemSubnivel(subnivel_item.get("nombre").toString(),
                            subnivel_item.get("url").toString(), subnivel_item.getInt("id")));
                    //modelRecyclerItemActividades = new ArrayList<>();
                }

                // System.out.println(lst_subniveles);

                sectionList.add(new ModelRecyclerItemNivel(nivel_item.get("nombre").toString(),
                        nivel_item.get("url").toString(),
                        section));
                // System.out.println("-------------------------------------");

            }

            //mainRecyclerView = findViewById(R.id.mainRecyclerView);
            AdpRecycler_Main mainRecyclerAdapter = new AdpRecycler_Main(getContext(), sectionList);
            mainRecyclerView.setAdapter(mainRecyclerAdapter);

            //Añade una linea divisora
            //mainRecyclerView.addItemDecoration(new DividerItemDecoration(MainActivity.this, DividerItemDecoration.VERTICAL));


            progressBar.setVisibility(View.GONE);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


}