package com.example.tolan.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.tolan.R;
import com.example.tolan.adapters.AdpAutocompleteTeacher;
import com.example.tolan.clases.ClssConvertTextToSpeech;
import com.example.tolan.clases.ClssStaticGroup;
import com.example.tolan.clases.ClssStaticUser;
import com.example.tolan.clases.ClssValidations;
import com.example.tolan.clases.ClssVolleySingleton;
import com.example.tolan.models.ModelUser;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FrgRegisterUser extends Fragment {

    //static ClssConvertTextToSpeech tts;
    //private RequestQueue requestQueue;
    private JsonArrayRequest jsonArrayRequest;
    private Toolbar toolbar;
    private ProgressBar progressBar;
    private View linea;
    private String url;
    private TextView txtReg,txtDatPer, txtDatUser;
    private TextInputLayout Lnombres, Lapellidos, Ltelefono, Lemail, LFechaNac, Ldocente, Lusuario, Lclave, Lconfclave;
    private TextInputEditText nombre, apellido, telefono, email, fechaNac, usuario, clave, confirclave;
    private LinearLayout datosDocente;
    private RadioGroup rgTipoUser;
    private MaterialRadioButton rbDocente;
    private AutoCompleteTextView docente;
    private Button btnRegistrarse;
    private ClssValidations validate;
    private Fragment fragment;
    private String Merror= "Campo obligatorio";
    private int anio, mes, dia;
    private List<ModelUser> docentes;
    private AdpAutocompleteTeacher adp;
    private JSONObject selectedDocente;
    private ModelUser selectedDoc;

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
        //tts = new ClssConvertTextToSpeech();
        //tts.init(getContext());
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register_user, container, false);
        try {
            toolbar = view.findViewById(R.id.toolbar);
            toolbar.setVisibility(View.GONE);
            setHasOptionsMenu(true);
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("");
            linea = view.findViewById(R.id.linea);
            linea.setVisibility(View.GONE);
            url = getString(R.string.urlBase) + "usuario";
            progressBar = view.findViewById(R.id.progressBar);
            validate = new ClssValidations();
            docentes = new ArrayList<>();
            txtReg = view.findViewById(R.id.txtReg);
            txtReg.setOnClickListener(v ->  ClssConvertTextToSpeech.getIntancia(v.getContext()).reproduce(txtReg.getText().toString()));
            //tts.reproduce(txtReg.getText().toString()));
            txtDatPer = view.findViewById(R.id.txtDatPer);
            txtDatPer.setOnClickListener(v ->  ClssConvertTextToSpeech.getIntancia(v.getContext()).reproduce(txtDatPer.getText().toString()));
            //tts.reproduce(txtDatPer.getText().toString()));
            txtDatUser = view.findViewById(R.id.txtDatUser);
            txtDatUser.setOnClickListener(v ->  ClssConvertTextToSpeech.getIntancia(v.getContext()).reproduce(txtDatUser.getText().toString()));
            //tts.reproduce(txtDatUser.getText().toString()));
            Lnombres = view.findViewById(R.id.Lnombres);
            nombre = view.findViewById(R.id.nombres);
            validate.TextChanged(nombre, null, Lnombres, Merror);
            Lapellidos = view.findViewById(R.id.Lapellidos);
            apellido = view.findViewById(R.id.apellidos);
            validate.TextChanged(apellido, null, Lapellidos, Merror);
            Ltelefono = view.findViewById(R.id.Ltelefono);
            telefono = view.findViewById(R.id.telefono);
            validate.TextChanged(telefono, null, Ltelefono, Merror);
            Lemail = view.findViewById(R.id.Lemail);
            email = view.findViewById(R.id.email);
            validate.TextChanged(email, null, Lemail, Merror);
            LFechaNac = view.findViewById(R.id.LFechaNac);
            fechaNac = view.findViewById(R.id.txtFechaNac);
            validate.TextChanged(fechaNac, null, LFechaNac, Merror);
            fechaNac.setOnClickListener(v -> showDialog());
            Lusuario = view.findViewById(R.id.Lusuario);
            usuario = view.findViewById(R.id.txtusuario);
            validate.TextChanged(usuario, null, Lusuario, Merror);
            Lclave = view.findViewById(R.id.Lclave);
            clave = view.findViewById(R.id.clave);
            validate.TextChanged(clave, null, Lclave, Merror);
            Lconfclave = view.findViewById(R.id.Lconfclave);
            confirclave = view.findViewById(R.id.confclave);
            validate.TextChanged(confirclave, null, Lconfclave, Merror);
            rbDocente = view.findViewById(R.id.rbDocente);
            rgTipoUser = view.findViewById(R.id.rgTipoUser);
            rgTipoUser.setOnCheckedChangeListener((group, checkid) -> isChecked());
            datosDocente = view.findViewById(R.id.datosDocente);
            Ldocente = view.findViewById(R.id.Ldocente);
            docente = view.findViewById(R.id.docente);
            if(ClssStaticGroup.docente != null) {
                toolbar.setVisibility(View.VISIBLE);
                linea.setVisibility(View.VISIBLE);
                txtReg.setText("Registro de estudiante");
                rbDocente.setEnabled(false);
                docente.setText(ClssStaticGroup.docente);
                docente.setEnabled(false);
                selectedDocente = new JSONObject();
                selectedDocente.put("id", ClssStaticGroup.iddocente);
                selectedDocente.put("nombres", ClssStaticUser.nombres);
                selectedDocente.put("apellidos", ClssStaticUser.apellidos);
            } else {
                txtReg.setText("Registro de usuario");
                docente.setThreshold(1);
                autocomplete();
            }
            btnRegistrarse = view.findViewById(R.id.btnRegister);
            btnRegistrarse.setOnClickListener(v -> RegisterUser());
        }
        catch (Exception e){}
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

    private void showDialog(){
        final Calendar calendar = Calendar.getInstance();
        anio = calendar.get(Calendar.YEAR);
        mes = calendar.get(Calendar.MONTH);
        dia = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),mDateSetListener,anio,mes,dia);
        datePickerDialog.show();
    }

    private void isChecked(){
        if (rbDocente.isChecked())
            datosDocente.setVisibility(View.GONE);
        else {
            datosDocente.setVisibility(View.VISIBLE);
            validate.Validar(null,docente,Ldocente,Merror);
        }
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

    private void autocomplete(){
        try {
            // Crear nueva cola de peticiones
            //requestQueue = Volley.newRequestQueue(getContext());
            jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            ModelUser user = null;
                            try {
                                for (int i = 0; i < response.length(); i++) {
                                    user = new ModelUser();
                                    JSONObject objUser = response.getJSONObject(i);
                                    if (objUser.getString("tipousuario").trim().equals("DC")) {
                                        if (!objUser.get("docente").equals(null)) {
                                            JSONObject ObjDatos = (JSONObject) objUser.get("docente");
                                            user.setId(ObjDatos.getInt("id"));
                                            user.setNombres(ObjDatos.getString("nombres").trim());
                                            user.setApellidos(ObjDatos.getString("apellidos").trim());
                                            user.setTelefono(ObjDatos.getString("telefono").trim());
                                            user.setCorreo(ObjDatos.getString("correo").trim());
                                            user.setFechanacimiento(ObjDatos.getString("fechanacimiento").trim());
                                            user.setActivo(objUser.getBoolean("activo"));
                                            docentes.add(user);
                                        }
                                    }
                                }
                                if(getContext() != null) {
                                    adp = new AdpAutocompleteTeacher(getContext(), docentes);
                                    docente.setAdapter(adp);
                                    docente.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                                            try {
                                                Ldocente.setError(null);
                                                selectedDoc = (ModelUser) adapterView.getItemAtPosition(pos);
                                                selectedDocente = new JSONObject();
                                                selectedDocente.put("id", selectedDoc.getId());
                                                selectedDocente.put("nombres", selectedDoc.getNombres().trim());
                                                selectedDocente.put("apellidos", selectedDoc.getApellidos().trim());
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

    private void RegisterUser(){
        ClssConvertTextToSpeech.getIntancia(getContext()).reproduce(btnRegistrarse.getText().toString().trim());
        if(!rbDocente.isChecked()){
            if(!validate.Validar(null,docente,Ldocente,Merror)){
                Ldocente.setError("Docente no válido");
                //tts.reproduce("Docente no válido");
                ClssConvertTextToSpeech.getIntancia(getContext()).reproduce("Docente no válido");
                return;
            }
        }
        if(validate.Validar(nombre,null,Lnombres,Merror) & validate.Validar(apellido,null,Lapellidos,Merror) &
                validate.Validar(telefono,null,Ltelefono,Merror) & validate.Validar(email,null,Lemail,Merror) &
                validate.Validar(fechaNac,null,LFechaNac,Merror) & validate.Validar(usuario,null,Lusuario,Merror) &
                validate.Validar(clave,null,Lclave,Merror) & validate.Validar(confirclave,null,Lconfclave,Merror)){
            if(clave.getText().toString().trim().equals(confirclave.getText().toString().trim())){
                Lconfclave.setError(null);
                try {
                    createUsuario();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else{
                //tts.reproduce("Las contraseñas no coinciden");
                ClssConvertTextToSpeech.getIntancia(getContext()).reproduce("Las contraseñas no coinciden");
                Lconfclave.setError("Las contraseñas no coinciden");
            }
        }
        else{
            //tts.reproduce("Datos no válidos");
            ClssConvertTextToSpeech.getIntancia(getContext()).reproduce("Datos no válidos");
            Toast.makeText(getContext(),"Datos no válidos",Toast.LENGTH_SHORT).show();
        }
    }

    private void createUsuario() throws JSONException {
        progressBar.setVisibility(View.VISIBLE);
        // Crear nueva cola de peticiones
        //requestQueue= Volley.newRequestQueue(getContext());
        //Parámetros a enviar a la API
        JSONObject param = new JSONObject();
        param.put("usuario", usuario.getText().toString().trim());
        param.put("clave", clave.getText().toString().trim());
        param.put("isDocente", rbDocente.isChecked());
        if(!rbDocente.isChecked()){
            if(selectedDocente.equals(null)){
                //tts.reproduce("Docente no válido");
                ClssConvertTextToSpeech.getIntancia(getContext()).reproduce("Docente no válido");
                Ldocente.setError("Docente no válido");
                progressBar.setVisibility(View.GONE);
                return;
            }
            else{
                if(docente.getText().toString().trim().equals(selectedDocente.getString("nombres").trim() + " " + selectedDocente.getString("apellidos").trim())){
                    Ldocente.setError(null);
                    param.put("selectedDocente", selectedDocente);
                }
                else{
                    //tts.reproduce("Docente no válido");
                    ClssConvertTextToSpeech.getIntancia(getContext()).reproduce("Docente no válido");
                    Ldocente.setError("Docente no válido");
                    progressBar.setVisibility(View.GONE);
                    return;
                }
            }
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
                                //tts.reproduce("Usuario Registrado");
                                progressBar.setVisibility(View.GONE);
                                ClssConvertTextToSpeech.getIntancia(getContext()).reproduce("Usuario Registrado");
                                Toast.makeText(getContext(),"Usuario Registrado",Toast.LENGTH_SHORT).show();
                                redirectLogin();
                            }
                            else{
                                //tts.reproduce(response.get("message").toString());
                                progressBar.setVisibility(View.GONE);
                                ClssConvertTextToSpeech.getIntancia(getContext()).reproduce(response.get("message").toString());
                                Toast.makeText(getContext(),response.get("message").toString(),Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            progressBar.setVisibility(View.GONE);
                            //Toast.makeText(getContext(),"Error de conexión",Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(),"Error de conexión con el servidor. Intente nuevamente",Toast.LENGTH_SHORT).show();
                //tts.reproduce("Error de conexión con el servidor. Intente nuevamente");
                ClssConvertTextToSpeech.getIntancia(getContext()).reproduce("Error de conexión con el servidor. Intente nuevamente");
            }
        });
        // Añadir petición a la cola
        //requestQueue.add(request_json);
        ClssVolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(request_json);
    }

    private void redirectLogin() {
        fragment = new FrgLogin();
        getFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
    }
}