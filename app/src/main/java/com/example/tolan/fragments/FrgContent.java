package com.example.tolan.fragments;

import android.annotation.SuppressLint;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.tolan.R;
import com.example.tolan.clases.ClssVolleySingleton;
import com.example.tolan.dialogs.Diag_Frg_CargaMultimedia;
import com.example.tolan.models.ModelActivity;
import com.example.tolan.models.ModelContenido;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class FrgContent extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_content, container, false);
    }

    private Toolbar toolbar;
    AutoCompleteTextView act_lstactividades;
    ModelActivity modelActivitySelected;

    ArrayList<ModelContenido> lstContenidos;
    RecyclerView rv_ContenidosRegistrados;
    AdaptadorContenido adaptadorContenido;
    Button btn_añadir;
    EditText descripcion_contenido;
    CheckBox chkIsEnunciado, chkIsRespuesta;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar = view.findViewById(R.id.toolbar);
        setHasOptionsMenu(true);
        ((AppCompatActivity) this.getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) this.getActivity()).getSupportActionBar().setTitle("");

        act_lstactividades = view.findViewById(R.id.act_lstactividades);
        loadActividades("https://db-bartolucci.herokuapp.com/actividad");

        act_lstactividades.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                modelActivitySelected = lst_activities.get(position);
                //Toast.makeText(getContext(), modelActivitySelected.getId() + modelActivitySelected.getDescripcion() + modelActivitySelected.getNombre(), Toast.LENGTH_LONG).show();
            }
        });

        rv_ContenidosRegistrados = view.findViewById(R.id.rv_ContenidosRegistrados);
        lstContenidos = new ArrayList<>();
        LinearLayoutManager l = new LinearLayoutManager(getContext());
        rv_ContenidosRegistrados.setLayoutManager(l);
        adaptadorContenido = new AdaptadorContenido();
        rv_ContenidosRegistrados.setAdapter(adaptadorContenido);

        descripcion_contenido = view.findViewById(R.id.descripcion_contenido);
        chkIsEnunciado = view.findViewById(R.id.chkIsEnunciado);
        chkIsRespuesta = view.findViewById(R.id.chkIsRespuesta);


        btn_añadir = view.findViewById(R.id.btn_añadir);
        btn_añadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (modelActivitySelected != null && descripcion_contenido.getText().length() > 0) {
                    JSONObject param = new JSONObject();
                    try {
                        //Parámetros a enviar a la API
                        param.put("idActividad", modelActivitySelected.getId());
                        param.put("descripcion", descripcion_contenido.getText().toString());
                        param.put("isEnunciado", chkIsEnunciado.isChecked());
                        param.put("isRespuesta", chkIsRespuesta.isChecked());
                    } catch (JSONException ex) {
                        Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    registerContenido("https://db-bartolucci.herokuapp.com/contenido", param);
                } else {
                    Toast.makeText(getContext(), "Complete toda la información correspondiente", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    // RequestQueue request;
    JsonArrayRequest jsonArrayRequest;
    private ArrayList<ModelActivity> lst_activities;
    ArrayAdapter arrayAdapterActivities;

    public void loadActividades(String urlR) {
        lst_activities = new ArrayList<>();
        jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlR, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ArrayList activitiesName = new ArrayList<>();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject activity_item = response.getJSONObject(i);
                                lst_activities.add(new ModelActivity(
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

                                activitiesName.add(activity_item.getString("nombre"));
                            }

                            if (getContext() != null) {
                                arrayAdapterActivities = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item
                                        , activitiesName);
                                act_lstactividades.setAdapter(arrayAdapterActivities);
                                act_lstactividades.setThreshold(1);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
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

                        //ClssVolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(request_json);
                    }
                });
        ClssVolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(jsonArrayRequest);
    }

    private JsonObjectRequest jsonObjectRequest;

    public void registerContenido(String urlS, JSONObject param) {
        jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, urlS, param,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.length() > 1) {
                                Toast.makeText(getContext(), "Contenido registrado exitosamente", Toast.LENGTH_SHORT).show();

                                ModelContenido modelContenido = new ModelContenido(response.getInt("id"),
                                        response.getString("descripcion"), response.getBoolean("enunciado"), response.getBoolean("respuesta"));
                                lstContenidos.add(modelContenido);
                                adaptadorContenido.notifyDataSetChanged();
                                limpiar();

                                //redirectActividades();
                            } else
                                Toast.makeText(getContext(), response.get("message").toString(), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            //Toast.makeText(getContext(),"Error de conexión",Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
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
            }
        });
        ClssVolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(jsonObjectRequest);
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_toolbar, menu);
        MenuItem mc = menu.findItem(R.id.btnCaritas);
        mc.setVisible(false);
        MenuItem mr = menu.findItem(R.id.btnRecompensa);
        mr.setVisible(false);
    }

    private class AdaptadorContenido extends RecyclerView.Adapter<AdaptadorContenido.AdaptadorContenidoHolder> {

        @NonNull
        @Override
        public AdaptadorContenidoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new AdaptadorContenidoHolder(getLayoutInflater().inflate(R.layout.item_contenido, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull AdaptadorContenidoHolder holder, @SuppressLint("RecyclerView") int position) {
            holder.imprimir(position);
            holder.fabtnAñadirMultimedia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Diag_Frg_CargaMultimedia diag_frg_cargaMultimedia = new Diag_Frg_CargaMultimedia(lstContenidos.get(position).getIdContenido().toString());
                    diag_frg_cargaMultimedia.show(getParentFragmentManager(), "Registro de contenido multimedia");
                }
            });
        }

        @Override
        public int getItemCount() {
            return lstContenidos.size();
        }

        class AdaptadorContenidoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            CheckBox chkEnunciado, chkRespuesta;
            TextView txt_DescripctionActividad, txt_IdActividad;
            FloatingActionButton fabtnAñadirMultimedia;

            public AdaptadorContenidoHolder(@NonNull View itemView) {
                super(itemView);

                chkEnunciado = (CheckBox) itemView.findViewById(R.id.chkEnunciado);
                chkRespuesta = (CheckBox) itemView.findViewById(R.id.chkRespuesta);
                txt_DescripctionActividad = itemView.findViewById(R.id.txt_DescripctionActividad);
                txt_IdActividad = itemView.findViewById(R.id.txt_IdActividad);
                fabtnAñadirMultimedia = itemView.findViewById(R.id.fabtnAñadirMultimedia);
                itemView.setOnClickListener(this);
            }

            public void imprimir(int position) {
                chkEnunciado.setChecked(lstContenidos.get(position).isEnunciado());
                chkRespuesta.setChecked(lstContenidos.get(position).isRespuesta());
                txt_DescripctionActividad.setText(lstContenidos.get(position).getDescripcion());
                txt_IdActividad.setText(lstContenidos.get(position).getIdContenido().toString());
            }

            @Override
            public void onClick(View v) {
                //mostrar(getLayoutPosition());
            }
        }
    }

    public void limpiar() {
        //modelActivitySelected = null;
        descripcion_contenido.setText("");
        chkIsEnunciado.setChecked(false);
        chkIsRespuesta.setChecked(false);
        descripcion_contenido.setFocusable(true);


    }
}