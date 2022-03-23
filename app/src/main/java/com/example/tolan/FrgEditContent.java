package com.example.tolan;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

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
import com.example.tolan.clases.ClssVolleySingleton;
import com.example.tolan.fragments.FrgActividad;
import com.example.tolan.fragments.FrgContent;
import com.example.tolan.models.ModelActivity;
import com.example.tolan.models.ModelContenido;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class FrgEditContent extends Fragment implements View.OnClickListener {
    private Toolbar toolbar;

    ModelContenido modelContenido;

    public FrgEditContent(ArrayList<ModelActivity> lst_activities, ModelContenido modelContenido, ModelActivity modelActivity) {
        this.lst_activities = lst_activities;
        this.modelContenido = modelContenido;
        this.modelActivitySelected = modelActivity;
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_content, container, false);
    }


    AutoCompleteTextView act_lstactividades;
    EditText edt_idContenido, edt_descripcion_contenido;
    CheckBox chkEnunciado, chkRespuesta;
    ModelActivity modelActivitySelected;
    Button btn_actualizar_contenidos;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar = view.findViewById(R.id.toolbar);
        setHasOptionsMenu(true);
        ((AppCompatActivity) this.getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) this.getActivity()).getSupportActionBar().setTitle("");

        act_lstactividades = view.findViewById(R.id.lst_actividades);
        edt_idContenido = view.findViewById(R.id.idContenido);
        edt_descripcion_contenido = view.findViewById(R.id.descripcion_contenido);
        chkEnunciado = (CheckBox) view.findViewById(R.id.chkEnunciado);
        chkRespuesta = (CheckBox) view.findViewById(R.id.chkRespuesta);
        btn_actualizar_contenidos=view.findViewById(R.id.btn_actualizar_contenidos);
        btn_actualizar_contenidos.setOnClickListener(this);

        edt_idContenido.setText(modelContenido.getIdContenido().toString());
        edt_descripcion_contenido.setText(modelContenido.getDescripcion());
        chkEnunciado.setChecked(modelContenido.isEnunciado());
        chkRespuesta.setChecked(modelContenido.isRespuesta());


        loadActividades();
        act_lstactividades.setText(modelActivitySelected.getNombre(), false);
        act_lstactividades.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                modelActivitySelected = lst_activities.get(position);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_toolbar, menu);
        MenuItem mc = menu.findItem(R.id.btnCaritas);
        mc.setVisible(false);
        MenuItem mr = menu.findItem(R.id.btnRecompensa);
        mr.setVisible(false);
        MenuItem mi = menu.findItem(R.id.btnMyInfo);
        mi.setVisible(false);
    }


    JsonArrayRequest jsonArrayRequest;
    private ArrayList<ModelActivity> lst_activities;
    ArrayAdapter arrayAdapterActivities;

    public void loadActividades() {
        ArrayList activitiesName = new ArrayList<>();
        for (ModelActivity modelActivity : lst_activities) {
            activitiesName.add(modelActivity.getNombre());
        }


        arrayAdapterActivities = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item
                , activitiesName);
        act_lstactividades.setAdapter(arrayAdapterActivities);
        act_lstactividades.setThreshold(1);

    }


    private JsonObjectRequest jsonObjectRequest;

    public void updateContenido(String urlS, JSONObject param) {
        jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, urlS, param,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.length() > 1) {
                                Toast.makeText(getContext(), "Contenido actualizado exitosamente", Toast.LENGTH_SHORT).show();
                                redirectContenido();
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

    Fragment fragment;

    public void redirectContenido() {
        fragment = new FrgContent();
        getFragmentManager().beginTransaction().replace(R.id.content, fragment).addToBackStack(null).commit();
    }

    @Override
    public void onClick(View v) {
        JSONObject param = new JSONObject();
        try {
            //Parámetros a enviar a la API
            param.put("id", edt_idContenido.getText().toString());
            param.put("idActividad", modelActivitySelected.getId());
            param.put("descripcion", edt_descripcion_contenido.getText().toString());
            param.put("isEnunciado", chkEnunciado.isChecked());
            param.put("isRespuesta", chkRespuesta.isChecked());
            param.put("activo", true);

        } catch (JSONException ex) {
            Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        updateContenido(getString(R.string.urlBase) + "contenido", param);
    }
}