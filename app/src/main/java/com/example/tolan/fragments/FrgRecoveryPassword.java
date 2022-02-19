package com.example.tolan.fragments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.tolan.R;
import com.example.tolan.models.ModelUser;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FrgRecoveryPassword extends Fragment {

    private Button otro, recovery;
    private TextView txtTittle;
    private TextInputEditText celular, correo;
    private TextInputLayout Lcelular, Lcorreo;
    String Merror= "Campo obligatorio";
    private Fragment fragment;
    private RequestQueue requestQueue;
    private String urlP = "https://db-bartolucci.herokuapp.com/usuario/recoveryPhone?telefono=";
    private String urlE = "https://db-bartolucci.herokuapp.com/usuario/recoveryEmail?correo=";

    public FrgRecoveryPassword() {
        // Required empty public constructor
    }

    public static FrgRecoveryPassword newInstance(String param1, String param2) {
        FrgRecoveryPassword fragment = new FrgRecoveryPassword();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recovery_password, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        txtTittle = view.findViewById(R.id.txtRecoveryMetodo);
        Lcelular = view.findViewById(R.id.Ltelefono);
        celular = view.findViewById(R.id.telefono);
        TextChanged(celular,Lcelular,Merror);
        Lcorreo = view.findViewById(R.id.Lemail);
        Lcorreo.setVisibility(View.GONE);
        correo = view.findViewById(R.id.email);
        TextChanged(correo,Lcorreo,Merror);
        otro = view.findViewById(R.id.otro);
        otro.setOnClickListener(v -> OtroMetodo());
        recovery = view.findViewById(R.id.recovery);
        recovery.setOnClickListener(v -> Recovery());
        return view;
    }

    public Boolean Validar(TextInputEditText txt, TextInputLayout layout, String error){
        Boolean err = false;
        if(txt != null){
            if(txt.getText().length() > 0){
                layout.setError(null);
                err = true;
            }
            else{
                layout.setError(error);
                err = false;
            }
        }
        return err;
    }

    public void TextChanged(TextInputEditText txt, TextInputLayout layout, String error){
        if(txt != null){
            txt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    Validar(txt,layout,error);
                }
            });
        }
    }

    public void OtroMetodo(){
        if(Lcorreo.getVisibility() == View.GONE){
            txtTittle.setText(getString(R.string.msrecemail));
            Lcorreo.setVisibility(View.VISIBLE);
            Lcelular.setVisibility(View.GONE);
        }
        else{
            txtTittle.setText(getString(R.string.msrectel));
            Lcorreo.setVisibility(View.GONE);
            Lcelular.setVisibility(View.VISIBLE);
        }
    }

    public void Recovery() {
        String url = "";
        if(Lcorreo.getVisibility() == View.GONE)
            url = urlP + celular.getText().toString().trim();
        else
            url = urlE + correo.getText().toString().trim();
        // Crear nueva cola de peticiones
        requestQueue= Volley.newRequestQueue(getContext());
        JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Toast.makeText(getContext(),response.get("message").toString(),Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Toast.makeText(getContext(),"Error de conexión",Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });
        // Añadir petición a la cola
        requestQueue.add(request_json);
    }
}