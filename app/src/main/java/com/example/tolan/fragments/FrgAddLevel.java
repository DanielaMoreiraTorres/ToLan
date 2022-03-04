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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.tolan.R;
import com.example.tolan.clases.ClssConvertirTextoAVoz;
import com.example.tolan.clases.ClssGetRealPath;
import com.example.tolan.clases.ClssValidations;
import com.example.tolan.clases.ClssVolleySingleton;
import com.example.tolan.interfaces.MultimediaApi;
import com.example.tolan.models.ModelLevel;
import com.example.tolan.models.ModelUploadImage;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FrgAddLevel extends Fragment {

    private Toolbar toolbar;
    private ProgressBar progressBar;
    private FloatingActionButton btnimagen, btnaddNSubnivel;
    public static final int PICK_IMAGE = 1;
    private static int contResult = 0;
    private TextView txttxtTitleN;
    private TextInputLayout Lnombre, Ldescripcion;
    private TextInputEditText txtnameNi, txtdescripcionNivel;
    private ClssValidations validate;
    private String Merror = "Campo obligatorio";
    private JsonObjectRequest jsonObjectRequest;
    public Button acceptN;
    private CheckBox checkBoxNivel;
    private ImageView imagen;
    private Fragment fragment;
    private Uri imageUri;
    private File file = null;
    private ClssGetRealPath clssGetRealPath = new ClssGetRealPath();
    private MultimediaApi multimediaApi;
    private String urlN, url, publicid;
    private ModelLevel levelsel = null;
    private JSONObject multimedia;

    public FrgAddLevel() {
        // Required empty public constructor
    }

    public static FrgAddLevel newInstance(String param1, String param2) {
        FrgAddLevel fragment = new FrgAddLevel();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            levelsel = (ModelLevel) getArguments().getSerializable("levelSelected");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_level, container, false);
        try {
            //levelsel = (ModelLevel) getActivity().getIntent().getSerializableExtra("levelSelected");
            toolbar = view.findViewById(R.id.toolbar);
            setHasOptionsMenu(true);
            ((AppCompatActivity) this.getActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity) this.getActivity()).getSupportActionBar().setTitle("");
            urlN = getString(R.string.urlBase) + "nivel";
            validate = new ClssValidations();
            progressBar = view.findViewById(R.id.progressBar);
            txttxtTitleN = view.findViewById(R.id.txtTitleN);
            txttxtTitleN.setOnClickListener(v -> ClssConvertirTextoAVoz.getIntancia(v.getContext()).reproduce(txttxtTitleN.getText().toString()));
            Lnombre = view.findViewById(R.id.nameNivel);
            Ldescripcion = view.findViewById(R.id.descripcionNivel);
            txtnameNi = view.findViewById(R.id.nameNi);
            txtdescripcionNivel = view.findViewById(R.id.txtdescripcionNivel);
            checkBoxNivel = view.findViewById(R.id.checkBoxNivel);
            imagen = view.findViewById(R.id.imgniv);
            btnimagen = view.findViewById(R.id.seleccionarimg);
            btnimagen.setOnClickListener(v -> openGallery());
            acceptN = view.findViewById(R.id.acceptN);
            acceptN.setOnClickListener(v -> AddOrRegister());
            //btnaddNSubnivel = view.findViewById(R.id.addNSubnivel);
            llenarComponents();
            validate.TextChanged(txtnameNi, null, Lnombre, Merror);
            //validate.TextChanged(txtdescripcionNivel, null, Ldescripcion, Merror);
        } catch (Exception e) {}
        return view;
    }

    private void AddOrRegister(){
        try{
            ClssConvertirTextoAVoz.getIntancia(getContext()).reproduce(acceptN.getText().toString());
            if(validate.Validar(txtnameNi,null,Lnombre,Merror)){
                //Ocultar teclado
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(txtdescripcionNivel.getWindowToken(), 0);
                progressBar.setVisibility(View.VISIBLE);
                if(levelsel != null){
                    if(contResult > 0)
                        registerMultimedia();
                    else {
                        updateLevel();
                        //updateLevel();
                    }
                }
                else{
                    registerMultimedia();
                }
            }
            else {
                ClssConvertirTextoAVoz.getIntancia(getContext()).reproduce("Nombre de nivel no válido");
                Toast.makeText(getContext(),"Nombre de nivel no válido",Toast.LENGTH_SHORT).show();
            }
        } catch (AuthFailureError authFailureError) {
            authFailureError.printStackTrace();
        }
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
            //imagen.setImageURI(imageUri);
            Glide.with(this)
                    .load(imageUri)
                    .into(imagen);
        }
    }

    public void llenarComponents()
    {
        if(levelsel != null){
            //btnaddNSubnivel.setVisibility(View.GONE);
            txttxtTitleN.setText("Modifique datos del nivel");
            checkBoxNivel.setChecked(levelsel.getActivo());
            checkBoxNivel.setEnabled(true);
            txtnameNi.setText(levelsel.getNombre());
            txtdescripcionNivel.setText(levelsel.getDescripcion());
            Glide.with(this)
                    .load(levelsel.getUrl())
                    .into(imagen);
        }
        else {
            //btnaddNSubnivel.setVisibility(View.VISIBLE);
            txttxtTitleN.setText(R.string.addnivel);
            checkBoxNivel.setChecked(true);
            checkBoxNivel.setEnabled(false);
            txtnameNi.setText("");
            txtdescripcionNivel.setText("");
            Glide.with(this)
                    .load(R.drawable.background_image)
                    .into(imagen);
        }
    }

    /*public static String cleanString(String texto) {
        texto = Normalizer.normalize(texto, Normalizer.Form.NFD);
        texto = texto.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return texto;
    }*/

    public void registerMultimedia() {
        try {
            MultipartBody.Part requestImage = null;
            Retrofit retrofit = new Retrofit.Builder().baseUrl(getString(R.string.urlBase))
                    .addConverterFactory(GsonConverterFactory.create()).build();
            multimediaApi = retrofit.create(MultimediaApi.class);
            if(imageUri != null) {
                if (file == null) {
                    String path = clssGetRealPath.getRealPath(getContext(), imageUri);
                    //String pathNorm = cleanString(path);
                    file = new File(path);
                }
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                requestImage = MultipartBody.Part.createFormData("multipartFile", URLEncoder.encode(file.getName(), "utf-8"), requestFile);
                Call<ModelUploadImage> call = multimediaApi.uploadImage(requestImage);
                call.enqueue(new Callback<ModelUploadImage>() {
                    @Override
                    public void onResponse(Call<ModelUploadImage> call, Response<ModelUploadImage> response) {
                        if (response.isSuccessful()) {
                            try {
                                response.body();
                                multimedia = new JSONObject();
                                url = response.body().getUrl();
                                publicid = response.body().getPublicid();
                                multimedia.put("publicid", publicid);
                                multimedia.put("url", url);
                                if (levelsel != null) {
                                    updateLevel();
                                    //updateLevel();
                                } else
                                    registerLevel();
                            } catch (JSONException | AuthFailureError e) {
                                e.printStackTrace();
                            }
                            //Toast.makeText(getContext(), "Imagen registrada correctamente", Toast.LENGTH_SHORT).show();
                        } else
                            progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<ModelUploadImage> call, Throwable t) {
                        //Toast.makeText(getContext(), "Error: " + t, Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
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
            progressBar.setVisibility(View.GONE);
        }
    }

    public void registerLevel() {
        try {
            //Parámetros a enviar a la API
            JSONObject param = new JSONObject();
            param.put("nombre", txtnameNi.getText().toString().trim());
            param.put("descripcion", txtdescripcionNivel.getText().toString().trim());
            param.put("multimedia", multimedia);
            jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,urlN, param,
                    new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response.length() > 1) {
                                    Toast.makeText(getContext(), "Nivel registrado exitosamente", Toast.LENGTH_SHORT).show();
                                    ClssConvertirTextoAVoz.getIntancia(getContext()).reproduce("Nivel registrado exitosamente");
                                    progressBar.setVisibility(View.GONE);
                                    redirectLevels();
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
            // Añadir petición a la cola
            //requestQueue.add(request_json);
            ClssVolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(jsonObjectRequest);
        } catch (JSONException e) {}
    }

    public void updateLevel () throws AuthFailureError{
        try {
            //Parámetros a enviar a la API
            JSONObject param = new JSONObject();
            param.put("id", levelsel.getId());
            param.put("nombre", txtnameNi.getText().toString().trim());
            param.put("descripcion", txtdescripcionNivel.getText().toString().trim());
            param.put("activo", checkBoxNivel.isChecked());
            if(multimedia == null){
                multimedia = new JSONObject();
                multimedia.put("publicid", levelsel.getPublicid());
                multimedia.put("url", levelsel.getUrl());
            }
            else
                param.put("multimedia", multimedia);
            jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, urlN, param,
                    new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Toast.makeText(getContext(),"Nivel actualizado exitosamente", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            redirectLevels();
                        }
                    },
                    new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Toast.makeText(getContext(),error.getMessage(), Toast.LENGTH_LONG).show();
                            try {
                                updateLevel();
                            } catch (AuthFailureError authFailureError) {
                                authFailureError.printStackTrace();
                            }
                            //System.out.println("Este es el error:" + error.networkResponse.data);
                        }
                    });
            // Añadir petición a la cola
            ClssVolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(jsonObjectRequest);
        } catch (Exception e) {
            //Toast.makeText(getContext(),e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void redirectLevels() {
        fragment = new FrgLevel();
        getFragmentManager().beginTransaction().replace(R.id.content, fragment).addToBackStack(null).commit();
    }
}