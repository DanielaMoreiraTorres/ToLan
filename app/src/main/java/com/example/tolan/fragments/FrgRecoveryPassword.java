package com.example.tolan.fragments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.tolan.R;
import com.example.tolan.clases.ClssConvertTextToSpeech;
import com.example.tolan.clases.ClssValidations;
import com.example.tolan.clases.ClssVolleySingleton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import java.util.Locale;

public class FrgRecoveryPassword extends Fragment {

    static TextToSpeech textToSpeech;
    //ClssConvertTextToSpeech tts;
    private Button otro, recovery;
    private TextView txtRec, txtTittle;
    private TextInputEditText celular, correo;
    private TextInputLayout Lcelular, Lcorreo;
    private ClssValidations validate;
    String Merror = "Campo obligatorio";
    //private RequestQueue requestQueue;
    private String urlP;
    private String urlE;

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
        textToSpeech = new TextToSpeech(getContext(), i -> reproducirAudio(i, getString(R.string.msrecemail)));
        if (getArguments() != null) {
        }
    }

    public void reproducirAudio(int i, String mensaje) {
        if (i != TextToSpeech.ERROR) {
            textToSpeech.setLanguage(Locale.getDefault());
            textToSpeech.speak(mensaje, TextToSpeech.QUEUE_FLUSH, null);
        }
        //tts = new ClssConvertTextToSpeech();
        //tts.init(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recovery_password, container, false);
        try {
            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
            validate = new ClssValidations();
            urlP = getString(R.string.urlBase) + "usuario/recoveryPhone?telefono=";
            urlE = getString(R.string.urlBase) + "usuario/recoveryEmail?correo=";
            txtRec = view.findViewById(R.id.txtRec);
            txtRec.setOnClickListener(v -> ClssConvertTextToSpeech.getIntancia(v.getContext()).reproduce(txtRec.getText().toString()));
            //tts.reproduce(txtRec.getText().toString()));
            txtTittle = view.findViewById(R.id.txtRecoveryMetodo);
            txtTittle.setOnClickListener(v -> ClssConvertTextToSpeech.getIntancia(v.getContext()).reproduce(txtTittle.getText().toString()));
            //tts.reproduce(txtTittle.getText().toString()));
            Lcelular = view.findViewById(R.id.Ltelefono);
            celular = view.findViewById(R.id.telefono);
            Lcelular.setVisibility(View.GONE);
            validate.TextChanged(celular, null, Lcelular, Merror);
            Lcorreo = view.findViewById(R.id.Lemail);
            correo = view.findViewById(R.id.email);
            validate.TextChanged(correo, null, Lcorreo, Merror);
            otro = view.findViewById(R.id.otro);
            otro.setOnClickListener(v -> OtroMetodo());
            otro.setVisibility(View.GONE);
            recovery = view.findViewById(R.id.recovery);
            recovery.setOnClickListener(v -> Recovery());
        } catch (Exception e) {
        }
        return view;
    }

    public void OtroMetodo() {
        //tts.reproduce(otro.getText().toString());
        ClssConvertTextToSpeech.getIntancia(getContext()).reproduce(otro.getText().toString());
        if (Lcorreo.getVisibility() == View.GONE) {
            txtTittle.setText(getString(R.string.msrecemail));
            //tts.reproduce(txtTittle.getText().toString());
            ClssConvertTextToSpeech.getIntancia(getContext()).reproduce(txtTittle.getText().toString());
            Lcorreo.setVisibility(View.VISIBLE);
            Lcelular.setVisibility(View.GONE);
        } else {
            txtTittle.setText(getString(R.string.msrectel));
            //tts.reproduce(txtTittle.getText().toString());
            ClssConvertTextToSpeech.getIntancia(getContext()).reproduce(txtTittle.getText().toString());
            Lcorreo.setVisibility(View.GONE);
            Lcelular.setVisibility(View.VISIBLE);
        }
    }

    public void SendDatos(String url) {
        // Crear nueva cola de peticiones
        //requestQueue= Volley.newRequestQueue(getContext());
        JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //tts.reproduce(response.get("message").toString());
                            ClssConvertTextToSpeech.getIntancia(getContext()).reproduce(response.get("message").toString());
                            Toast.makeText(getContext(), response.get("message").toString(), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            //Toast.makeText(getContext(),"Error de conexión",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                Toast.makeText(getContext(), "Error de conexión con el servidor.\nIntente nuevamente", Toast.LENGTH_SHORT).show();
                //tts.reproduce("Error de conexión con el servidor. Intente nuevamente");
                ClssConvertTextToSpeech.getIntancia(getContext()).reproduce("Error de conexión con el servidor. Intente nuevamente");
            }
        });
        // Añadir petición a la cola
        //requestQueue.add(request_json);
        ClssVolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(request_json);
    }

    public void Recovery() {
        //tts.reproduce(recovery.getText().toString());
        ClssConvertTextToSpeech.getIntancia(getContext()).reproduce(recovery.getText().toString());
        String url = "";
        if (Lcorreo.getVisibility() == View.GONE) {
            url = urlP + celular.getText().toString().trim();
            if (validate.Validar(celular, null, Lcelular, Merror))
                SendDatos(url);
            else {
                //tts.reproduce("Número de celular no válido");
                ClssConvertTextToSpeech.getIntancia(getContext()).reproduce("Número de celular no válido");
                Toast.makeText(getContext(), "Número de celular no válido", Toast.LENGTH_SHORT).show();
            }
        } else {
            url = urlE + correo.getText().toString().trim();
            if (validate.Validar(correo, null, Lcorreo, Merror))
                SendDatos(url);
            else {
                //tts.reproduce("Correo no válido");
                ClssConvertTextToSpeech.getIntancia(getContext()).reproduce("Correo no válido");
                Toast.makeText(getContext(), "Correo no válido", Toast.LENGTH_SHORT).show();

            }
        }
    }
}