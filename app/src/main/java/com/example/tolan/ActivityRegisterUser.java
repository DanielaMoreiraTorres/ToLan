package com.example.tolan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.tolan.adapters.AdpAutocompleteDocente;
import com.example.tolan.models.ModelUser;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ActivityRegisterUser extends AppCompatActivity {

    private RequestQueue requestQueue;
    private JsonArrayRequest jsonArrayRequest;
    private String url = "https://db-bartolucci.herokuapp.com/usuario";
    private TextInputLayout Lnombres, Lapellidos, Ltelefono, Lemail, LFechaNac, Ldocente, Lusuario, Lclave, Lconfclave;
    private TextInputEditText nombre, apellido, telefono, email, fechaNac, usuario, clave, confirclave;
    private LinearLayout datosDocente;
    private SwitchMaterial switch_docent;
    private AutoCompleteTextView docente;
    String Merror= "Campo obligatorio";
    static final int DATE_ID = 0;
    int anio, mes, dia;
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat formato;
    Date date;
    Date dataFormateada;
    String fech;
    List<ModelUser> docentes;
    AdpAutocompleteDocente adp;
    JSONObject selectedDocente;
    ModelUser selectedDoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        docentes = new ArrayList<>();
        anio = calendar.get(Calendar.YEAR);
        mes = calendar.get(Calendar.MONTH);
        dia = calendar.get(Calendar.DAY_OF_MONTH);

        Lnombres = findViewById(R.id.Lnombres);
        nombre = findViewById(R.id.nombres);
        TextChanged(nombre,null,Lnombres,Merror);

        Lapellidos = findViewById(R.id.Lapellidos);
        apellido = findViewById(R.id.apellidos);
        TextChanged(apellido,null,Lapellidos,Merror);

        Ltelefono = findViewById(R.id.Ltelefono);
        telefono = findViewById(R.id.telefono);
        TextChanged(telefono,null,Ltelefono,Merror);

        Lemail = findViewById(R.id.Lemail);
        email = findViewById(R.id.email);
        TextChanged(email,null,Lemail,Merror);

        LFechaNac = findViewById(R.id.LFechaNac);
        fechaNac = findViewById(R.id.txtFechaNac);
        TextChanged(fechaNac,null,LFechaNac,Merror);
        fechaNac.setOnClickListener(view -> {
            showDialog(DATE_ID);
        });

        Lusuario = findViewById(R.id.Lusuario);
        usuario = findViewById(R.id.txtusuario);
        TextChanged(usuario,null,Lusuario,Merror);

        Lclave = findViewById(R.id.Lclave);
        clave = findViewById(R.id.clave);
        TextChanged(clave,null,Lclave,Merror);

        Lconfclave = findViewById(R.id.Lconfclave);
        confirclave = findViewById(R.id.confclave);
        TextChanged(confirclave,null,Lconfclave,Merror);

        switch_docent = findViewById(R.id.switch_docent);
        datosDocente = findViewById(R.id.datosDocente);

        Ldocente = findViewById(R.id.Ldocente);
        docente = findViewById(R.id.docente);
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
    }

    public Boolean Validar(TextInputEditText txt, AutoCompleteTextView com, TextInputLayout layout, String error){
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
        // Crear nueva cola de peticiones
        requestQueue = Volley.newRequestQueue(this);
        jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ModelUser user = null;
                        try {
                            for (int i=0;i<response.length();i++){
                                user = new ModelUser();
                                JSONObject objUser = response.getJSONObject(i);
                                if(objUser.getString("tipousuario").equals("DC")){
                                    if(!objUser.get("docente").equals(null)) {
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
                            adp = new AdpAutocompleteDocente(ActivityRegisterUser.this, docentes);
                            docente.setAdapter(adp);
                            docente.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                                    selectedDoc = (ModelUser) adapterView.getItemAtPosition(pos);
                                    selectedDocente = new JSONObject();
                                    try {
                                        selectedDocente.put("id",selectedDoc.getId());
                                        selectedDocente.put("nombres",selectedDoc.getNombres());
                                        selectedDocente.put("apellidos",selectedDoc.getApellidos());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }catch (JSONException e){
                            e.printStackTrace();
                            Toast.makeText(ActivityRegisterUser.this,e.toString(),Toast.LENGTH_LONG).show();
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

    public void RegisterClick(View view) throws JSONException {
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
                createUsuario();
            }
            else
                Lconfclave.setError("Las contraseñas no coinciden");
        }
    }

    /*public Boolean formatearFecha(){
        Boolean ban = true;
        try {
            formato = new SimpleDateFormat("yyyy-MM-dd");
            String data = fechaNac.getText().toString();
            dataFormateada = formato.parse(data);
        }catch (ParseException e){
            ban = false;
            LFechaNac.setError("El formato de fecha no es correcto \n Formato válido: aaaa-MM-dd");
            //Toast.makeText(ActivityRegisterUser.this,"El formato de fecha no es correcto",Toast.LENGTH_LONG).show();
        }
        return ban;
    }*/

    public void createUsuario() throws JSONException {
        try {
            formato = new SimpleDateFormat("yyyy-MM-dd");
            String data = fechaNac.getText().toString();
            dataFormateada = formato.parse(data);
            fech = formato.format(dataFormateada);
        }
        catch (ParseException ex){
            LFechaNac.setError("El formato de fecha no es correcto \n Formato válido: aaaa-MM-dd");
            Toast.makeText(ActivityRegisterUser.this,"El formato de fecha no es correcto",Toast.LENGTH_LONG).show();
        }
        // Crear nueva cola de peticiones
        requestQueue= Volley.newRequestQueue(ActivityRegisterUser.this);
        //Parámetros a enviar a la API
        JSONObject param = new JSONObject();
        param.put("usuario", usuario.getText().toString());
        param.put("clave", clave.getText().toString());
        param.put("isDocente", switch_docent.isChecked());
        if(!switch_docent.isChecked())
            param.put("selectedDocente", selectedDocente);
        param.put("nombres", nombre.getText().toString());
        param.put("apellidos", apellido.getText().toString());
        param.put("telefono", telefono.getText().toString());
        param.put("correo", email.getText().toString());
        param.put("fechanacimiento", fech);
        /*
        Map<String, String>  parameters = new HashMap<>();
        parameters.put("usuario", usuario.getText().toString());
        parameters.put("clave", clave.getText().toString());
        parameters.put("isDocente", "false");
        parameters.put("nombre", nombre.getText().toString());
        parameters.put("apellido", apellido.getText().toString());
        parameters.put("fechanacimiento", fechaNac.getText().toString());
        parameters.put("correo", email.getText().toString());
        parameters.put("telefono", telefono.getText().toString());
         */
        JsonObjectRequest request_json = new JsonObjectRequest(url, param,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Toast.makeText(ActivityRegisterUser.this,"Usuario Registrado",Toast.LENGTH_LONG).show();
                            redirectLogin();
                        } catch (Exception e) {
                            Toast.makeText(ActivityRegisterUser.this,"Error de conexión",Toast.LENGTH_LONG).show();
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

    private void redirectLogin() {
        /*
        Intent intent = new Intent(ActivityRegisterUser.this, ActivityLogin.class);
        startActivity(intent);*/
    }
}