package com.example.tolan.fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.tolan.R;
import com.example.tolan.clases.ClssConvertTextToSpeech;
import com.example.tolan.clases.ClssStaticGroup;
import com.example.tolan.clases.ClssValidations;
import com.example.tolan.controller.ControllerUser;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class FrgLogin extends Fragment {

    ProgressBar progressBar;
    //static ClssConvertTextToSpeech tts;
    private ControllerUser controllerUser;
    private Button btnLogin, register;
    private TextView forgetPass, txtIni;
    private TextInputEditText user, password;
    private TextInputLayout Lusuario, Lclave;
    private ClssValidations validate;
    private String Merror = "Campo obligatorio";
    private Fragment fragment;

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
        //tts = new ClssConvertTextToSpeech();
        //tts.init(getContext());
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            resetClssStatic();
            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
            validate = new ClssValidations();
            progressBar = view.findViewById(R.id.progressBar);
            controllerUser = new ControllerUser(getContext(), progressBar);
            Lusuario = view.findViewById(R.id.Lusuario);
            txtIni = view.findViewById(R.id.txtIni);
            txtIni.setOnClickListener(v -> ClssConvertTextToSpeech.getIntancia(v.getContext()).reproduce(txtIni.getText().toString()));
            //tts.reproduce(txtIni.getText().toString()));
            user = view.findViewById(R.id.txtuser);
            validate.TextChanged(user, null, Lusuario, Merror);
            Lclave = view.findViewById(R.id.Lclave);
            password = view.findViewById(R.id.txtPass);
            validate.TextChanged(password, null, Lclave, Merror);
            btnLogin = view.findViewById(R.id.btnSigin);
            btnLogin.setOnClickListener(v -> Login());
            forgetPass = view.findViewById(R.id.forget);
            forgetPass.setOnClickListener(v -> Forget());
            register = view.findViewById(R.id.register);
            register.setOnClickListener(v -> RegisterUs());
        } catch (Exception e) {
        }
    }

    private void resetClssStatic() {
        ClssStaticGroup.id = 0 ;
        ClssStaticGroup.iddocente= 0;
        ClssStaticGroup.docente = null;
        ClssStaticGroup.idestudiante = 0;
        ClssStaticGroup.estudiante = null;
    }

    private void Login() {
        //Ocultar teclado
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(password.getWindowToken(), 0);
        //tts.reproduce(btnLogin.getText().toString());
        ClssConvertTextToSpeech.getIntancia(getContext()).reproduce(btnLogin.getText().toString());
        if (validate.Validar(user, null, Lusuario, Merror) & validate.Validar(password, null, Lclave, Merror)) {
            progressBar.setVisibility(View.VISIBLE);
            controllerUser.getUsuario(user.getText().toString().trim(),password.getText().toString().trim());
            controllerUser.getUsuario(user.getText().toString().trim(),password.getText().toString().trim());
        } else
            ClssConvertTextToSpeech.getIntancia(getContext()).reproduce("Datos no válidos");
    }

    private void RegisterUs() {
        //tts.reproduce(register.getText().toString());
        ClssConvertTextToSpeech.getIntancia(getContext()).reproduce(register.getText().toString());
        fragment = new FrgRegisterUser();
        getFragmentManager().beginTransaction().replace(R.id.content, fragment).addToBackStack(null).commit();
    }

    @SuppressLint("ResourceAsColor")
    private void Forget() {
        forgetPass.setTextColor(Color.parseColor("#44cccc"));
        //tts.reproduce(forgetPass.getText().toString());
        ClssConvertTextToSpeech.getIntancia(getContext()).reproduce(forgetPass.getText().toString());
        fragment = new FrgRecoveryPassword();
        getFragmentManager().beginTransaction().replace(R.id.content, fragment).addToBackStack(null).commit();
    }
}