package com.example.tolan.fragments;

import android.content.Intent;
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
import com.example.tolan.models.ModelActivity;
import com.example.tolan.models.ModelTeacher;
import com.example.tolan.models.ModelSublevel;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class FrgAddActividad extends Fragment implements View.OnClickListener {


    String tipo_accion;
    ModelActivity modelActivity;

    public FrgAddActividad(String tipo_accion, ModelActivity modelActivity) {
        this.tipo_accion = tipo_accion;
        this.modelActivity = modelActivity;
    }

    public FrgAddActividad(String tipo_accion) {
        this.tipo_accion = tipo_accion;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_actividad, container, false);
    }


    private Toolbar toolbar;
    private AutoCompleteTextView act_dificultad;
    private AutoCompleteTextView act_tipo;
    //FloatingActionButton fabtn_addpicture;
    //ImageView img_actividad;

    private EditText idActividad, subnivel, docente, descripcion_actividad;
    TextInputLayout til_idActividad, til_tipo;
    //til_subnivel, til_docente;
    AutoCompleteTextView act_lstsubnivel, act_lstdocente, nombre_actividad;
    Button btn_actualizar_actividades;
    TextView txt_TituloFrg;

    ModelSublevel modelSublevelSelected;
    ModelTeacher modelDocenteSelected;
    String dificultadSelected, tipoSelected, actividad_selected;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar = view.findViewById(R.id.toolbar);
        setHasOptionsMenu(true);
        ((AppCompatActivity) this.getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) this.getActivity()).getSupportActionBar().setTitle("");

        //fabtn_addpicture = view.findViewById(R.id.fabtn_addpicture);
        //img_actividad = view.findViewById(R.id.img_actividad);
        //fabtn_addpicture.setOnClickListener(this::loadImage);
        idActividad = view.findViewById(R.id.idActividad);
        btn_actualizar_actividades = view.findViewById(R.id.btn_actualizar_actividades);
        btn_actualizar_actividades.setOnClickListener(this);
        txt_TituloFrg = view.findViewById(R.id.txt_TituloFrg);
        til_tipo = view.findViewById(R.id.til_tipo);


        descripcion_actividad = view.findViewById(R.id.descripcion_actividad);


        //subnivel= view.findViewById(R.id.subnivel);


        //docente=view.findViewById(R.id.docente);
        til_idActividad = view.findViewById(R.id.til_idActividad);
        //til_subnivel = view.findViewById(R.id.til_subnivel);
        //til_docente = view.findViewById(R.id.til_docente);
        act_lstsubnivel = view.findViewById(R.id.act_lstsubnivel);
        act_lstdocente = view.findViewById(R.id.act_lstdocente);


        fill_items_list(view);

        loadSubniveles(getString(R.string.urlBase) + "subnivel");
        act_tipo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override

            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                tipoSelected = (String) arrayAdapterTipo.getItem(position);
                //Toast.makeText(getContext(), tipoSelected, Toast.LENGTH_LONG).show();
            }
        });
        act_dificultad.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                dificultadSelected = (String) arrayAdapterDificultad.getItem(position);
                //Toast.makeText(getContext(), dificultadSelected, Toast.LENGTH_LONG).show();
            }
        });
        //loadSubniveles(getString(R.string.urlBase) + "subnivel");

        act_lstsubnivel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                modelSublevelSelected = sublevels.get(position);
                //Toast.makeText(getContext(), modelSublevelSelected.getId() + modelSublevelSelected.getDescripcion() + modelSublevelSelected.getNombre(), Toast.LENGTH_LONG).show();
            }
        });

        act_lstdocente.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                modelDocenteSelected = docentes.get(position);
                //Toast.makeText(getContext(), modelDocenteSelected.getId() + modelDocenteSelected.getNombres() + modelDocenteSelected.getApellidos(), Toast.LENGTH_LONG).show();


                ////////Borrar

                //getIdDocente("https://db-bartolucci.herokuapp.com/usuario/" + modelDocenteSelected.getId());

                /////Borrar
            }
        });

        nombre_actividad.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                actividad_selected = (String) arrayAdapter_nombreactividad.getItem(position);
                //Toast.makeText(getContext(), modelSublevelSelected.getId() + modelSublevelSelected.getDescripcion() + modelSublevelSelected.getNombre(), Toast.LENGTH_LONG).show();
            }
        });


        switch (tipo_accion) {
            case "add":
                btn_actualizar_actividades.setText("Guardar");
                txt_TituloFrg.setText("Añadir nuevo");
                loadDocentes(getString(R.string.urlBase) + "usuario/byTipo?tipo=DC");
                til_idActividad.setVisibility(View.GONE);
                //til_subnivel.setVisibility(View.GONE);
                //til_docente.setVisibility(View.GONE);

                break;

            case "edit":
                nombre_actividad.setText(modelActivity.getNombre(), false);
                actividad_selected = modelActivity.getNombre();


                act_lstdocente.setText(modelActivity.getDocente());
                idActividad.setText(String.valueOf(modelActivity.getId()));
                descripcion_actividad.setText(modelActivity.getDescripcion());
                til_tipo.setVisibility(View.GONE);
                //setTipo();
                setDificultad();
                setSubnivel();
                break;

        }
    }

    ArrayAdapter arrayAdapterDificultad, arrayAdapterTipo, arrayAdapter_lstsubnivel, arrayAdapter_lstdocente, arrayAdapter_nombreactividad;


    public void fill_items_list(View v) {
        act_dificultad = (AutoCompleteTextView) v.findViewById(R.id.act_dificultad);
        arrayAdapterDificultad = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item
                , getResources().getStringArray(R.array.dificultad_list));
        act_dificultad.setAdapter(arrayAdapterDificultad);
        act_dificultad.setThreshold(1);


        act_tipo = (AutoCompleteTextView) v.findViewById(R.id.act_tipo);
        arrayAdapterTipo = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item
                , getResources().getStringArray(R.array.tipos_list));

        act_tipo.setAdapter(arrayAdapterTipo);
        act_tipo.setThreshold(1);

        nombre_actividad = (AutoCompleteTextView) v.findViewById(R.id.nombre_actividad);
        arrayAdapter_nombreactividad = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item
                , getResources().getStringArray(R.array.actividades_list));
        nombre_actividad.setAdapter(arrayAdapter_nombreactividad);
        nombre_actividad.setThreshold(1);
    }

    /*
    public void loadImage(View view) {
        //Toast.makeText(this, act_dificultad.getText(), Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, act_tipo.getText(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        try {
            startActivityForResult(intent.createChooser(intent, "Seleccione la aplicación"), 10);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    */

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*
        if (resultCode == RESULT_OK) {
            Uri path = data.getData();
            //civ_actividad_id.setImageURI(path);
            img_actividad.setImageURI(data.getData());
            //Glide.with(getContext()).load(data.getData()).into(img_actividad);
        } else {
            Toast.makeText(getContext(), "Error al cargar la imagen intente nuevamente", Toast.LENGTH_LONG).show();
        }
        */
        //System.out.println("Error " + resultCode);
    }


    private ArrayList<ModelSublevel> sublevels;
    private JsonArrayRequest jsonArrayRequest;

    public void loadSubniveles(String urlR) {
        sublevels = new ArrayList<>();
        jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlR, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ModelSublevel sublevel = null;
                        ArrayList sublevelsName = new ArrayList<>();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                sublevel = new ModelSublevel();
                                JSONObject objSublevel = response.getJSONObject(i);
                                sublevel.setId(objSublevel.getInt("id"));
                                sublevel.setNombre(objSublevel.getString("nombre"));
                                sublevel.setDescripcion(objSublevel.getString("descripcion"));
                                sublevel.setNumactividades(objSublevel.getInt("numactividades"));
                                sublevel.setPrioridad(objSublevel.getInt("prioridad"));
                                sublevel.setPublicid(objSublevel.getString("publicid"));
                                sublevel.setUrl(objSublevel.getString("url"));
                                sublevel.setActivo(objSublevel.getBoolean("activo"));

                                sublevels.add(sublevel);
                                sublevelsName.add(objSublevel.getString("nombre"));

                            }

                            if (getContext() != null) {
                                arrayAdapter_lstsubnivel = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item
                                        , sublevelsName);
                                act_lstsubnivel.setAdapter(arrayAdapter_lstsubnivel);
                                act_lstsubnivel.setThreshold(1);
                            }

                            if (modelActivity != null) {
                                //Add the sublevel selected
                                for (int i = 0; i < sublevels.size(); i++) {
                                    if (sublevels.get(i).getId() == modelActivity.getIdSubnivel()) {
                                        //Toast.makeText(getContext(), " Esta seleccionado " + modelActivity.getSubnivel(), Toast.LENGTH_SHORT).show();
                                        modelSublevelSelected = sublevels.get(i);
                                        break;
                                    }
                                }
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


    List<ModelTeacher> docentes;

    public void loadDocentes(String urlR) {
        docentes = new ArrayList<>();
        jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlR, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ModelTeacher docente = null;
                        ArrayList subdocenteName = new ArrayList<>();
                        try {
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject objDocente = response.getJSONObject(i);
                                docente = new ModelTeacher(
                                        objDocente.getInt("idPersona"),
                                        objDocente.getString("nombres"),
                                        objDocente.getString("apellidos"),
                                        objDocente.getString("telefono"),
                                        objDocente.getString("correo"),
                                        objDocente.getString("fechanacimiento"),
                                        objDocente.getString("tipo"),
                                        objDocente.getBoolean("activo"));
                                docentes.add(docente);
                                String nameComplete = docente.getNombres() + " " + docente.getApellidos();
                                subdocenteName.add(nameComplete);

                            }

                            if (getContext() != null) {
                                arrayAdapter_lstdocente = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item
                                        , subdocenteName);
                                act_lstdocente.setAdapter(arrayAdapter_lstdocente);
                                act_lstdocente.setThreshold(1);
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

    public void setTipo() {
        if (modelActivity.getTipo().equals("AC")) {
            act_tipo.setText(arrayAdapterTipo.getItem(0).toString(), false);
            //tipoSelected = arrayAdapterTipo.getItem(0).toString();
        } else {
            act_tipo.setText(arrayAdapterTipo.getItem(1).toString(), false);
            //tipoSelected = arrayAdapterTipo.getItem(1).toString();
        }
    }

    public void setDificultad() {
        switch (modelActivity.getRecompensavalor()) {
            case 1:
                dificultadSelected = "Muy Baja";
                act_dificultad.setText("Muy Baja", false);
                break;
            case 2:
                dificultadSelected = "Baja";
                act_dificultad.setText("Baja", false);
                break;
            case 4:
                dificultadSelected = "Media";
                act_dificultad.setText("Media", false);
                break;
            case 6:
                dificultadSelected = "Alta";
                act_dificultad.setText("Alta", false);
                break;
            case 8:
                dificultadSelected = "Muy Alta";
                act_dificultad.setText("Muy Alta", false);
                break;
            case 10:
                dificultadSelected = "Extrema";
                act_dificultad.setText("Extrema", false);
                break;
        }
    }

    public void setSubnivel() {
        act_lstsubnivel.setText(modelActivity.getSubnivel(), false);
    }


    String nameActividad, descriptionActividad;

    @Override
    public void onClick(View v) {
        if (txt_TituloFrg.getText().equals("Editar")) {
            if (modelActivity != null && modelSublevelSelected != null &&
                    actividad_selected != null && descripcion_actividad.getText().toString().length() > 0 &&
                    dificultadSelected != null) {
                JSONObject param = new JSONObject();
                try {
                    //Parámetros a enviar a la API
                    param.put("id", modelActivity.getId());
                    param.put("idSubnivel", modelSublevelSelected.getId());
                    param.put("nombre", actividad_selected);
                    param.put("descripcion", descripcion_actividad.getText().toString());
                    param.put("recompensavalor", getDificultad(dificultadSelected));
                    param.put("activo", true);
                    System.out.println("Ok");
                } catch (JSONException ex) {
                    Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
                updateActividad(getString(R.string.urlBase) + "actividad", param);

            } else {
                Toast.makeText(getContext(), "Por favor complete los datos correspondientes para editar", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (modelSublevelSelected != null && modelDocenteSelected != null && actividad_selected != null &&
                    descripcion_actividad.getText().toString().length() > 0 && dificultadSelected != null &&
                    tipoSelected != null) {
                JSONObject param = new JSONObject();
                try {
                    //Parámetros a enviar a la API
                    param.put("idSubnivel", modelSublevelSelected.getId());


                    //Original
                    param.put("idDocente", modelDocenteSelected.getId());


                    ////Quitar
                    //param.put("idDocente", idDocente);
                    ///Quitar


                    param.put("nombre", actividad_selected);
                    param.put("descripcion", descripcion_actividad.getText().toString());
                    param.put("recompensavalor", getDificultad(dificultadSelected));
                    param.put("tipo", getTipo(tipoSelected));
                } catch (JSONException ex) {
                    Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
                registerActividad(getString(R.string.urlBase) + "actividad", param);

            } else {
                Toast.makeText(getContext(), "Vas a guardar un dato, completa todos los campos", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private JsonObjectRequest jsonObjectRequest;


    public void registerActividad(String urlS, JSONObject param) {
        jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, urlS, param,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.length() > 1) {
                                Toast.makeText(getContext(), "Actividad registrada exitosamente", Toast.LENGTH_SHORT).show();
                                redirectActividades();
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

    public void updateActividad(String urlS, JSONObject param) {
        jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, urlS, param,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.length() > 1) {
                                Toast.makeText(getContext(), "Actividad actualizada exitosamente", Toast.LENGTH_SHORT).show();
                                redirectActividades();
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

    public Integer getDificultad(String param) {
        switch (param) {
            case "Muy Baja":
                return 1;
            case "Baja":
                return 2;
            case "Media":
                return 4;
            case "Alta":
                return 6;
            case "Muy Alta":
                return 8;
            case "Extrema":
                return 10;
        }
        return 0;
    }

    public String getTipo(String param) {
        switch (param) {
            case "Actividad Común":
                return "AC";
            case "Evaluación":
                return "EV";
        }
        return "ER";
    }

    Fragment fragment;

    public void redirectActividades() {
        fragment = new FrgActividad();
        getFragmentManager().beginTransaction().replace(R.id.content, fragment).addToBackStack(null).commit();
    }

    /*
    ///////Eliminar
    int idDocente;

    public void getIdDocente(String urlR) {
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlR, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject obj = response.getJSONObject("docente");
                            idDocente = obj.getInt("id");

                            //Toast.makeText(getContext(), "ID docente recuperado :" + idDocente, Toast.LENGTH_LONG).show();
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
        ClssVolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(jsonObjectRequest);

    }
    ////Eliminar

    */
}

