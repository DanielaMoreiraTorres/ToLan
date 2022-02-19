package com.example.tolan.clases;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.AutoCompleteTextView;

import com.example.tolan.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ClssValidations {

    public Boolean Validar(TextInputEditText txt, AutoCompleteTextView com, TextInputLayout layout, String error){
        Boolean err = false;
        if(txt != null){
            if(txt.getText().length() > 0){
                if(txt.getId() == R.id.telefono){
                    if(txt.getText().length() >= 10){
                        layout.setError(null);
                        err = true;
                    }
                    else{
                        layout.setError("Mínimo 10 dígitos");
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
}
