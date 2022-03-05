package com.example.tolan.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.example.tolan.R;
import com.example.tolan.clases.ClssConvertTextToSpeech;
import com.example.tolan.clases.ClssGetRealPath;
import com.example.tolan.clases.ClssVolleySingleton;
import com.example.tolan.interfaces.MultimediaApi;
import com.example.tolan.models.ModelUploadImage;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Diag_Frg_CargaMultimedia extends DialogFragment {

    Activity actividad;
    TextView txtIdContenido;

    String idContenido;
    FloatingActionButton fabtn_seleccionarimg;
    private Uri imageUri;
    public static final int PICK_IMAGE = 1;
    private static int contResult = 0;
    ImageView imgMultimedia;
    AutoCompleteTextView act_lst_tipoMultimedia;
    String tipoSelected;
    Button btn_registrar_multimedia;
    EditText descripcion_multimedia;
    CheckBox chkIsEnunciado;

    public Diag_Frg_CargaMultimedia(String idContenido) {
        this.idContenido = idContenido;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return crearDialogo();
    }

    private AlertDialog crearDialogo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View v = layoutInflater.inflate(R.layout.fragment_diag__frg__carga_multimedia, null);
        builder.setView(v);

        act_lst_tipoMultimedia = v.findViewById(R.id.act_lst_tipoMultimedia);
        fill_items_list();
        act_lst_tipoMultimedia.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                tipoSelected = (String) arrayAdapterlst_tipo_multimedia.getItem(position);
                Toast.makeText(getContext(), tipoSelected, Toast.LENGTH_LONG).show();
            }
        });

        descripcion_multimedia = v.findViewById(R.id.descripcion_multimedia);
        chkIsEnunciado = v.findViewById(R.id.chkIsEnunciado);
        txtIdContenido = v.findViewById(R.id.txtIdContenido);
        txtIdContenido.setText(idContenido);
        fabtn_seleccionarimg = v.findViewById(R.id.fabtn_seleccionarimg);
        imgMultimedia = v.findViewById(R.id.imgMultimedia);
        fabtn_seleccionarimg.setOnClickListener(this::openGallery);
        btn_registrar_multimedia = v.findViewById(R.id.btn_registrar_multimedia);
        btn_registrar_multimedia.setOnClickListener(this::generarMultimedia);


        return builder.create();
    }

    ArrayAdapter arrayAdapterlst_tipo_multimedia;

    public void fill_items_list() {
        arrayAdapterlst_tipo_multimedia = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item
                , getResources().getStringArray(R.array.tiposMultimedia_list));
        act_lst_tipoMultimedia.setAdapter(arrayAdapterlst_tipo_multimedia);
        act_lst_tipoMultimedia.setThreshold(1);
    }


    private void openGallery(View view) {
        ClssConvertTextToSpeech.getIntancia(getContext()).reproduce("Seleccionar imagen");
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
                    .into(imgMultimedia);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            this.actividad = (Activity) context;
        } else {
            throw new RuntimeException(context.toString() + "must implement OnFragmentInteractionListener");
        }
    }

    private JsonObjectRequest jsonObjectRequest;

    public void registrarMultimedia(String urlS, JSONObject param) {
        jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, urlS, param,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.length() > 1) {
                                Toast.makeText(getContext(), "Multimedia registrada exitosamente", Toast.LENGTH_SHORT).show();
                                limpiar();

                                //redirectActividades();
                            } else
                                Toast.makeText(getContext(), response.get("message").toString(), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            //Toast.makeText(getContext(),"Error de conexión",Toast.LENGTH_LONG).show();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
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

    private File file = null;
    private ClssGetRealPath clssGetRealPath = new ClssGetRealPath();
    private MultimediaApi multimediaApi;
    MultipartBody.Part requestImage = null;
    private JSONObject multimedia;
    private String url, publicid;

    public void generarMultimedia(View v) {

        if (tipoSelected != null && descripcion_multimedia.getText().length() > 0 && imageUri != null) {

            Retrofit retrofit = new Retrofit.Builder().baseUrl(getString(R.string.urlBase))
                    .addConverterFactory(GsonConverterFactory.create()).build();
            multimediaApi = retrofit.create(MultimediaApi.class);
            if (imageUri != null) {
                //if (file == null) {
                String path = clssGetRealPath.getRealPath(getContext(), imageUri);
                //String pathNorm = cleanString(path);
                file = new File(path);
                //}
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                try {
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

                                    JSONObject param = new JSONObject();
                                    try {
                                        //Parámetros a enviar a la API
                                        param.put("idContenido", idContenido);
                                        param.put("descripcion", descripcion_multimedia.getText().toString());
                                        param.put("tipo", tipoSelected);
                                        param.put("isInicial", chkIsEnunciado.isChecked());
                                        param.put("multimedia", multimedia);
                                    } catch (JSONException ex) {
                                        Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                    registrarMultimedia("https://db-bartolucci.herokuapp.com/multimedia", param);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                //Toast.makeText(getContext(), "Imagen registrada correctamente", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                                System.out.println(response.errorBody().toString());
                            }
                        }

                        @Override
                        public void onFailure(Call<ModelUploadImage> call, Throwable t) {
                            Toast.makeText(getContext(), "Error: " + t, Toast.LENGTH_SHORT).show();
                            System.out.println("Error :" + t.getMessage());
                        }
                    });
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Toast.makeText(getContext(), "Complete toda la información correspondiente", Toast.LENGTH_SHORT).show();
        }


    }

    public void limpiar() {
        descripcion_multimedia.setText("");
        chkIsEnunciado.setChecked(false);
        imgMultimedia.setImageResource(R.drawable.background_image);
        tipoSelected = null;
        imageUri = null;
        descripcion_multimedia.setFocusable(true);
    }
}