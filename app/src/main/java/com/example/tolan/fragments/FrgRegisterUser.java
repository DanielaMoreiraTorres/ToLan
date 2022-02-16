package com.example.tolan.fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.tolan.ActivityLogin;
import com.example.tolan.R;
import com.example.tolan.adapters.AdpAutocompleteDocente;
import com.example.tolan.models.ModelUser;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FrgRegisterUser extends Fragment {

    private RequestQueue requestQueue;
    private JsonArrayRequest jsonArrayRequest;
    private String url = "https://db-bartolucci.herokuapp.com/usuario";
    private TextInputLayout Lnombres, Lapellidos, Ltelefono, Lemail, LFechaNac, Ldocente, Lusuario, Lclave, Lconfclave;
    private TextInputEditText nombre, apellido, telefono, email, fechaNac, usuario, clave, confirclave;
    private LinearLayout datosDocente;
    private SwitchMaterial switch_docent;
    private AutoCompleteTextView docente;
    private Button btnRegistrarse;
    private Fragment fragment;
    String Merror= "Campo obligatorio";
    int anio, mes, dia;
    List<ModelUser> docentes;
    AdpAutocompleteDocente adp;
    JSONObject selectedDocente;
    ModelUser selectedDoc;

    public FrgRegisterUser() {
        // Required empty public constructor
    }

    public static FrgRegisterUser newInstance(String param1, String param2) {
        FrgRegisterUser fragment = new FrgRegisterUser();
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
        View view = inflater.inflate(R.layout.fragment_frg_register_user, container, false);
        docentes = new ArrayList<>();

        Lnombres = view.findViewById(R.id.Lnombres);
        nombre = view.findViewById(R.id.nombres);
        TextChanged(nombre,null,Lnombres,Merror);

        Lapellidos = view.findViewById(R.id.Lapellidos);
        apellido = view.findViewById(R.id.apellidos);
        TextChanged(apellido,null,Lapellidos,Merror);

        Ltelefono = view.findViewById(R.id.Ltelefono);
        telefono = view.findViewById(R.id.telefono);
        TextChanged(telefono,null,Ltelefono,Merror);

        Lemail = view.findViewById(R.id.Lemail);
        email = view.findViewById(R.id.email);
        TextChanged(email,null,Lemail,Merror);

        LFechaNac = view.findViewById(R.id.LFechaNac);
        fechaNac = view.findViewById(R.id.txtFechaNac);
        TextChanged(fechaNac,null,LFechaNac,Merror);
        fechaNac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                anio = calendar.get(Calendar.YEAR);
                mes = calendar.get(Calendar.MONTH);
                dia = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),mDateSetListener,anio,mes,dia);
                datePickerDialog.show();
            }
        });

        Lusuario = view.findViewById(R.id.Lusuario);
        usuario = view.findViewById(R.id.txtusuario);
        TextChanged(usuario,null,Lusuario,Merror);

        Lclave = view.findViewById(R.id.Lclave);
        clave = view.findViewById(R.id.clave);
        TextChanged(clave,null,Lclave,Merror);

        Lconfclave = view.findViewById(R.id.Lconfclave);
        confirclave = view.findViewById(R.id.confclave);
        TextChanged(confirclave,null,Lconfclave,Merror);

        switch_docent = view.findViewById(R.id.switch_docent);
        datosDocente = view.findViewById(R.id.datosDocente);

        Ldocente = view.findViewById(R.id.Ldocente);
        docente = view.findViewById(R.id.docente);
        docente.setThreshold(1);
        autocomplete();

        switch_docent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    datosDocente.setVisibility(View.GONE);
                else {
                    datosDocente.setVisibility(View.VISIBLE);
                    Validar(null,docente,Ldocente,Merror);
                }
            }
        });

        btnRegistrarse = view.findViewById(R.id.btnRegister);
        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!switch_docent.isChecked()){
                    if(!Validar(null,docente,Ldocente,Merror))
                        return;
                }
                if(Validar(nombre,null,Lnombres,Merror) & Validar(apellido,null,Lapellidos,Merror) &
                        Validar(telefono,null,Ltelefono,Merror) & Validar(email,null,Lemail,Merror) &
                        Validar(fechaNac,null,LFechaNac,Merror) & Validar(usuario,null,Lusuario,Merror) &
                        Validar(clave,null,Lclave,Merror) & Validar(confirclave,null,Lconfclave,Merror)){
                    if(clave.getText().toString().equals(confirclave.getText().toString())){
                        Lconfclave.setError(null);
                        try {
                            createUsuario();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else
                        Lconfclave.setError("Las contraseñas no coinciden");
                }
            }
        });
        return view;
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener(){
                @Override
                public void onDateSet(DatePicker datePicker, int Dyear, int Dmonth, int Dday) {
                    anio = Dyear;
                    mes = Dmonth;
                    dia = Dday;
                    fechaNac.setText(anio + "-" + (mes+1) +"-" + dia);
                }
            };

    public Boolean Validar(TextInputEditText txt, AutoCompleteTextView com, TextInputLayout layout, String error){
        Boolean err = false;
        if(txt != null){
            if(txt.getText().length() > 0){
                if(txt.equals(telefono)){
                    if(txt.getText().length() >= 9){
                        layout.setError(null);
                        err = true;
                    }
                    else{
                        layout.setError("Mínimo 9 dígitos");
                        err = false;
                    }
                }
                else {
                    layout.setError(null);
                    err = true;
                }
            }
            else{
                layout.setError(error);
                err = false;
            }
        }
        else {
            if(com.getText().length() > 0){
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

    public void TextChanged(TextInputEditText txt, AutoCompleteTextView com, TextInputLayout layout, String error){
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
                    Validar(txt,com,layout,error);
                }
            });
        }
        else {
            com.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    Validar(txt,com,layout,error);
                }
            });
        }
    }

    public void autocomplete(){
        try {
            // Crear nueva cola de peticiones
            requestQueue = Volley.newRequestQueue(getContext());
            jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            ModelUser user = null;
                            try {
                                for (int i = 0; i < response.length(); i++) {
                                    user = new ModelUser();
                                    JSONObject objUser = response.getJSONObject(i);
                                    if (objUser.getString("tipousuario").equals("DC")) {
                                        if (!objUser.get("docente").equals(null)) {
                                            JSONObject ObjDatos = (JSONObject) objUser.get("docente");
                                            user.setId(ObjDatos.getInt("id"));
                                            user.setNombres(ObjDatos.getString("nombres"));
                                            user.setApellidos(ObjDatos.getString("apellidos"));
                                            user.setTelefono(ObjDatos.getString("telefono"));
                                            user.setCorreo(ObjDatos.getString("correo"));
                                            user.setFechanacimiento(ObjDatos.getString("fechanacimiento"));
                                            user.setActivo(objUser.getBoolean("activo"));
                                            docentes.add(user);
                                        }
                                    }
                                }
                                adp = new AdpAutocompleteDocente(getContext(), docentes);
                                docente.setAdapter(adp);
                                docente.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                                        selectedDoc = (ModelUser) adapterView.getItemAtPosition(pos);
                                        selectedDocente = new JSONObject();
                                        try {
                                            selectedDocente.put("id", selectedDoc.getId());
                                            selectedDocente.put("nombres", selectedDoc.getNombres());
                                            selectedDocente.put("apellidos", selectedDoc.getApellidos());
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            VolleyLog.e("Error: ", error.getMessage());
                        }
                    });
            requestQueue.add(jsonArrayRequest);
        }
        catch (Exception e){}
    }

    public void createUsuario() throws JSONException {
        // Crear nueva cola de peticiones
        requestQueue= Volley.newRequestQueue(getContext());
        //Parámetros a enviar a la API
        JSONObject param = new JSONObject();
        param.put("usuario", usuario.getText().toString().trim());
        param.put("clave", clave.getText().toString().trim());
        param.put("isDocente", switch_docent.isChecked());
        if(!switch_docent.isChecked()){
            if(selectedDocente.equals(null)){
                Ldocente.setError("Docente no válido");
                return;
            }
            else
                param.put("selectedDocente", selectedDocente);
        }
        param.put("nombres", nombre.getText().toString().trim());
        param.put("apellidos", apellido.getText().toString().trim());
        param.put("telefono", telefono.getText().toString().trim());
        param.put("correo", email.getText().toString().trim());
        param.put("fechanacimiento", fechaNac.getText().toString().trim());
        JsonObjectRequest request_json = new JsonObjectRequest(url, param,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.length() > 1)
                            {
                                Toast.makeText(getContext(),"Usuario Registrado",Toast.LENGTH_LONG).show();
                                redirectLogin();
                            }
                            else
                                Toast.makeText(getContext(),response.get("message").toString(),Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Toast.makeText(getContext(),"Error de conexión",Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                Toast.makeText(getContext(),error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        // Añadir petición a la cola
        requestQueue.add(request_json);
    }

    private void redirectLogin() {
        fragment = new FrgLogin();
        getFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
    }
}