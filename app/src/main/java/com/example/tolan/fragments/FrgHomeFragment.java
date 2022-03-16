package com.example.tolan.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
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
import com.example.tolan.models.ModelRecyclerItemNivel;
import com.example.tolan.models.ModelRecyclerItemSubnivel;
import com.example.tolan.models.ModelUser;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FrgHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FrgHomeFragment extends Fragment implements Response.Listener<JSONArray>, Response.ErrorListener {

    private Toolbar toolbar;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FrgHomeFragment() {
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
    public static FrgHomeFragment newInstance(String param1, String param2) {
        FrgHomeFragment fragment = new FrgHomeFragment();
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
        //textToSpeech = new TextToSpeech(getContext(),i -> reproducirAudio(i, "Bienvenido "+ ClssStaticGroup.estudiante));
        return inflater.inflate(R.layout.fragment_inicio, container, false);
    }

    MenuItem mr;

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_toolbar, menu);
        mr = menu.findItem(R.id.btnRecompensa);
        mr.setTitle(String.valueOf(ModelUser.stockcaritas));
    }

    ProgressBar progressBar;


    private static final String TAG = "MainActivity";


    private RecyclerView courseRV;


    //RecyclerView padre.- Contenedor de los items
    RecyclerView mainRecyclerView;
    List<ModelRecyclerItemNivel> all_niveles;

    List<ModelRecyclerItemSubnivel> all_subniveles = new ArrayList<>();


    // RequestQueue request;
    JsonArrayRequest jsonArrayRequest;

    NavController navController;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar = view.findViewById(R.id.toolbar);
        setHasOptionsMenu(true);
        ((AppCompatActivity) this.getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) this.getActivity()).getSupportActionBar().setTitle("");


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
        all_niveles = new ArrayList<>();
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

                //Obtengo mi item de nivel del response
                JSONObject nivel_item = response.getJSONObject(i);

                //Necesito  inicializar el objeto para parsear la información
                all_subniveles = new ArrayList<>();

                ///Cargar JSONArray de Subniveles de cada nivel
                JSONArray subniveles = nivel_item.getJSONArray("subniveles");

                for (int j = 0; j < subniveles.length(); j++) {
                    JSONObject subnivel_item = subniveles.getJSONObject(j);
                    /* Parsear las actividades
                    JSONArray item_subnivel = (JSONArray) subnivel_item.get("actividad");
                    if (item_subnivel.length() > 0) {
                        //lst_subniveles.add(item_subnivel);
                        for (int k = 0; k < item_subnivel.length(); k++) {
                            JSONObject element = item_subnivel.getJSONObject(k);
                            modelRecyclerItemActividades.add(new ModelRecyclerItemActividad(element.getInt("id"), element.getString("nombre")));
                        }
                    }*/
                    all_subniveles.add(crearSubnivel(
                            subnivel_item.get("nombre").toString(),
                            subnivel_item.get("url").toString(),
                            subnivel_item.getInt("id")));
                    if (j == subniveles.length() - 1) {
                        //Toast.makeText(getContext(), "Ultimo subnivel", Toast.LENGTH_SHORT).show();
                        all_subniveles.add(crearSubnivel("Evaluación", "https://res.cloudinary.com/ddgl3cxau/image/upload/v1646930311/fqqoocpuleggxxrgsepn.png", 0));
                    }
                }

                all_niveles.add(crearNivel(
                        nivel_item.get("nombre").toString(),
                        nivel_item.get("url").toString(),
                        all_subniveles, nivel_item.getInt("id")));
                // System.out.println("-------------------------------------");

            }

            //mainRecyclerView = findViewById(R.id.mainRecyclerView);
            AdpRecycler_Main mainRecyclerAdapter = new AdpRecycler_Main(getContext(), all_niveles);
            mainRecyclerView.setAdapter(mainRecyclerAdapter);

            //Añade una linea divisora al recyclerView
            //mainRecyclerView.addItemDecoration(new DividerItemDecoration(MainActivity.this, DividerItemDecoration.VERTICAL));


            progressBar.setVisibility(View.GONE);
            //Actualice el stock visualmente
            //mr.setTitle(String.valueOf(ModelUser.stockcaritas));


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public ModelRecyclerItemSubnivel crearSubnivel(String titulo, String image, int id) {
        return new ModelRecyclerItemSubnivel(titulo, image, id);
    }

    public ModelRecyclerItemNivel crearNivel(String sectionName, String image, List<ModelRecyclerItemSubnivel> all_subniveles, int idNivel) {
        return new ModelRecyclerItemNivel(sectionName, image, all_subniveles, idNivel);
    }


}