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
import android.widget.CheckBox;
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
import com.example.tolan.MainActivity;
import com.example.tolan.R;
import com.example.tolan.adapters.AdpAutocompleteDocente;
import com.example.tolan.clases.ClssConvertirTextoAVoz;
import com.example.tolan.clases.ClssStaticGrupo;
import com.example.tolan.clases.ClssStaticUser;
import com.example.tolan.clases.ClssValidations;
import com.example.tolan.clases.ClssVolleySingleton;
import com.example.tolan.models.ModelLevel;
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

    //static ClssConvertirTextoAVoz tts;
    //private RequestQueue requestQueue;
    private JsonArrayRequest jsonArrayRequest;
    private Toolbar toolbar;
    private ProgressBar progressBar;
    private View linea;
    private String url, urlP;
    private TextView txtReg,txtDatPer, txtDatUser;
    private TextInputLayout Lnombres, Lapellidos, Ltelefono, Lemail, LFechaNac, Ldocente, Lusuario, LclaveAct, Lclave, Lconfclave;
    private TextInputEditText nombre, apellido, telefono, email, fechaNac, usuario, claveAct, clave, confirclave;
    private LinearLayout datPerson, datUser, datosDocente;
    private CheckBox checkBoxUser;
    private RadioGroup rgTipoUser;
    private MaterialRadioButton rbDocente, rbEstudiante;
    private AutoCompleteTextView docente;
    private Button btnRegistrarse;
    private ClssValidations validate;
    private Fragment fragment;
    private String Merror= "Campo obligatorio";
    private int anio, mes, dia;
    private List<ModelUser> docentes;
    private AdpAutocompleteDocente adp;
    private JSONObject selectedDocente;
    private ModelUser selectedDoc;
    private Boolean registrar;

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
            registrar = getArguments().getBoolean("registrar");
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
            datPerson = view.findViewById(R.id.datPerson);
            datUser = view.findViewById(R.id.datUser);
            LclaveAct = view.findViewById(R.id.LclaveAct);
            checkBoxUser = view.findViewById(R.id.checkBoxUser);
            txtReg = view.findViewById(R.id.txtReg);
            txtReg.setOnClickListener(v ->  ClssConvertirTextoAVoz.getIntancia(v.getContext()).reproduce(txtReg.getText().toString()));
            //tts.reproduce(txtReg.getText().toString()));
            txtDatPer = view.findViewById(R.id.txtDatPer);
            txtDatPer.setOnClickListener(v ->  ClssConvertirTextoAVoz.getIntancia(v.getContext()).reproduce(txtDatPer.getText().toString()));
            //tts.reproduce(txtDatPer.getText().toString()));
            txtDatUser = view.findViewById(R.id.txtDatUser);
            txtDatUser.setOnClickListener(v ->  ClssConvertirTextoAVoz.getIntancia(v.getContext()).reproduce(txtDatUser.getText().toString()));
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
            validate.TextChanged(claveAct, null, LclaveAct, Merror);
            LclaveAct = view.findViewById(R.id.LclaveAct);
            claveAct = view.findViewById(R.id.claveAct);
            clave = view.findViewById(R.id.clave);
            validate.TextChanged(clave, null, Lclave, Merror);
            Lconfclave = view.findViewById(R.id.Lconfclave);
            confirclave = view.findViewById(R.id.confclave);
            validate.TextChanged(confirclave, null, Lconfclave, Merror);
            rbDocente = view.findViewById(R.id.rbDocente);
            rbEstudiante = view.findViewById(R.id.rbEstudiante);
            rgTipoUser = view.findViewById(R.id.rgTipoUser);
            rgTipoUser.setOnCheckedChangeListener((group, checkid) -> isChecked());
            datosDocente = view.findViewById(R.id.datosDocente);
            Ldocente = view.findViewById(R.id.Ldocente);
            docente = view.findViewById(R.id.docente);
            /*if(!ClssStaticGrupo.docente.isEmpty()) {
                toolbar.setVisibility(View.VISIBLE);
                linea.setVisibility(View.VISIBLE);
                txtReg.setText("Registro de estudiante");
                rbDocente.setEnabled(false);
                docente.setText(ClssStaticGrupo.docente);
                docente.setEnabled(false);
                selectedDocente = new JSONObject();
                selectedDocente.put("id", ClssStaticUser.id);
                selectedDocente.put("nombres", ClssStaticUser.nombres);
                selectedDocente.put("apellidos", ClssStaticUser.apellidos);
            } else {
                txtReg.setText("Registro de usuario");
                docente.setThreshold(1);
                autocomplete();
            }*/
            btnRegistrarse = view.findViewById(R.id.btnRegister);
            btnRegistrarse.setOnClickListener(v -> {
                try {
                    RegisterUser();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
            llenarComponents();
        }
        catch (Exception e){}
        return view;
    }

    private void llenarComponents() throws JSONException {
        try {
            if (registrar == null & !ClssStaticUser.usuario.isEmpty()) {
                toolbar.setVisibility(View.VISIBLE);
                linea.setVisibility(View.VISIBLE);
                txtReg.setText("Editar datos");
                LclaveAct.setVisibility(View.VISIBLE);
                Lclave.setHint("Contraseña nueva");
                btnRegistrarse.setText("Aceptar");
                nombre.setText(ClssStaticUser.nombres);
                apellido.setText(ClssStaticUser.apellidos);
                telefono.setText(ClssStaticUser.telefono);
                email.setText(ClssStaticUser.correo);
                fechaNac.setText(ClssStaticUser.fechanacimiento);
                usuario.setText(ClssStaticUser.usuario);
                checkBoxUser.setEnabled(true);
                checkBoxUser.setChecked(ClssStaticUser.activo);
            }
        /*if(ClssStaticUser.tipousuario.trim().equals("AD")){ //EDITAR DATOS ADMIN
            toolbar.setVisibility(View.VISIBLE);
            linea.setVisibility(View.VISIBLE);
            txtReg.setText("Editar datos");
            datPerson.setVisibility(View.GONE);
            rgTipoUser.setVisibility(View.GONE);
            datosDocente.setVisibility(View.GONE);
            LclaveAct.setVisibility(View.VISIBLE);
            Lclave.setHint("Contraseña nueva");
            btnRegistrarse.setText("Aceptar");
        }*/
            if (ClssStaticUser.tipousuario.trim().equals("ES")) { //EDITAR DATOS ESTUDIANTE
                rbDocente.setEnabled(false);
                datosDocente.setVisibility(View.VISIBLE);
                docente.setEnabled(false);
            } else if (ClssStaticUser.tipousuario.trim().equals("DC")) {
                if (registrar == null) //EDITAR DATOS DOCENTE
                {
                    rbDocente.setEnabled(true);
                    rbDocente.setChecked(true);
                    rbEstudiante.setEnabled(false);
                    rbEstudiante.setChecked(false);
                    datosDocente.setVisibility(View.GONE);
                } else { //DOCENTE REGISTRA ESTUDIANTE A SU CARGO
                    toolbar.setVisibility(View.VISIBLE);
                    linea.setVisibility(View.VISIBLE);
                    txtReg.setText("Registro de estudiante");
                    LclaveAct.setVisibility(View.GONE);
                    rbDocente.setEnabled(false);
                    docente.setText(ClssStaticGrupo.docente);
                    docente.setEnabled(false);
                    selectedDocente = new JSONObject();
                    selectedDocente.put("id", ClssStaticUser.id);
                    selectedDocente.put("nombres", ClssStaticUser.nombres);
                    selectedDocente.put("apellidos", ClssStaticUser.apellidos);
                }
            } else { //REGISTRO DE USUARIO
                toolbar.setVisibility(View.GONE);
                linea.setVisibility(View.GONE);
                txtReg.setText("Registro de usuario");
                LclaveAct.setVisibility(View.GONE);
                docente.setThreshold(1);
                autocomplete();
            }
        } catch (Exception e) {}
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
                                    adp = new AdpAutocompleteDocente(getContext(), docentes);
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

    private void RegisterUser() throws JSONException {
        ClssConvertirTextoAVoz.getIntancia(getContext()).reproduce(btnRegistrarse.getText().toString().trim());
        if(registrar == null & !ClssStaticUser.usuario.isEmpty()) {
            if (validate.Validar(nombre, null, Lnombres, Merror) & validate.Validar(apellido, null, Lapellidos, Merror) &
                    validate.Validar(telefono, null, Ltelefono, Merror) & validate.Validar(email, null, Lemail, Merror) &
                    validate.Validar(fechaNac, null, LFechaNac, Merror) & validate.Validar(usuario, null, Lusuario, Merror) &
                    validate.Validar(claveAct, null, LclaveAct, Merror) & validate.Validar(clave, null, Lclave, Merror) &
                    validate.Validar(confirclave, null, Lconfclave, Merror)) {
                if(claveAct.equals(ClssStaticUser.clave)) {
                    if (clave.getText().toString().trim().equals(confirclave.getText().toString().trim())) {
                        Lconfclave.setError(null);
                        createUsuario();
                    }
                    else {
                        ClssConvertirTextoAVoz.getIntancia(getContext()).reproduce("Las contraseñas no coinciden");
                        Lconfclave.setError("Las contraseñas no coinciden");
                    }
                }
                else {
                    ClssConvertirTextoAVoz.getIntancia(getContext()).reproduce("La contraseña actual no es correcta");
                    LclaveAct.setError("La contraseña actual no es correcta");
                }
            }
            else {
                ClssConvertirTextoAVoz.getIntancia(getContext()).reproduce("Datos no válidos");
                Toast.makeText(getContext(), "Datos no válidos", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            if (!rbDocente.isChecked()) {
                if (!validate.Validar(null, docente, Ldocente, Merror)) {
                    Ldocente.setError("Docente no válido");
                    //tts.reproduce("Docente no válido");
                    ClssConvertirTextoAVoz.getIntancia(getContext()).reproduce("Docente no válido");
                    return;
                }
            }
            if (validate.Validar(nombre, null, Lnombres, Merror) & validate.Validar(apellido, null, Lapellidos, Merror) &
                    validate.Validar(telefono, null, Ltelefono, Merror) & validate.Validar(email, null, Lemail, Merror) &
                    validate.Validar(fechaNac, null, LFechaNac, Merror) & validate.Validar(usuario, null, Lusuario, Merror) &
                    validate.Validar(clave, null, Lclave, Merror) & validate.Validar(confirclave, null, Lconfclave, Merror)) {
                if (clave.getText().toString().trim().equals(confirclave.getText().toString().trim())) {
                    Lconfclave.setError(null);
                        createUsuario();
                } else {
                    //tts.reproduce("Las contraseñas no coinciden");
                    ClssConvertirTextoAVoz.getIntancia(getContext()).reproduce("Las contraseñas no coinciden");
                    Lconfclave.setError("Las contraseñas no coinciden");
                }
            } else {
                //tts.reproduce("Datos no válidos");
                ClssConvertirTextoAVoz.getIntancia(getContext()).reproduce("Datos no válidos");
                Toast.makeText(getContext(), "Datos no válidos", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void createUsuario() throws JSONException {
        progressBar.setVisibility(View.VISIBLE);
        // Crear nueva cola de peticiones
        //requestQueue= Volley.newRequestQueue(getContext());
        //Parámetros a enviar a la API
        JSONObject param = new JSONObject();
        if(registrar == null & !ClssStaticUser.usuario.isEmpty()) {
            param.put("id",ClssStaticUser.id);
            param.put("isDocente",rbDocente.isChecked());
            param.put("activo", checkBoxUser.isChecked());
        }
        param.put("usuario", usuario.getText().toString().trim());
        param.put("clave", clave.getText().toString().trim());
        param.put("isDocente", rbDocente.isChecked());
        if(!rbDocente.isChecked()){
            if(registrar == null & ClssStaticUser.usuario.isEmpty()) {
                if (selectedDocente.equals(null)) {
                    //tts.reproduce("Docente no válido");
                    ClssConvertirTextoAVoz.getIntancia(getContext()).reproduce("Docente no válido");
                    Ldocente.setError("Docente no válido");
                    progressBar.setVisibility(View.GONE);
                    return;
                } else {
                    if (docente.getText().toString().trim().equals(selectedDocente.getString("nombres").trim() + " " + selectedDocente.getString("apellidos").trim())) {
                        Ldocente.setError(null);
                        param.put("selectedDocente", selectedDocente);
                    } else {
                        //tts.reproduce("Docente no válido");
                        ClssConvertirTextoAVoz.getIntancia(getContext()).reproduce("Docente no válido");
                        Ldocente.setError("Docente no válido");
                        progressBar.setVisibility(View.GONE);
                        return;
                    }
                }
            }
        }
        param.put("nombres", nombre.getText().toString().trim());
        param.put("apellidos", apellido.getText().toString().trim());
        param.put("telefono", telefono.getText().toString().trim());
        param.put("correo", email.getText().toString().trim());
        param.put("fechanacimiento", fechaNac.getText().toString().trim());
        if(registrar == null & !ClssStaticUser.usuario.isEmpty()) {
            JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.PUT, url, param,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response.length() > 1) {
                                    progressBar.setVisibility(View.GONE);
                                    ClssConvertirTextoAVoz.getIntancia(getContext()).reproduce("Usuario actualizado");
                                    Toast.makeText(getContext(), "Usuario actualizado", Toast.LENGTH_SHORT).show();
                                    redirectLogin();
                                } else {
                                    //tts.reproduce(response.get("message").toString());
                                    progressBar.setVisibility(View.GONE);
                                    ClssConvertirTextoAVoz.getIntancia(getContext()).reproduce(response.get("message").toString());
                                    Toast.makeText(getContext(), response.get("message").toString(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getContext(), "Error de conexión con el servidor. Intente nuevamente", Toast.LENGTH_SHORT).show();
                    //tts.reproduce("Error de conexión con el servidor. Intente nuevamente");
                    ClssConvertirTextoAVoz.getIntancia(getContext()).reproduce("Error de conexión con el servidor. Intente nuevamente");
                }
            });
            ClssVolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(request_json);
        }
        else {
            JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.POST, url, param,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response.length() > 1) {
                                    //tts.reproduce("Usuario Registrado");
                                    progressBar.setVisibility(View.GONE);
                                    ClssConvertirTextoAVoz.getIntancia(getContext()).reproduce("Usuario Registrado");
                                    Toast.makeText(getContext(), "Usuario Registrado", Toast.LENGTH_SHORT).show();
                                    redirectLogin();
                                } else {
                                    //tts.reproduce(response.get("message").toString());
                                    progressBar.setVisibility(View.GONE);
                                    ClssConvertirTextoAVoz.getIntancia(getContext()).reproduce(response.get("message").toString());
                                    Toast.makeText(getContext(), response.get("message").toString(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getContext(), "Error de conexión con el servidor. Intente nuevamente", Toast.LENGTH_SHORT).show();
                    //tts.reproduce("Error de conexión con el servidor. Intente nuevamente");
                    ClssConvertirTextoAVoz.getIntancia(getContext()).reproduce("Error de conexión con el servidor. Intente nuevamente");
                }
            });
            // Añadir petición a la cola
            //requestQueue.add(request_json);
            ClssVolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(request_json);
        }
    }

    private void redirectLogin() {
        fragment = new FrgLogin();
        getFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
    }
}