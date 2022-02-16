package com.example.tolan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.tolan.clases.ClssGetRealPath;
import com.example.tolan.fragments.FrgLevel;
import com.example.tolan.fragments.FrgLogin;
import com.example.tolan.models.ModelLevel;
import com.example.tolan.models.ModelUploadImage;
import com.example.tolan.interfaces.MultimediaApi;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActivityAddLevel extends AppCompatActivity {

    private Toolbar toolbar;
    private FloatingActionButton btnimagen, btnaddNSubnivel;
    public static final int PICK_IMAGE = 111;
    private TextView txttxtTitleN, txtnameNi, txttxtdescripcionNivel;
    private CheckBox checkBoxNivel;
    private ImageView imagen;
    private Fragment fragment;
    Uri imageUri;
    private RequestQueue requestQueue;
    private String urlB = "https://db-bartolucci.herokuapp.com/multimedia/saveFileMedia";
    private StringRequest stringRequest;
    File file = null;
    ClssGetRealPath clssGetRealPath = new ClssGetRealPath();
    MultimediaApi multimediaApi;
    public String url = null;
    public String publicid = null;
    ModelLevel levelsel = null;
    JSONObject multimedia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_level);

        levelsel = (ModelLevel) getIntent().getSerializableExtra("levelSelected");
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        txttxtTitleN = findViewById(R.id.txtTitleN);
        txtnameNi = findViewById(R.id.nameNi);
        txttxtdescripcionNivel = findViewById(R.id.txtdescripcionNivel);
        checkBoxNivel = findViewById(R.id.checkBoxNivel);
        imagen = findViewById(R.id.imgniv);
        btnimagen = findViewById(R.id.seleccionarimg);
        btnimagen.setOnClickListener(v -> openGallery());
        btnaddNSubnivel = findViewById(R.id.addNSubnivel);
        btnaddNSubnivel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityAddLevel.this, ActivitySublevel.class);
                startActivity(intent);
            }
        });
        llenarComponents();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.btnNotifi) {

        }
        if(id == R.id.btnLogIn) {
            /*
            Intent intent = new Intent(this, ActivityLogin.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);*/
        }
        if(id == R.id.btnContacts) {
            Intent intent = new Intent(this, ActivityContact.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            //imagen.setImageURI(imageUri);
            Glide.with(this)
                    .load(imageUri)
                    .into(imagen);
        }
    }

    public void registerMultimedia(View view) throws URISyntaxException {
        MultipartBody.Part requestImage = null;
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://db-bartolucci.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        multimediaApi = retrofit.create(MultimediaApi.class);
        if(file == null){
            String path = clssGetRealPath.getRealPath(this,imageUri);
            file = new File(path);
        }
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"),file);
        requestImage = MultipartBody.Part.createFormData("multipartFile",file.getName(),requestFile);

        Call<ModelUploadImage> call = multimediaApi.uploadImage(requestImage);
        call.enqueue(new Callback<ModelUploadImage>() {
            @Override
            public void onResponse(Call<ModelUploadImage> call, Response<ModelUploadImage> response) {
                if(response.isSuccessful()){
                    try {
                        response.body();
                        url = response.body().getUrl();
                        publicid = response.body().getPublicid();
                        multimedia.put("publicid", publicid);
                        multimedia.put("url", url);
                        registerLevel();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //Toast.makeText(ActivityAddLevel.this, "Imagen registrada correctamente", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(ActivityAddLevel.this, response.errorBody().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ModelUploadImage> call, Throwable t) {
                Toast.makeText(ActivityAddLevel.this, "Error: " + t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void registerLevel() throws JSONException {
        // Crear nueva cola de peticiones
        requestQueue= Volley.newRequestQueue(ActivityAddLevel.this);
        //Parámetros a enviar a la API
        JSONObject param = new JSONObject();
        param.put("nombre", txtnameNi.getText().toString().trim());
        param.put("descripcion", txttxtdescripcionNivel.getText().toString().trim());
        param.put("multimedia", multimedia);
        JsonObjectRequest request_json = new JsonObjectRequest(url, param,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.length() > 1)
                            {
                                Toast.makeText(ActivityAddLevel.this,"Nivel Registrado",Toast.LENGTH_LONG).show();
                                redirectLevels();
                            }
                            else
                                Toast.makeText(ActivityAddLevel.this,response.get("message").toString(),Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Toast.makeText(ActivityAddLevel.this,"Error de conexión",Toast.LENGTH_LONG).show();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                Toast.makeText(ActivityAddLevel.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        // Añadir petición a la cola
        requestQueue.add(request_json);
    }

    void llenarComponents()
    {
        if(!levelsel.equals(null)){
            btnaddNSubnivel.setVisibility(View.GONE);
            txttxtTitleN.setText("Modifique datos del nivel");
            checkBoxNivel.setEnabled(levelsel.getActivo());
            txtnameNi.setText(levelsel.getNombre());
            txttxtdescripcionNivel.setText(levelsel.getDescripcion());
            Glide.with(this)
                    .load(levelsel.getUrl())
                    .into(imagen);
        }
        else {
            btnaddNSubnivel.setVisibility(View.VISIBLE);
            txttxtTitleN.setText(R.string.addnivel);
            checkBoxNivel.setEnabled(true);
            txtnameNi.setText("");
            txttxtdescripcionNivel.setText("");
            Glide.with(this)
                    .load(R.drawable.background_image)
                    .into(imagen);
        }
    }

    private void redirectLevels() {
        /*Intent intent = new Intent(ActivityAddLevel.this, ActivityLevel.class);
        startActivity(intent);*/
        fragment = new FrgLevel();
        getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).addToBackStack(null).commit();
    }
}