package com.example.tolan.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.tolan.ActivityLogin;
import com.example.tolan.ActivityMenuAdmin;
import com.example.tolan.ActivityMenuDocente;
import com.example.tolan.ActivityRegisterUser;
import com.example.tolan.R;
import com.example.tolan.models.ModelUser;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FrgLogin extends Fragment {

    private FloatingActionButton btnLogin;
    private Button forgetPass, register;
    private TextInputEditText user, password;
    private TextInputLayout Lusuario, Lclave;
    String Merror= "Campo obligatorio";
    private Fragment fragment;
    private RequestQueue requestQueue;
    private String url = "https://db-bartolucci.herokuapp.com/usuario/login";

    public FrgLogin() {
        // Required empty public constructor
    }

    public static FrgLogin newInstance(String param1, String param2) {
        FrgLogin fragment = new FrgLogin();
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
        View view = inflater.inflate(R.layout.fragment_frg_login, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        Lusuario = view.findViewById(R.id.Lusuario);
        user = view.findViewById(R.id.txtuser);
        TextChanged(user,Lusuario,Merror);
        Lclave = view.findViewById(R.id.Lclave);
        password = view.findViewById(R.id.txtPass);
        TextChanged(password,Lclave,Merror);
        btnLogin = view.findViewById(R.id.btnSigin);
        btnLogin.setOnClickListener(v -> Login());
        forgetPass = view.findViewById(R.id.forget);
        register = view.findViewById(R.id.register);
        register.setOnClickListener(v -> RegisterUs());
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

    public void Login(){
        if(Validar(user,Lusuario,Merror) & Validar(password,Lclave,Merror)){
            getUsuario();
        }
        /*if(!user.getText().toString().isEmpty() & !password.getText().toString().isEmpty())

        else
            Toast.makeText(getContext(),"Usuario y/o clave incorrectos",Toast.LENGTH_LONG).show();*/
    }

    public void getUsuario(){
        // Crear nueva cola de peticiones
        requestQueue= Volley.newRequestQueue(getContext());
        //Parámetros a enviar a la API
        Map<String, String> parameters = new HashMap<>();
        parameters.put("username", user.getText().toString());
        parameters.put("password", password.getText().toString());
        JsonObjectRequest request_json = new JsonObjectRequest(url, new JSONObject(parameters),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.length() > 1){
                                ModelUser user = new ModelUser();
                                user.setUsuario(response.getString("usuario"));
                                user.setClave(response.getString("clave"));
                                user.setTipousuario(response.getString("tipousuario"));
                                user.setActivo(response.getBoolean("activo"));
                                user.setDocente(response.get("docente").toString());
                                user.setEstudiante(response.get("estudiante").toString());
                                Iniciar(user);
                            }
                            else
                                Toast.makeText(getContext(),response.get("message").toString(),Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Toast.makeText(getContext(),"Usuario y/o clave incorrectos",Toast.LENGTH_LONG).show();
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

    public void Iniciar(ModelUser muser){
        Intent intent = null;
        if(muser.getTipousuario().equals("AD"))
            fragment = new FrgMenuAdmin();
        else if(muser.getTipousuario().equals("DC"))
            fragment = new FrgMenuDocente();
        else
            fragment = new FrgWelcome();
        //Creamos la información a pasar entre actividades
        Bundle b = new Bundle();
        b.putString("user", muser.getUsuario());
        b.putString("tipousuario", muser.getTipousuario());
        //Añadimos la información e iniciamos el nuevo fragment
        fragment.setArguments(b);
        getFragmentManager().beginTransaction().replace(R.id.content, fragment).addToBackStack(null).commit();
    }

    public void RegisterUs() {
        fragment = new FrgRegisterUser();
        getFragmentManager().beginTransaction().replace(R.id.content, fragment).addToBackStack(null).commit();
    }
}