package com.example.tolan.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.example.tolan.R;
import com.example.tolan.adapters.AdpAutocompleteDocente;
import com.example.tolan.adapters.AdpAutocompleteLevel;
import com.example.tolan.clases.ClssConvertirTextoAVoz;
import com.example.tolan.clases.ClssGetRealPath;
import com.example.tolan.clases.ClssValidations;
import com.example.tolan.clases.ClssVolleySingleton;
import com.example.tolan.interfaces.MultimediaApi;
import com.example.tolan.models.ModelLevel;
import com.example.tolan.models.ModelSublevel;
import com.example.tolan.models.ModelUploadImage;
import com.example.tolan.models.ModelUser;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FrgAddSublevel extends Fragment {

    private Toolbar toolbar;
    private ProgressBar progressBar;
    private FloatingActionButton btnimagen, btnaddNSubnivel;
    public static final int PICK_IMAGE = 1;
    private static int contResult = 0;
    private AutoCompleteTextView nivel;
    private TextView txttxtTitleS;
    private TextInputLayout Lnivel, Lnombre, Ldescripcion;
    private TextInputEditText txtnameS, txtdescripcionS;
    private ClssValidations validate;
    private String Merror = "Campo obligatorio";
    private JsonArrayRequest jsonArrayRequest;
    private JsonObjectRequest jsonObjectRequest;
    public Button acceptS;
    private CheckBox checkBoxS;
    private ImageView imagen;
    private Fragment fragment;
    private List<ModelLevel> levels;
    private AdpAutocompleteLevel adp;
    private ModelLevel selectedLevel;
    private Uri imageUri;
    private File file = null;
    private ClssGetRealPath clssGetRealPath = new ClssGetRealPath();
    private MultimediaApi multimediaApi;
    private String urlN, urlS, url, publicid;
    private ModelSublevel sublevelSel = null;
    private JSONObject selectedLev, multimedia;

    public FrgAddSublevel() {
        // Required empty public constructor
    }

    public static FrgAddSublevel newInstance(String param1, String param2) {
        FrgAddSublevel fragment = new FrgAddSublevel();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            sublevelSel = (ModelSublevel) getArguments().getSerializable("sublevelSelected");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_sublevel, container, false);
        try {
            toolbar = view.findViewById(R.id.toolbar);
            setHasOptionsMenu(true);
            ((AppCompatActivity) this.getActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity) this.getActivity()).getSupportActionBar().setTitle("");
            urlN = getString(R.string.urlBase) + "nivel";
            urlS = getString(R.string.urlBase) + "subnivel";
            levels = new ArrayList<>();
            nivel = view.findViewById(R.id.nivel);
            validate = new ClssValidations();
            progressBar = view.findViewById(R.id.progressBar);
            txttxtTitleS = view.findViewById(R.id.txtTitleS);
            txttxtTitleS.setOnClickListener(v -> ClssConvertirTextoAVoz.getIntancia(v.getContext()).reproduce(txttxtTitleS.getText().toString()));
            nivel.setThreshold(1);
            autocomplete();
            Lnivel = view.findViewById(R.id.Lnivel);
            Lnombre = view.findViewById(R.id.nameSubnivel);
            Ldescripcion = view.findViewById(R.id.descripcionSub);
            txttxtTitleS = view.findViewById(R.id.txtTitleS);
            txtnameS = view.findViewById(R.id.nameS);
            txtdescripcionS = view.findViewById(R.id.txtdescripcionSubnivel);
            checkBoxS = view.findViewById(R.id.checkBoxSubnivel);
            imagen = view.findViewById(R.id.imgsubniv);
            btnimagen = view.findViewById(R.id.seleccionarimg);
            btnimagen.setOnClickListener(v -> openGallery());
            acceptS = view.findViewById(R.id.acceptS);
            acceptS.setOnClickListener(v -> AddOrRegister());
            llenarComponents();
            validate.TextChanged(null, nivel, Lnivel, Merror);
            validate.TextChanged(txtnameS, null, Lnombre, Merror);
            //validate.TextChanged(txtdescripcionS, null, Ldescripcion, Merror);
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

    private void openGallery() {
        ClssConvertirTextoAVoz.getIntancia(getContext()).reproduce("Seleccionar imagen");
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE) {
            contResult += 1;
            imageUri = data.getData();
            Glide.with(this)
                    .load(imageUri)
                    .into(imagen);
        }
    }

    public void llenarComponents() {
        if(sublevelSel != null){
            //btnaddNSubnivel.setVisibility(View.GONE);
            //nivel.setText(sublevelSel.getIdnivel());
            Lnivel.setVisibility(View.GONE);
            txttxtTitleS.setText("Modifique datos del subnivel");
            checkBoxS.setChecked(sublevelSel.getActivo());
            checkBoxS.setEnabled(true);
            txtnameS.setText(sublevelSel.getNombre());
            txtdescripcionS.setText(sublevelSel.getDescripcion());
            Glide.with(this)
                    .load(sublevelSel.getUrl())
                    .into(imagen);
        }
        else {
            //btnaddNSubnivel.setVisibility(View.VISIBLE);
            Lnivel.setVisibility(View.VISIBLE);
            txttxtTitleS.setText("Ingrese datos del subnivel");
            checkBoxS.setChecked(true);
            checkBoxS.setEnabled(false);
            txtnameS.setText("");
            txtdescripcionS.setText("");
            Glide.with(this)
                    .load(R.drawable.background_image)
                    .into(imagen);
        }
    }

    private void autocomplete(){
        try {
            jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlN, null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            ModelLevel level = null;
                            try {
                                for (int i = 0; i < response.length(); i++) {
                                    level = new ModelLevel();
                                    JSONObject objLevel = response.getJSONObject(i);
                                    if (objLevel.getBoolean("activo")) {
                                        level.setId(objLevel.getInt("id"));
                                        level.setNombre(objLevel.getString("nombre").trim());
                                        level.setDescripcion(objLevel.getString("descripcion").trim());
                                        level.setPrioridad(objLevel.getInt("prioridad"));
                                        level.setPublicid(objLevel.getString("publicid").trim());
                                        level.setUrl(objLevel.getString("url").trim());
                                        level.setActivo(objLevel.getBoolean("activo"));
                                        levels.add(level);
                                    }
                                }
                                if(getContext() != null) {
                                    adp = new AdpAutocompleteLevel(getContext(), levels);
                                    nivel.setAdapter(adp);
                                    nivel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                                            try {
                                                //Ldocente.setError(null);
                                                selectedLevel = (ModelLevel) adapterView.getItemAtPosition(pos);
                                                selectedLev = new JSONObject();
                                                selectedLev.put("id", selectedLevel.getId());
                                                selectedLev.put("nombre", selectedLevel.getNombre().trim());
                                                selectedLev.put("descripcion", selectedLevel.getDescripcion().trim());
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                //Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            VolleyLog.e("Error: ", error.getMessage());
                        }
                    });
            //requestQueue.add(jsonArrayRequest);
            ClssVolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(jsonArrayRequest);
        }
        catch (Exception e){}
    }

    private void AddOrRegister(){
        try{
            ClssConvertirTextoAVoz.getIntancia(getContext()).reproduce(acceptS.getText().toString());
            if(validate.Validar(null,nivel,Lnivel,Merror) & validate.Validar(txtnameS,null,Lnombre,Merror)) {
                //Ocultar teclado
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(txtdescripcionS.getWindowToken(), 0);
                progressBar.setVisibility(View.VISIBLE);
                if (sublevelSel != null) {
                    if (contResult > 0)
                        registerMultimedia();
                    else {
                        updateSublevel();
                        //updateSublevel();
                    }
                } else {
                    if(nivel.getText().toString().trim().equals(selectedLev.getString("nombre").trim())){
                        Lnivel.setError(null);
                        registerMultimedia();
                    }
                    else{
                        ClssConvertirTextoAVoz.getIntancia(getContext()).reproduce("Nivel no válido");
                        Lnivel.setError("Nivel no válido");
                        return;
                    }
                }
            }
            else {
                ClssConvertirTextoAVoz.getIntancia(getContext()).reproduce("Datos no válidos");
                Toast.makeText(getContext(),"Datos no válidos",Toast.LENGTH_SHORT).show();
            }
        } catch (AuthFailureError | JSONException authFailureError) {
            authFailureError.printStackTrace();
        }
    }

    public void registerMultimedia() {
        try {
            MultipartBody.Part requestImage = null;
            Retrofit retrofit = new Retrofit.Builder().baseUrl(getString(R.string.urlBase))
                    .addConverterFactory(GsonConverterFactory.create()).build();
            multimediaApi = retrofit.create(MultimediaApi.class);
            if(imageUri != null) {
                if (file == null) {
                    String path = clssGetRealPath.getRealPath(getContext(), imageUri);
                    file = new File(path);
                }
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                requestImage = MultipartBody.Part.createFormData("multipartFile", URLEncoder.encode(file.getName(), "utf-8"), requestFile);
                Call<ModelUploadImage> call = multimediaApi.uploadImage(requestImage);
                call.enqueue(new Callback<ModelUploadImage>() {
                    @Override
                    public void onResponse(Call<ModelUploadImage> call, retrofit2.Response<ModelUploadImage> response) {
                        if (response.isSuccessful()) {
                            try {
                                response.body();
                                multimedia = new JSONObject();
                                url = response.body().getUrl();
                                publicid = response.body().getPublicid();
                                multimedia.put("publicid", publicid);
                                multimedia.put("url", url);
                                if (sublevelSel != null) {
                                    updateSublevel();
                                    //updateSublevel();
                                } else
                                    registerSublevel();
                            } catch (JSONException | AuthFailureError e) {
                                e.printStackTrace();
                            }
                            //Toast.makeText(getContext(), "Imagen registrada correctamente", Toast.LENGTH_SHORT).show();
                        }
                        //Toast.makeText(getContext(), response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<ModelUploadImage> call, Throwable t) {
                        //Toast.makeText(getContext(), "Error: " + t, Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else{
                Toast.makeText(getContext(), "Seleccione una imagen", Toast.LENGTH_SHORT).show();
                ClssConvertirTextoAVoz.getIntancia(getContext()).reproduce("Seleccione una imagen");
                progressBar.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            String error = e.toString();
        }
    }

    public void registerSublevel() {
        try {
            //Parámetros a enviar a la API
            JSONObject param = new JSONObject();
            param.put("idNivel", selectedLevel.getId());
            param.put("nombre", txtnameS.getText().toString().trim());
            param.put("descripcion", txtdescripcionS.getText().toString().trim());
            param.put("multimedia", multimedia);
            jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,urlS, param,
                    new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response.length() > 1) {
                                    Toast.makeText(getContext(), "Subnivel registrado exitosamente", Toast.LENGTH_SHORT).show();
                                    ClssConvertirTextoAVoz.getIntancia(getContext()).reproduce("Subnivel registrado exitosamente");
                                    progressBar.setVisibility(View.GONE);
                                    redirectSublevels();
                                } else{
                                    Toast.makeText(getContext(), response.get("message").toString(), Toast.LENGTH_SHORT).show();
                                    ClssConvertirTextoAVoz.getIntancia(getContext()).reproduce(response.get("message").toString());
                                    progressBar.setVisibility(View.GONE);
                                }
                            } catch (Exception e) {
                                //Toast.makeText(getContext(),"Error de conexión",Toast.LENGTH_LONG).show();
                            }
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.e("Error: ", error.getMessage());
                    //Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getContext(), "Error de conexión con el servidor\nIntente nuevamente", Toast.LENGTH_SHORT).show();
                    ClssConvertirTextoAVoz.getIntancia(getContext()).reproduce("Error de conexión con el servidor. Intente nuevamente");
                    progressBar.setVisibility(View.GONE);
                }
            });
            ClssVolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(jsonObjectRequest);
        } catch (JSONException e) {}
    }

    public void updateSublevel () throws AuthFailureError{
        try {
            //Parámetros a enviar a la API
            JSONObject param = new JSONObject();
            param.put("id", sublevelSel.getId());
            param.put("nombre", txtnameS.getText().toString().trim());
            param.put("descripcion", txtdescripcionS.getText().toString().trim());
            param.put("activo", checkBoxS.isChecked());
            if(multimedia == null){
                multimedia = new JSONObject();
                multimedia.put("publicid", sublevelSel.getPublicid());
                multimedia.put("url", sublevelSel.getUrl());
            }
            else
                param.put("multimedia", multimedia);
            jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, urlS, param,
                    new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Toast.makeText(getContext(),"Subnivel actualizado exitosamente", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            redirectSublevels();
                        }
                    },
                    new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Toast.makeText(getContext(),error.getMessage(), Toast.LENGTH_LONG).show();
                            //System.out.println("Este es el error:" + error.networkResponse.data);
                            try {
                                updateSublevel();
                            } catch (AuthFailureError authFailureError) {
                                authFailureError.printStackTrace();
                            }
                        }
                    });
            // Añadir petición a la cola
            ClssVolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(jsonObjectRequest);
        } catch (Exception e) {
            //Toast.makeText(getContext(),e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void redirectSublevels() {
        fragment = new FrgSublevel();
        getFragmentManager().beginTransaction().replace(R.id.content, fragment).addToBackStack(null).commit();
    }
}